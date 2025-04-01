package rpg.Monsters.Enemy.MaleSatyr;

import rpg.Monsters.*;

public class MaleSatyrAI extends BaseEnemyAI {
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 20;

  public MaleSatyrAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 1000;
  }

  @Override
  public void attack() {
    if (randomAttackAccumulator == attackCoolDown) {
      transition(EnumEvents.CAST_ATTACK);
      monster.getLevel()
          .addThing(new MaleSatyrFireball(monster.getImageView().getBoundsInParent().getCenterX(),
              monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance()));
      randomAttackAccumulator = 0;
    } else {
      transition(EnumEvents.FINISH_ATTACK);
      randomAttackAccumulator++;
    }
  }

  @Override
  public EnumEnemyStates currentState() {
    return super.getCurrentState();
  }
}
