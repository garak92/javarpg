package rpg.game.items;

import rpg.engine.common.Game;
import rpg.engine.common.item.BaseItem;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class MiniHealthPickupItem extends BaseItem {
    private Player target = null;

    public MiniHealthPickupItem(BaseMonster monster) {
        super(3, monster);
    }

    @Override
    protected void useItem() {
        if(target == null) {
            target = Player.getInstance();
            return;
        }

        if (monster.getMonster().detectCollision(target)) {
            target.heal(boostValue);
            monster.die();
        }
    }

    @Override
    public void update() {
        useItem();
    }
}
