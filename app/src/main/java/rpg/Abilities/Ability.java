package rpg.Abilities;

import rpg.Monsters.BaseMonster;

public abstract class Ability {
  protected BaseMonster monster;

  public Ability(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update();
}
