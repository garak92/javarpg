package rpg.Common;

import java.util.Random;

import javafx.scene.layout.Pane;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumQuestStatus;
import rpg.Monsters.Quest;

public class QuestDialogBox extends BaseDialogBox {
  private Quest quest;
  private String[] defaultDialogueList;
  private BaseMonster questGiver;
  private double yOffset = 0;

  public QuestDialogBox(Quest quest, Pane pane, BaseMonster questGiver, String[] defaultDialogueList) {
    super(pane);
    this.quest = quest;
    this.questGiver = questGiver;
    this.defaultDialogueList = defaultDialogueList;

    yOffset = box.getHeight() + 50;
    text.setText(questGiver.getName().toUpperCase() + ": " + this.quest.getCurrentText());
    updateLayout(questGiver.getCharPosx(), yOffset);

  }

  private void setDialogueText() {
    if (this.quest.getQuestStatus() != EnumQuestStatus.DELIVERED) {
      text.setText(questGiver.getName().toUpperCase() + ": " + this.quest.getCurrentText());
      quest.deliverQuest(questGiver.getLevel().getPlayer());
    } else {
      int randomNumber = new Random().nextInt(defaultDialogueList.length);
      text.setText(defaultDialogueList[randomNumber]);
    }

    updateLayout(questGiver.getCharPosx(), questGiver.getCharPosy() - (yOffset));
  }

  @Override
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
}
