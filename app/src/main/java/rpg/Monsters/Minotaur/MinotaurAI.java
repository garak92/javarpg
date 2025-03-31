package rpg.Monsters.Minotaur;

import rpg.Monsters.*;

public class MinotaurAI extends BaseEnemyAI {
  double targetPosX = Player.getInstance().getCharPosx();
  double targetPosY = Player.getInstance().getCharPosy();
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 20;

  public MinotaurAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 1000;
  }

  @Override
  public void attack() {
    if (!monster.detectCollision(Player.getInstance())) {
      MonsterUtils.jumpToDirection(monster, targetPosX, targetPosY, 22);
    }
    if (randomAttackAccumulator == attackCoolDown) {
      transition(EnumEvents.CAST_ATTACK);
      if (monster.detectCollision(Player.getInstance())) {
        Player.getInstance().receiveDamage(10);
      }
      randomAttackAccumulator = 0;
    } else {
      transition(EnumEvents.FINISH_ATTACK);
      targetPosX = Player.getInstance().getCharPosx();
      targetPosY = Player.getInstance().getCharPosy();
      randomAttackAccumulator++;
    }
  }

  @Override
  public EnumEnemyStates currentState() {
    return super.getCurrentState();
  }

}


