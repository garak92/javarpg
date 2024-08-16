package rpg.Monsters;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpg.Common.QuestDialogBox;

public class QuestGiver {
  private BaseMonster target;
  private BaseMonster monster;
  private List<Quest> questList = new ArrayList<>();
  private QuestDialogBox dialgoBox;
  protected static Logger logger = LoggerFactory.getLogger(QuestGiver.class);

  public QuestGiver(BaseMonster monster, List<Quest> questList) {
    this.monster = monster;
    this.questList = questList;
    this.dialgoBox = new QuestDialogBox(questList.get(0).getCurrentDialog(),
        monster.getLevel().getPane(), monster);
  }

  public void use(Player player) {
    dialgoBox.use();
  }
}
