package me.cdkrot.javahw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.*;

import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;

public class GuiController {
    @FXML
    private TextField textField;

    @FXML
    private Button connectButton;

    @FXML
    private Button localButton;

    @FXML
    private TreeView<FileEntry> treeView;

    private Socket sock;
    private DataInputStream socketInput;
    private DataOutputStream socketOutput;

    private Thread serverThread = null;

    private void lock(boolean b) {
        textField.setDisable(b);
        connectButton.setDisable(b);
    }

    /**
     * Setups the treeView
     */
    public void initialize() {
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    TreeViewItem item = (TreeViewItem)(treeView.getSelectionModel().getSelectedItem());
                    
                    if (item != null && item.isLeaf() && event.getClickCount() == 2) {
                        saveFile(item.getFullPath(), item.getBaseName());
                    }
                }
            });
    }

    /**
     * Handles the file saving
     * @param path to fetch
     * @param basename suggested name to save
     */
    public void saveFile(String path, String basename) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(basename);

        Window window = treeView.getScene().getWindow();
        File destination = fileChooser.showSaveDialog(window);
        if (destination == null || destination.isDirectory()) {
            Alert alert;
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selected destination is not valid");
            alert.show();
            return;
        }

        Task task = new Task<Boolean>() {
                @Override
                public Boolean call() {
                    byte[] data;
                    try {
                        data = Client.getFile(path, socketInput, socketOutput);
                    } catch (IOException ex) {
                        return false;
                    }

                    if (data == null)
                        return false;
                
                    try (FileOutputStream stream = new FileOutputStream(destination)) {
                        stream.write(data);
                    } catch (IOException ex) {
                        return false;
                    }

                    return true;
                }
            };

        task.setOnSucceeded((event) -> {
                boolean res = ((Task<Boolean>)(event.getSource())).getValue();
                Alert alert;
                if (res) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Download succeeded");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Download failed");

                }
                
                alert.show();
            });

        (new Thread(task)).start();
    }

    /**
     * Handles the connect button clicks
     * @param mouseEvent ignored
     */
    public void clickConnect(MouseEvent mouseEvent) {
        try {
            lock(true);
            sock = new Socket(textField.getText(), 2030);
            socketInput = new DataInputStream(sock.getInputStream());
            socketOutput = new DataOutputStream(sock.getOutputStream());

            resetTreeView();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection failure");
            alert.setContentText(ex.getMessage());

            lock(false);

            alert.show();
        }
    }

    /**
     * Reloads the tree view.
     */
    private void resetTreeView() {
        treeView.setRoot(new TreeViewItem("/", new FileEntry("/", true)));
    }

    /**
     * changes the server button state
     * @param on is running
     */
    public void reactivateStartServerButton(boolean on){
        if (on)
            localButton.setText("Stop local server");
        else
            localButton.setText("Start local server");
    }

    /**
     * Spawns or stops local server
     * @param mouseEvent ignored
     */ 
    public void clickLocal(MouseEvent mouseEvent) {
        if (serverThread != null) {
            serverThread.interrupt();
            try {
                serverThread.join();
            } catch (InterruptedException ex) {}
            
            serverThread = null;
            reactivateStartServerButton(false);
        } else {
            serverThread = new Thread(() -> {
                try {
                    Server.main(null);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    reactivateStartServerButton(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Server error");
                    alert.setContentText(ex.getMessage());
                    alert.show();
                }
            });
            serverThread.start();
            reactivateStartServerButton(true);
        }
    }

    /**
     * Represents an entry in the tree view.
     *
     * Performs dynamic content loading
     */
    private class TreeViewItem extends TreeItem<FileEntry> {
        private FileEntry entry;
        private String fullpath;
        private boolean loaded = false;

        public TreeViewItem(String fullpath, FileEntry entry) {
            super(entry);
            this.entry = entry;
            this.fullpath = fullpath;
        }

        @Override
        public boolean isLeaf() {
            return !entry.isDir;
        }

        public String getFullPath() {
            return fullpath;
        }

        public String getBaseName() {
            return entry.path;
        }
        
        @Override
        public ObservableList<TreeItem<FileEntry>> getChildren() {
            if (!loaded) {
                super.getChildren().setAll(loadChildren());
                loaded = true;
            }
            return super.getChildren();
        }

        public ObservableList<TreeItem<FileEntry>> loadChildren() {
            if (isLeaf())
                return FXCollections.emptyObservableList();
            ObservableList<TreeItem<FileEntry>> res = FXCollections.observableArrayList();

            try {
                FileEntry[] response = Client.listDirectory(fullpath, socketInput, socketOutput);
                if (res != null)
                    for (FileEntry fl: response)
                        res.add(new TreeViewItem(fullpath + fl.path + "/", fl));
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection failure");
                alert.setContentText(ex.getMessage());
                alert.show();
            }

            return res;
        }
    };
}
