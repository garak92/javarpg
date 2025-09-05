package rpg.game.items;

import rpg.engine.common.item.BaseItem;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class MedKitItem extends BaseItem {
    private Player target = null;

    public MedKitItem(BaseMonster monster) {
        super(30, monster);
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
