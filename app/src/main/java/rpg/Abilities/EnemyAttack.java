package rpg.Abilities;

import java.util.List;

import rpg.Common.Usable;
import rpg.Monsters.BaseMonster;

public abstract class EnemyAttack {
  protected BaseMonster monster;

  public EnemyAttack(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update(List<Usable> usables);
}
