package rpg.Common;

import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumQuestStatus;
import rpg.Monsters.Quest;

public class QuestDialogBox {
  private Rectangle box = new Rectangle();
  private Text text = new Text();
  private Pane pane;
  private boolean open = false;
  private Quest quest = null;
  private String[] defaultDialogueList;
  private BaseMonster questGiver;

  public QuestDialogBox(Quest quest, Pane pane, BaseMonster questGiver, String[] defaultDialogueList) {
    this.pane = pane;
    this.quest = quest;
    this.defaultDialogueList = defaultDialogueList;
    this.questGiver = questGiver;

    text.setFill(Color.WHITE);
    text.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 12));
    text.setX(questGiver.getCharPosx());
    text.setY(questGiver.getCharPosy() + 5);
    text.setText(this.quest.getCurrentText());

    box = new Rectangle(questGiver.getCharPosx() - 10, questGiver.getCharPosy() - 10,
        text.getLayoutBounds().getWidth() + 20,
        text.getLayoutBounds().getHeight() + 10);
    box.setFill(Color.BLACK);
  }

  public void setDialogueText() {
    if (this.quest.getQuestStatus() != EnumQuestStatus.DELIVERED) {
      text.setText(this.quest.getCurrentText());
      quest.deliverQuest(questGiver.getLevel().getPlayer());
    } else {
      int randomNumber = new Random().nextInt(0, defaultDialogueList.length);
      text.setText(defaultDialogueList[randomNumber]);
    }
    box.setWidth(text.getLayoutBounds().getWidth() + 20);
    box.setHeight(text.getLayoutBounds().getHeight() + 10);

  }

  public void use() {
    setDialogueText();
    if (open) {
      close();
      quest.acceptQuest();
      if (this.quest.getQuestStatus() == EnumQuestStatus.DELIVERED
          && QuestLog.INSTANCE.getNextQuestForThisGiver(questGiver) != null) {
        this.quest = QuestLog.INSTANCE.getNextQuestForThisGiver(questGiver);
        text.setText(this.quest.getCurrentText());
      }
    } else {
      open();
    }
  }

  public void open() {
    pane.getChildren().addAll(box, text);
    open = true;
  }

  private void close() {
    pane.getChildren().removeAll(box, text);
    open = false;
  }

  public boolean isOpen() {
    return open;
  }
}
