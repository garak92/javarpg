package rpg.Items;

import rpg.Monsters.BaseMonster;

public abstract class BaseItem extends Item {
  protected int boostValue;

  public BaseItem(int boostValue, BaseMonster monster) {
    super(monster);
    this.boostValue = boostValue;
  }

  protected abstract void useItem();
}
