package rpg.engine.common.misc;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import rpg.engine.quest.Quest;
import rpg.engine.quest.QuestLog;
import rpg.game.entities.player.Player;

import java.util.List;
import java.util.Objects;

public class PlayerStatusBar {
  private final Rectangle box = new Rectangle();
  private final TextFlow textFlow = new TextFlow();
  private final Player player;
  private final Pane pane;

  public PlayerStatusBar(double posX, double posY, Pane pane, Player player) {
    this.player = player;
    this.pane = pane;

    textFlow.setLayoutX(posX);
    textFlow.setLayoutY(posY);
    textFlow.setLineSpacing(2);
    textFlow.setTextAlignment(TextAlignment.CENTER);
    textFlow.setPrefWidth(300);

    box.setX(posX - 10);
    box.setY(posY - 10);

    Image bgImage = new Image(Objects.requireNonNull(getClass().getResource("/sprites/other/container.png")).toExternalForm());
    box.setFill(new ImagePattern(bgImage, 0, 0, 1, 1, true));

    box.toFront();
    textFlow.toFront();

    pane.getChildren().addAll(box, textFlow);

    update(posX, posY);
  }

  private String generateStatusText() {
    List<Quest> questLogActive = QuestLog.INSTANCE.getCurrentQuests();
    StringBuilder sb = new StringBuilder();
    sb.append("Health: ").append(player.getMonster().getHealth()).append("\n");
    sb.append("Experience points: ").append(player.getExperiencePoints()).append("\n");
    sb.append("Quest log:\n");

    for (Quest q : questLogActive) {
      if (q != null) {
        sb.append("===========\n");
        sb.append("Quest name: ").append(q.getName()).append("\n");
        sb.append("Status: ").append(q.getQuestStatus()).append("\n");
        sb.append("Objective: ").append(q.getQuestObjective()).append(" ").append(q.getEntityFriendlyName()).append("\n");
        sb.append("Progress: ").append(q.getEntityCounter()).append(" out of ").append(q.getQuestObjective()).append("\n");
        sb.append("===========\n");
      }
    }
    return sb.toString();
  }

  public void update(double posX, double posY) {
    textFlow.getChildren().clear();

    Text content = new Text(generateStatusText());
    content.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 14));
    content.setFill(javafx.scene.paint.Color.BLACK);

    textFlow.getChildren().add(content);

    textFlow.setLayoutX(posX);
    textFlow.setLayoutY(posY);
    box.setX(posX - 10);
    box.setY(posY - 10);

    // Defer size measurement until layout is complete
    Platform.runLater(() -> {
      textFlow.applyCss();
      textFlow.layout();

      double width = textFlow.getBoundsInLocal().getWidth() + 20;
      double height = textFlow.getBoundsInLocal().getHeight() + 20;

      box.setWidth(width);
      box.setHeight(height);
    });
  }
}
