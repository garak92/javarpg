package rpg.engine.levels;

import rpg.engine.common.Game;

public class LevelLoader {
  private LevelLoader() {
    throw new IllegalAccessError("Can't instantiate static class");
  }

  public static void loadLevel(Level newLevel) {
    try {
      Game.getInstance().setCurrentLevel(newLevel);
      Game.getInstance().restart();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
