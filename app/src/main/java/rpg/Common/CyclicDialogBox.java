package rpg.Common;

import javafx.scene.layout.Pane;

import java.util.List;

public class CyclicDialogBox extends BaseDialogBox {
  private List<String> dialogues;
  private int currentIndex = 0;
  private double posX, posY;

  public CyclicDialogBox(List<String> dialogues, Pane pane, double x, double y) {
    super(pane);
    this.dialogues = dialogues;
    this.posX = x;
    this.posY = y;

    text.setText(dialogues.get(currentIndex));
    updateLayout(posX, posY);
  }

  @Override
  public void use() {
    currentIndex = (currentIndex + 1) % dialogues.size();
    text.setText(dialogues.get(currentIndex));
    updateLayout(posX, posY);

    if (open) {
      close();
    } else {
      open();
    }
  }
}
