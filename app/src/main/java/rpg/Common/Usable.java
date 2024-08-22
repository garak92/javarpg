package rpg.Common;

import rpg.Levels.LevelNode;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.Player;

public interface Usable {
  public void use(Player player);

  public LevelNode getLevelNode();

  public BaseMonster getBaseMonster();
}
