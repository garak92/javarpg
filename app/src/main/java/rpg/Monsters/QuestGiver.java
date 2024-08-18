package rpg.Monsters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpg.Common.QuestDialogBox;
import rpg.Common.QuestLog;

public class QuestGiver {
  private QuestDialogBox dialogBox;
  protected static Logger logger = LoggerFactory.getLogger(QuestGiver.class);

  public QuestGiver(BaseMonster monster, String[] defaultDialogueList) {
    this.dialogBox = new QuestDialogBox(
        QuestLog.INSTANCE.getNextQuestForThisGiver(monster),
        monster.getLevel().getPane(), monster, defaultDialogueList);
  }

  public void use(Player player) {
    dialogBox.use();
  }
}
