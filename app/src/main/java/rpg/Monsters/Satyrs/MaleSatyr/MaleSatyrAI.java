package rpg.Monsters.Satyrs.MaleSatyr;

import rpg.Monsters.*;

public class MaleSatyrAI extends BaseEnemyAI {
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 40;

  public MaleSatyrAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 1000;
  }

  @Override
  public void attack() {
    if (randomAttackAccumulator == attackCoolDown) {
      super.transition(EnumEvents.CAN_ATTACK);
      logger.info("Bringer of death casts special attack FIREBALL!! ");
      monster.getLevel()
          .addThing(new MaleSatyrFireball(monster.getImageView().getBoundsInParent().getCenterX(),
              monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance()));
      randomAttackAccumulator = 0;
    } else {
      randomAttackAccumulator++;
    }
  }

  @Override
  public EnumEnemyStates currentState() {
    return super.getCurrentState();
  }
}
