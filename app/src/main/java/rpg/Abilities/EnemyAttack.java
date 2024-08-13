package rpg.Abilities;

import rpg.Monsters.BaseMonster;

public abstract class EnemyAttack {
  protected BaseMonster monster;

  public EnemyAttack(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update();
}
