package rpg.engine.common;

import rpg.engine.levels.LevelNode;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public interface Usable {
  public void use(Player player);

  public LevelNode getLevelNode();

  public BaseMonster getBaseMonster();
}
