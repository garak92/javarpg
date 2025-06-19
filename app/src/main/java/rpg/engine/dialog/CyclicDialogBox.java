package rpg.engine.dialog;

import javafx.scene.layout.Pane;
import rpg.engine.monster.BaseMonster;

import java.util.List;

public class CyclicDialogBox extends BaseDialogBox {
    private final List<String> dialogues;
    private final double posX;
    private final double posY;
    private final BaseMonster monster;
    private int currentIndex = 0;

    public CyclicDialogBox(List<String> dialogues, Pane pane, BaseMonster monster) {
        super(pane);
        this.monster = monster;
        this.dialogues = dialogues;
        this.posX = monster.getCharPosx();
        this.posY = monster.getCharPosy();

        text.setText(dialogues.get(currentIndex));
        updateLayout(monster.getCharPosx(), monster.getCharPosy());
    }

    @Override
    public void use() {
        currentIndex = (currentIndex + 1) % dialogues.size();
        text.setText(dialogues.get(currentIndex));

        updateLayout(monster.getCharPosx(), monster.getCharPosy());

        if (open) {
            close();
        } else {
            open();
        }
    }
}
