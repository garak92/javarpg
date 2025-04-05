package rpg.Monsters.Enemy.Gorgon;

import rpg.Monsters.*;

public class GorgonAI extends BaseEnemyAI {
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 40;
  private final int attackPositionOffset = 70;

  public GorgonAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 1000;
  }

  @Override
  public void attack() {
    if (randomAttackAccumulator == attackCoolDown) {
      transition(EnumEvents.CAST_ATTACK);
      monster.getLevel()
          .addThing(new GorgonWaterball(monster.getImageView().getBoundsInParent().getCenterX(),
              monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance(),
                  Player.getInstance().getCharPosx(), Player.getInstance().getCharPosy() + attackPositionOffset));

      monster.getLevel()
              .addThing(new GorgonWaterball(monster.getImageView().getBoundsInParent().getCenterX(),
                      monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance(),
                      Player.getInstance().getCharPosx(), Player.getInstance().getCharPosy()));

      monster.getLevel()
              .addThing(new GorgonWaterball(monster.getImageView().getBoundsInParent().getCenterX(),
                      monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance(),
                      Player.getInstance().getCharPosx(),
                      Player.getInstance().getCharPosy() - attackPositionOffset));
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
