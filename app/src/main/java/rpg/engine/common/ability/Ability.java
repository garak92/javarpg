package rpg.engine.common.ability;

import rpg.engine.monster.BaseMonster;

public abstract class Ability {
  protected BaseMonster monster;

  public Ability(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update();
}
