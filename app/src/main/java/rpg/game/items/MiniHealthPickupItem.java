package rpg.game.items;

import rpg.engine.common.item.BaseItem;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class MiniHealthPickupItem extends BaseItem {
  private Player target;

  public MiniHealthPickupItem(BaseMonster monster, Player target) {
    super(3, monster);
    this.target = target;
  }

  @Override
  protected void useItem() {
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
