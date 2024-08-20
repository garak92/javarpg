package rpg.Common;

import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import rpg.Monsters.BaseMonster;

public class CyclicDialogBox {
  private Rectangle box = new Rectangle();
  private Text text = new Text();
  private Pane pane;
  private boolean open = false;
  private String[] defaultDialogueList;
  private BaseMonster subject;

  public CyclicDialogBox(Pane pane, BaseMonster subject, String[] defaultDialogueList) {
    this.pane = pane;
    this.defaultDialogueList = defaultDialogueList;
    this.subject = subject;

    text.setFill(Color.WHITE);
    text.setFont(Font.font("Calibri", FontWeight.BOLD, FontPosture.REGULAR, 12));
    text.setX(subject.getCharPosx());
    text.setY(subject.getCharPosy() + 5);
    text.setText(defaultDialogueList[0]);

    box = new Rectangle(subject.getCharPosx() - 10, subject.getCharPosx() - 10,
        text.getLayoutBounds().getWidth() + 20,
        text.getLayoutBounds().getHeight() + 10);
    box.setFill(Color.BLACK);
  }

  public void setDialogueText() {
    int randomNumber = new Random().nextInt(0, defaultDialogueList.length);
    text.setX(subject.getCharPosx());
    text.setY(subject.getCharPosy() + 5);
    box.setX(subject.getCharPosx() - 10);
    box.setY(subject.getCharPosy() - 10);
    text.setText(defaultDialogueList[randomNumber]);
    box.setWidth(text.getLayoutBounds().getWidth() + 20);
    box.setHeight(text.getLayoutBounds().getHeight() + 10);
  }

  public void use() {
    setDialogueText();
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

  public boolean getState() {
    return open;
  }
}
