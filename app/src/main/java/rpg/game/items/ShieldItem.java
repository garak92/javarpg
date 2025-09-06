package rpg.game.items;

import rpg.engine.common.item.BaseItem;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class ShieldItem extends BaseItem {
    private Player target = null;

    public ShieldItem(BaseMonster monster) {
        super(50, monster);
    }

    @Override
    protected void useItem() {
        if(target == null) {
            target = Player.getInstance();
            return;
        }

        if (monster.getMonster().detectCollision(target)) {
            target.setShield(boostValue);
            monster.die();
        }
    }

    @Override
    public void update() {
        useItem();
    }
}
