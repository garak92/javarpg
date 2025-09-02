package rpg.game.items;

import rpg.engine.common.item.BaseItem;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class ScrollOfAntibioticsItem extends BaseItem {
    private Player target;

    public ScrollOfAntibioticsItem(BaseMonster monster, Player target) {
        super(0, monster);
        this.target = target;
    }

    @Override
    protected void useItem() {
        if(target == null) {
            target = Player.getInstance();
            return;
        }

        if (monster.getMonster().detectCollision(target)) {
            monster.die();
        }
    }

    @Override
    public void update() {
        useItem();
    }
}
