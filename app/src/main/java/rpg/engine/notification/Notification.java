package rpg.engine.notification;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import rpg.game.entities.player.Player;

import java.util.Objects;

public class Notification {
    TextFlow textFlow;
    Rectangle box;

    private Notification(TextFlow textFlow, Rectangle box) {
        this.textFlow = textFlow;
        this.box = box;
    }

    protected static Notification valueOf(String text) {
        Font rpgFont = Font.loadFont(Notification.class.getResourceAsStream("/fonts/HomeVideo-BLG6G.ttf"), 20);
        if (rpgFont == null) {
            rpgFont = Font.font("Serif", 16); // fallback
        }
        double posX = Player.getInstance().getImageView().getTranslateX();
        double posY = Player.getInstance().getImageView().getTranslateY();
        double playerHeight = Player.getInstance().getImageView().getFitHeight();
        TextFlow textFlow = new TextFlow();
        Rectangle box = new Rectangle();

        textFlow.setLayoutX(posX);
        textFlow.setLayoutY(posY);
        textFlow.setLineSpacing(2);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.setPrefWidth(300);
        textFlow.setPadding(new Insets(10)); // Add padding to the sides

        Image bgImage = new Image(Objects.requireNonNull(Notification.class.getResource("/sprites/other/notification.png"))
                .toExternalForm());
        box.setFill(new ImagePattern(bgImage, 0, 0, 1, 1, true));

        box.toFront();
        textFlow.toFront();

        Text content = new Text(text);
        content.setFont(rpgFont);
        content.setFill(Color.YELLOW);
        content.setStroke(Color.BLACK);

        textFlow.getChildren().add(content);

        textFlow.applyCss();
        textFlow.layout();

        box.widthProperty().bind(textFlow.widthProperty());
        box.heightProperty().bind(textFlow.heightProperty());
        textFlow.layoutXProperty().bind(box.xProperty());
        textFlow.layoutYProperty().bind(box.yProperty());

        box.setX(posX - 60);
        box.setY(posY - playerHeight);

        return new Notification(textFlow, box);
    }

    public Rectangle getBox() {
        return box;
    }

    public TextFlow getTextFlow() {
        return textFlow;
    }
}
