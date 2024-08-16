package rpg.Monsters;

public class Quest {
  String[] dialogChain = null;
  private int currentDialogIndex = 0;

  public Quest(String[] dialogChain) {
    this.dialogChain = dialogChain;
  }

  public String getCurrentDialog() {
    if (dialogChain.equals(null)) {
      return null;
    }
    return dialogChain[currentDialogIndex];
  }

  public void retartDialog() {
    this.currentDialogIndex = 0;
  }

  public void advanceToNextDialog() {
    this.currentDialogIndex++;
  }
}
