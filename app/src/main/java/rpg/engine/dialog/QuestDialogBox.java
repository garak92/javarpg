package rpg.engine.dialog;

import javafx.scene.layout.Pane;
import rpg.engine.monster.BaseMonster;
import rpg.engine.quest.EnumQuestStatus;
import rpg.engine.quest.Quest;
import rpg.engine.quest.QuestLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestDialogBox extends BaseDialogBox {
    private final String[] defaultDialogueList;
    private final BaseMonster questGiver;
    private Quest quest;

    // pagination
    private List<String> pages = new ArrayList<>();
    private int currentPageIndex = 0;

    public QuestDialogBox(Quest quest, Pane pane, BaseMonster questGiver, String[] defaultDialogueList) {
        super(pane);
        this.quest = quest;
        this.questGiver = questGiver;
        this.defaultDialogueList = defaultDialogueList;

        paginate(getInitialText());
        showCurrentPage();

        updateLayout(questGiver.getCharPosx(), questGiver.getCharPosy());
    }

    private String getInitialText() {
        return questGiver.getName().toUpperCase() + ": " + this.quest.getCurrentText();
    }

    private void paginate(String textToPaginate) {
        pages.clear();
        currentPageIndex = 0;
        int maxLen = 400;

        for (int i = 0; i < textToPaginate.length(); i += maxLen) {
            pages.add(textToPaginate.substring(i, Math.min(i + maxLen, textToPaginate.length())));
        }
    }

    private void showCurrentPage() {
        if (!pages.isEmpty() && currentPageIndex < pages.size()) {
            text.setText(pages.get(currentPageIndex));
            updateLayout(questGiver.getCharPosx(), questGiver.getCharPosy());
        }
    }

    private void setDialogueText() {
        String dialogText;
        if (this.quest.getQuestStatus() != EnumQuestStatus.DELIVERED) {
            dialogText = questGiver.getName().toUpperCase() + ": " + this.quest.getCurrentText();
        } else {
            int randomNumber = new Random().nextInt(defaultDialogueList.length);
            dialogText = defaultDialogueList[randomNumber];
        }

        paginate(dialogText);
        showCurrentPage();

        updateLayout(questGiver.getCharPosx(), questGiver.getCharPosy());
    }

    @Override
    public void use() {
        if (open && currentPageIndex < pages.size() - 1) {
            // still more pages left
            currentPageIndex++;
            showCurrentPage();
            return;
       }

        // reached the end of pagination OR not open yet
        setDialogueText();
        if (open) {
            close();
            if (this.quest.getQuestStatus() != EnumQuestStatus.DELIVERED) {
                quest.deliverQuest(questGiver.getLevel().getPlayer());
            }
            quest.acceptQuest();

            if (this.quest.getQuestStatus() == EnumQuestStatus.DELIVERED
                    && QuestLog.INSTANCE.getNextQuestForThisGiver(questGiver) != null) {
                this.quest = QuestLog.INSTANCE.getNextQuestForThisGiver(questGiver);
                paginate(this.quest.getCurrentText());
                showCurrentPage();
            }
        } else {
            open();
        }
    }

    @Override
    public void stopUsing() {
        if (open && currentPageIndex < pages.size() - 1) {
            // still more pages left
            currentPageIndex = 0;
            showCurrentPage();
            close();
            return;
        }

        // reached the end of pagination OR not open yet
        setDialogueText();
        if (open) {
            close();
            if (this.quest.getQuestStatus() != EnumQuestStatus.DELIVERED) {
                quest.deliverQuest(questGiver.getLevel().getPlayer());
            }
            quest.acceptQuest();

            if (this.quest.getQuestStatus() == EnumQuestStatus.DELIVERED
                    && QuestLog.INSTANCE.getNextQuestForThisGiver(questGiver) != null) {
                this.quest = QuestLog.INSTANCE.getNextQuestForThisGiver(questGiver);
                paginate(this.quest.getCurrentText());
                showCurrentPage();
            }
        }
    }
}
