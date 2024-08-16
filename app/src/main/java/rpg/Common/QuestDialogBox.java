package rpg.Common;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import rpg.Monsters.BaseMonster;

public class QuestDialogBox {
  private Rectangle box = new Rectangle();
  private Text text = new Text();
  private Pane pane;
  private boolean open = false;

  public QuestDialogBox(String dialogText, Pane pane, BaseMonster questGiver) {
    this.pane = pane;

    text.setFill(Color.WHITE);
    text.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 12));
    text.setX(questGiver.getCharPosx());
    text.setY(questGiver.getCharPosy() + 5);
    text.setText(dialogText);

    box = new Rectangle(questGiver.getCharPosx() - 10, questGiver.getCharPosy() - 10,
        text.getLayoutBounds().getWidth() + 20,
        text.getLayoutBounds().getHeight() + 10);
    box.setFill(Color.BLACK);
  }

  public void use() {
    if (open) {
      close();
    } else {
      open();
    }

  }

  public void open() {
    pane.getChildren().addAll(box, text);
    open = true;
  }

  private void close() {
    pane.getChildren().removeAll(box, text);
    open = false;
  }

  private void setText(String dialogText) {
    this.text.setText(dialogText);
    this.box.setHeight(this.text.getBoundsInParent().getHeight());
    this.box.setWidth(this.text.getBoundsInParent().getWidth());
  }

}
