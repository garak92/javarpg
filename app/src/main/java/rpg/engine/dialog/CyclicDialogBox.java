package rpg.engine.dialog;

import javafx.scene.layout.Pane;
import rpg.engine.monster.BaseMonster;

import java.util.List;
import java.util.SplittableRandom;

public class CyclicDialogBox extends BaseDialogBox {
    private final List<String> dialogues;
    private final double posX;
    private final double posY;
    private final BaseMonster monster;
    private int currentIndex = 0;
    private final SplittableRandom rngGenerator = new SplittableRandom();

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
        if (open) {
            close();
        } else {
            currentIndex = (rngGenerator.nextInt(0, dialogues.size())) % dialogues.size();
            text.setText(dialogues.get(currentIndex));
            updateLayout(monster.getCharPosx(), monster.getCharPosy());
            open();
        }
    }

    @Override
    public void stopUsing() {
        close();
    }
}
