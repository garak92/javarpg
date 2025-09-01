package rpg.engine.common;

import rpg.engine.levels.EntityNode;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public interface Usable {
    void use(Player player);

    EntityNode getLevelNode();

    BaseMonster getBaseMonster();

    void stopUsing(Player player);
}
