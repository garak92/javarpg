package rpg.Common;

import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import rpg.Monsters.Player;
import rpg.Monsters.Quest;

public class PlayerStatusBar {
  private Rectangle box = new Rectangle();
  private Text text = new Text();
  private Player player;
  private final String NEWLINE = "\n";

  public PlayerStatusBar(double posX, double posY, Pane pane, Player player) {
    this.player = player;

    text.setFill(Color.BLACK);
    text.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 12));
    text.setX(posX);
    text.setY(posY);
    text.setText("Health: " + player.getMonster().getHealth());
    text.setText("Experience Points: " + player.getExperiencePoints());

    box = new Rectangle(posX, posY,
        text.getLayoutBounds().getWidth() + 20,
        text.getLayoutBounds().getHeight() + 10);
    box.setFill(Color.WHITE);

    pane.getChildren().addAll(box, text);

  }

  public String generateStatusText() {
    List<Quest> questLogActive = QuestLog.INSTANCE.getCurrentQuests();
    StringBuilder sb = new StringBuilder();
    sb.append("Health: " + player.getMonster().getHealth());
    sb.append(NEWLINE);
    sb.append("Experience points: " + player.getExperiencePoints());
    sb.append(NEWLINE);
    sb.append("Quest log: ");
    sb.append(NEWLINE);
    for (Quest i : questLogActive) {
      System.out.println(questLogActive.size());
      if (!i.equals(null)) {
        sb.append("===========");
        sb.append(NEWLINE);
        sb.append("Quest name: " + i.getName());
        sb.append(NEWLINE);
        sb.append("Status: " + i.getQuestStatus());
        sb.append(NEWLINE);
        sb.append("Objective: " + i.getQuestObjective() + " " + i.getEntityFriendlyName());
        sb.append(NEWLINE);
        sb.append("Progress: " + i.getEntityCounter() + " out of " + i.getQuestObjective() + " ");
        sb.append(NEWLINE);
        sb.append("===========");
      }
    }

    return sb.toString();
  }

  public void update(double posX, double posY) {
    text.setText(generateStatusText());
    box.setLayoutX(posX);
    box.setLayoutY(posY);
    text.setX(posX);
    text.setY(posY + 10);
    box.setWidth(text.getLayoutBounds().getWidth() + 20);
    box.setHeight(text.getLayoutBounds().getHeight() + 10);
  }

}
