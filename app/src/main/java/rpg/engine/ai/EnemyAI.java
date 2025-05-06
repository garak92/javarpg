package rpg.engine.ai;

import rpg.engine.common.Usable;
import rpg.engine.monster.BaseMonster;

import java.util.List;

public abstract class EnemyAI {
  protected BaseMonster monster;

  public EnemyAI(BaseMonster monster) {
    this.monster = monster;
  }

  public abstract void update(List<Usable> usables);
  public abstract EnumEnemyStates currentState();
  public abstract void setIsPerformingAction(boolean isPerformingAction);
  public abstract void transition(EnumEvents monsterEvent);
}
