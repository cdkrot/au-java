package me.cdkrot.javahw;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.util.Random;

public class CustomPane extends GridPane {
    private RectangleBox field[][];
    private int lastI = -1, lastJ = -1;
    private boolean block = false;

    public CustomPane(int n) {
        this.setPadding(new Insets(20));

        this.setVgap(10);
        this.setHgap(10);

        int values[] = new int[n * n];
        for (int i = 0; i != n * n; ++i)
            values[i] = 1 + i / 2;

        Random rnd = new Random();
        for (int i = 0; i != values.length; ++i) {
            int j = i + rnd.nextInt(values.length - i);

            int tmp = values[i];
            values[i] = values[j];
            values[j] = tmp;
        }

        field = new RectangleBox[n][n];
        for (int i = 0; i != n; ++i) {
            for (int j = 0; j != n; ++j) {
                field[i][j] = new RectangleBox(this, i, j, n, values[i * n + j]);
                this.add(field[i][j], i, j, 1, 1);
            }
        }
    }

    public void click(int i, int j) {
        if (block)
            return;
        if (field[i][j].isGuessed())
            return;

        if (lastI == -1) {
            lastI = i;
            lastJ = j;
            field[i][j].toggleHiglight(true);
        } else if (lastI != i || lastJ != j) {
            field[i][j].toggleHiglight(true);

            if (field[i][j].getValue() == field[lastI][lastJ].getValue()) {
                field[i][j].setGuessed();
                field[lastI][lastJ].setGuessed();
            } else {
                field[i][j].toggleText(true);
                field[lastI][lastJ].toggleText(true);

                block = true;

                Timeline timeline = new Timeline(new KeyFrame(Duration.millis(800), new TickHandler(i, j, lastI, lastJ)));

                timeline.play();
            }

            lastI = -1;
            lastJ = -1;
        }
    }

    public class TickHandler implements EventHandler<ActionEvent> {
        private int i1, j1, i2, j2;

        TickHandler(int i1, int j1, int i2, int j2) {
            this.i1 = i1;
            this.j1 = j1;
            this.i2 = i2;
            this.j2 = j2;
        }

        @Override
        public void handle(ActionEvent actionEvent) {
            field[i1][j1].toggleText(false);
            field[i2][j2].toggleText(false);
            field[i1][j1].toggleHiglight(false);
            field[i2][j2].toggleHiglight(false);
            block = false;
        }
    }
}
