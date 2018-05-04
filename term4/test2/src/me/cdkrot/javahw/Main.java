package me.cdkrot.javahw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static int n;

    @Override
    public void start(Stage stage) throws Exception{
        CustomPane pane = new CustomPane(n);

        Scene sc = new Scene(pane);

        stage.setTitle("Game");
        stage.setScene(sc);
        stage.setFullScreen(true);
        stage.show();
    }


    public static void main(String[] args) {
        n = 6;
        if (args.length == 1)
            try {
                n = Integer.parseInt(args[1]);
            } catch (Exception ex) {
                System.err.println("Argument is not an integer");
            }

        if (n % 2 == 1) {
            n = 6;
            System.err.println("Size should be even");
        }

        launch(args);
    }
}
