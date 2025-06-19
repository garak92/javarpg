package rpg.engine.quest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.monster.BaseMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum QuestLog {
    INSTANCE;

    private static Logger logger = LoggerFactory.getLogger(QuestLog.class);
    private final List<Quest> quests;

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
        for (Quest quest : quests) {
            if (quest.isInProgress() && quest.getQuestGiver() == questGiver.getClass()) {
                return quest;
            }
        }

        List<Quest> list = quests.stream().filter(v -> {
            return v.getQuestStatus() == EnumQuestStatus.AVAILABLE && v.getQuestGiver() == questGiver.getClass();
        }).collect(Collectors.toList());
        if (!list.isEmpty()) {
            res = list.get(0);
        }

        return res;
    }

    public <T extends BaseMonster> boolean hasQuestsInProgress(T questGiver) {
        List<Quest> list = quests.stream().filter(v -> {
            return (v.getQuestStatus() == EnumQuestStatus.IN_PROGRESS || v.getQuestStatus() == EnumQuestStatus.COMPLETED)
                    && v.getQuestGiver() == questGiver.getClass();
        }).collect(Collectors.toList());

        return !list.isEmpty();
    }

    public List<Quest> getQuests() {
        return new ArrayList<>(quests);
    }

    public List<Quest> getCurrentQuests() {
        return quests.stream().filter(v -> {
            return v.getQuestStatus() != EnumQuestStatus.AVAILABLE && v.getQuestStatus() != EnumQuestStatus.DELIVERED;
        }).collect(Collectors.toList());
    }

    public <T extends BaseMonster> void updateActiveQuests(T questEntity) {
        for (Quest quest : quests) {
            if (quest.isInProgress()) {
                quest.update(questEntity);
            }
        }
    }

    public void displayQuests() {
        if (quests.isEmpty()) {
            logger.info("No quest exist in the quests file.");
        } else {
            logger.info("Active Quests:");
            for (Quest quest : quests) {
                if (quest.getQuestStatus() != EnumQuestStatus.AVAILABLE) {
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
