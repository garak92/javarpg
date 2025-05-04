package rpg.engine.ai;

import java.util.List;

import rpg.engine.common.Usable;
import rpg.engine.monster.BaseMonster;

public abstract class EnemyAI {
  protected BaseMonster monster;

  public EnemyAI(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update(List<Usable> usables);
  public abstract EnumEnemyStates currentState();
  public abstract boolean isAttacking();
  public abstract boolean isPerformingAction();
  public abstract void setIsPerformingAction(boolean isPerformingAction);
  public abstract void transition(EnumEvents monsterEvent);
}
