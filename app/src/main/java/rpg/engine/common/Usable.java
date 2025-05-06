package rpg.engine.common;

import rpg.engine.levels.LevelNode;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public interface Usable {
  void use(Player player);

  LevelNode getLevelNode();

  BaseMonster getBaseMonster();
}
