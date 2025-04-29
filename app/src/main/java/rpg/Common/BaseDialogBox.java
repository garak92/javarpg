package rpg.Common;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public abstract class BaseDialogBox {
    protected static final double TEXT_WIDTH = 800;
    protected Rectangle box = new Rectangle();
    protected Text text = new Text();
    protected TextFlow textFlow = new TextFlow(text);
    protected Pane pane;
    protected boolean open = false;

    protected BaseDialogBox(Pane pane) {
        this.pane = pane;

        Font rpgFont = Font.loadFont(getClass().getResourceAsStream("/fonts/UncialAntiqua.ttf"), 16);
        if (rpgFont == null) {
            rpgFont = Font.font("Serif", 16); // fallback
        }

        text.setFill(Color.WHITE);
        text.setFont(rpgFont);

        textFlow.setPrefWidth(TEXT_WIDTH);
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setLineSpacing(4);

        box.setFill(Color.BLACK);
        box.setArcWidth(15);
        box.setArcHeight(15);
        box.setStroke(Color.DARKGRAY);
        box.setStrokeWidth(2);
    }

    protected void updateLayout(double x, double y) {
        textFlow.setLayoutX(x);
        textFlow.setLayoutY(y);
        box.setX(x - 10);
        box.setY(y - 10);

        Platform.runLater(() -> {
            textFlow.applyCss();
            textFlow.layout();
            box.setWidth(textFlow.getBoundsInLocal().getWidth() + 20);
            box.setHeight(textFlow.getBoundsInLocal().getHeight() + 20);
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

