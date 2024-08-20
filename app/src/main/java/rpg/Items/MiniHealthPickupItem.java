package rpg.Items;

import rpg.Monsters.BaseMonster;
import rpg.Monsters.Player;

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
