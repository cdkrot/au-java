package me.cdkrot.javahw;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import javax.swing.event.HyperlinkEvent;
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

    private void resetTreeView() {
        treeView.setRoot(new TreeViewItem("/", new FileEntry("/", true)));
    }

    public void reactivateStartServerButton(boolean on){
        if (on)
            localButton.setText("Stop local server");
        else
            localButton.setText("Start local server");
    }

    public void clickLocal(MouseEvent mouseEvent) {
        if (serverThread != null) {
            serverThread.interrupt();
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
