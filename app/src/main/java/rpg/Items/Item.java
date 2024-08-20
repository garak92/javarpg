package rpg.Items;

import rpg.Monsters.BaseMonster;

public abstract class Item {
  protected BaseMonster monster;

  public Item(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update();
}
