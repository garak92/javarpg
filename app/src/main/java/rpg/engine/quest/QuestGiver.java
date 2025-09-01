package rpg.engine.quest;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.dialog.QuestDialogBox;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class QuestGiver {
    protected static Logger logger = LoggerFactory.getLogger(QuestGiver.class);
    private final QuestDialogBox dialogBox;
    private final BaseMonster monster;
    private final Text exclamation;
    private Quest quest;

    public QuestGiver(BaseMonster monster, String[] defaultDialogueList) {
        this.quest = QuestLog.INSTANCE.getNextQuestForThisGiver(monster);
        this.monster = monster;
        this.exclamation = new Text(monster.getCharPosx() + 50, monster.getCharPosy() + 30, "!");
        this.exclamation.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        this.exclamation.setFill(Color.YELLOW);
        this.dialogBox = new QuestDialogBox(quest, monster.getLevel().getPane(), monster, defaultDialogueList);

        if (this.quest != null) {
            monster.getLevel().getPane().getChildren().add(exclamation);
        }
    }

    public void use(Player player) {
        dialogBox.use();
        this.quest = QuestLog.INSTANCE.getNextQuestForThisGiver(monster);
        showExclamation();
    }

    public void stopUsing(Player player) {
        dialogBox.stopUsing();
        showExclamation();
    }

    public void showExclamation() {
        if (this.quest == null || this.dialogBox.isOpen() || QuestLog.INSTANCE.hasQuestsInProgress(monster)) {
            monster.getLevel().getPane().getChildren().remove(exclamation);
        } else if (!monster.getLevel().getPane().getChildren().contains(this.exclamation)) {
            monster.getLevel().getPane().getChildren().add(exclamation);
        }
    }
}
