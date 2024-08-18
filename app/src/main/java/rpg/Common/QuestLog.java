package rpg.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumQuestStatus;
import rpg.Monsters.Quest;

public enum QuestLog {
  INSTANCE;

  private final List<Quest> quests;
  protected static Logger logger = LoggerFactory.getLogger(QuestLog.class);

  QuestLog() {
    quests = new ArrayList<>();
  }

  public void addQuest(Quest quest) {
    quests.add(quest);
  }

  public void completeQuest(Quest quest) {
    quests.remove(quest);
  }

  public <T extends BaseMonster> Quest getNextQuestForThisGiver(T questGiver) {
    Quest res = null;

    List<Quest> list = quests.stream().filter(v -> {
      return v.getQuestStatus() == EnumQuestStatus.AVAILABLE && v.getQuestGiver() == questGiver.getClass();
    }).collect(Collectors.toList());
    if (!list.isEmpty()) {
      res = list.get(0);
    }

    return res;
  }

  public List<Quest> getQuests() {
    return new ArrayList<>(quests);
  }

  public <T extends BaseMonster> void updateActiveQuests(T questEntity) {
    for (Quest quest : quests) {
      quest.update(questEntity);
    }
  }

  public void displayQuests() {
    if (quests.isEmpty()) {
      logger.info("No quest exist in the quests file.");
    } else {
      logger.info("Active Quests:");
      for (Quest quest : quests) {
        if (quest.isInProgress()) {
          logger.info("Name: " + quest.getName());
          logger.info("Status: " + quest.getQuestStatus());
          logger.info("Done: " + quest.getEntityCounter());
          logger.info("Objective: " + quest.getQuestObjective());
        } else {
          logger.info("No active quests.");
        }
      }
    }
  }
}
