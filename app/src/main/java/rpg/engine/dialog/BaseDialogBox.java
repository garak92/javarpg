package rpg.engine.dialog;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public abstract class BaseDialogBox {
    protected static final double TEXT_WIDTH = 400;
    protected Rectangle box = new Rectangle();
    protected Text text = new Text();
    protected TextFlow textFlow = new TextFlow(text);
    protected Pane pane;
    protected boolean open = false;

    protected BaseDialogBox(Pane pane) {
        this.pane = pane;

        Font rpgFont = Font.loadFont(getClass().getResourceAsStream("/fonts/HomeVideo-BLG6G.ttf"), 16);
        if (rpgFont == null) {
            rpgFont = Font.font("Serif", 16); // fallback
        }

        text.setFill(Color.WHITE);
        text.setFont(rpgFont);

        textFlow.setPrefWidth(TEXT_WIDTH);
        if (text.getText().length() < 30) {
            textFlow.setTextAlignment(TextAlignment.CENTER);
        } else {
            textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        }

        textFlow.setLineSpacing(4);

        box.setFill(Color.BLACK);
        box.setArcWidth(15);
        box.setArcHeight(15);
        box.setStroke(Color.DARKGRAY);
        box.setStrokeWidth(2);
    }

    protected void updateLayout(double centerX, double bottomY) {
        textFlow.setLayoutX(centerX);
        textFlow.setLayoutY(bottomY);

        Platform.runLater(() -> {
            textFlow.applyCss();
            textFlow.layout();

            double boxWidth = textFlow.getBoundsInLocal().getWidth() + 20;
            double boxHeight = textFlow.getBoundsInLocal().getHeight() + 20;

            box.setWidth(boxWidth);
            box.setHeight(boxHeight);

            // Now center horizontally, and move upward vertically
            double adjustedX = centerX - (boxWidth / 2);
            double adjustedY = bottomY - boxHeight;

            box.setX(adjustedX - 10);
            box.setY(adjustedY - 10);

            textFlow.setLayoutX(adjustedX);
            textFlow.setLayoutY(adjustedY);
        });
    }

    public void open() {
        if (!open) {
            pane.getChildren().addAll(box, textFlow);
            open = true;
        }
    }

    public void close() {
        if (open) {
            pane.getChildren().removeAll(box, textFlow);
            open = false;
        }
    }

    public boolean isOpen() {
        return open;
    }

    public abstract void use();
}

