package rpg.Monsters;

import java.util.List;

import rpg.Common.Usable;

public abstract class EnemyAI {
  protected BaseMonster monster;

  public EnemyAI(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update(List<Usable> usables);
  public abstract EnumEnemyStates currentState();
}
