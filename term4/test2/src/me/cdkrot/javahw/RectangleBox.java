package me.cdkrot.javahw;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleBox extends VBox {
    private int value;
    private Rectangle rec;
    private CustomPane controller;
    private Label lbl;
    private boolean guessed = false;

    public RectangleBox(CustomPane controller, int i, int j, int n, int value) {
        this.value = value;
        this.controller = controller;

        StackPane sp = new StackPane();

        rec = new Rectangle(100, 100);
        rec.setFill(Color.YELLOW);

        lbl = new Label();
        lbl.setText(String.valueOf(value));
        lbl.setVisible(false);

        sp.getChildren().add(rec);
        sp.getChildren().add(lbl);

        this.getChildren().add(sp);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                controller.click(i, j);
                // RectangleBox.this.toggleHiglight(true);
            }
        });
    }

    public int getValue() {
        return value;
    }

    public boolean isTextVisible(){
        return lbl.isVisible();
    }

    public void toggleHiglight(boolean on) {
        if (on)
            rec.setFill(Color.GREEN);
        else
            rec.setFill(Color.YELLOW);
    }

    public void toggleText(boolean on) {
        lbl.setVisible(on);
    }

    public void setGuessed() {
        rec.setFill(Color.WHITE);
        toggleText(true);
        guessed = true;
    }

    public boolean isGuessed() {
        return guessed;
    }
}
