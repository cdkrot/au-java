package me.cdkrot.javahw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GuiMain extends Application {
    /**
     * Starts the application
     */
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        stage.setTitle("FTP GUI");
        
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Entry point for the GUI
     */
    public static void main(String[] args) {
        launch(args);
    }
}
