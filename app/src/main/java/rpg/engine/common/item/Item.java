package rpg.engine.common.item;

import rpg.engine.monster.BaseMonster;

abstract class Item {
  protected BaseMonster monster;

  public Item(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update();
}
