package rpg.Common;

import rpg.Game;
import rpg.Levels.Level;

public class LevelLoader {
  private LevelLoader() {
    throw new IllegalAccessError("Can't instantiate static class");
  }

  public static void loadLevel(Level newLevel) {
    try {
      Game.getInstance().setCurrentLevel(newLevel);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
