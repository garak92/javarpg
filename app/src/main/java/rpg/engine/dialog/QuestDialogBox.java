package rpg.engine.dialog;

import javafx.scene.layout.Pane;
import rpg.engine.monster.BaseMonster;
import rpg.engine.quest.EnumQuestStatus;
import rpg.engine.quest.Quest;
import rpg.engine.quest.QuestLog;

import java.util.Random;

public class QuestDialogBox extends BaseDialogBox {
    private final String[] defaultDialogueList;
    private final BaseMonster questGiver;
    private Quest quest;

    public QuestDialogBox(Quest quest, Pane pane, BaseMonster questGiver, String[] defaultDialogueList) {
        super(pane);
        this.quest = quest;
        this.questGiver = questGiver;
        this.defaultDialogueList = defaultDialogueList;

        text.setText(questGiver.getName().toUpperCase() + ": " + this.quest.getCurrentText());
        updateLayout(questGiver.getCharPosx(), questGiver.getCharPosy());

    }

    private void setDialogueText() {
        if (this.quest.getQuestStatus() != EnumQuestStatus.DELIVERED) {
            text.setText(questGiver.getName().toUpperCase() + ": " + this.quest.getCurrentText());
        } else {
            int randomNumber = new Random().nextInt(defaultDialogueList.length);
            text.setText(defaultDialogueList[randomNumber]);
        }

        updateLayout(questGiver.getCharPosx(), questGiver.getCharPosy());
    }

    @Override
    public void use() {
        setDialogueText();
        if (open) {
            close();
            if(this.quest.getQuestStatus() != EnumQuestStatus.DELIVERED) {
                quest.deliverQuest(questGiver.getLevel().getPlayer());
            }
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
