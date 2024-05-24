package rpg.Common;

import rpg.Levels.LevelNode;
import rpg.Monsters.Player;

public interface Usable {
  public void use(Player player);

  public LevelNode getLevelNode();
}
