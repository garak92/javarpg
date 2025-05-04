package rpg.Monsters.Enemy.OrcBerserk;

import rpg.Monsters.*;

public class OrcBerserkAI extends BaseEnemyAI {
  double targetPosX = Player.getInstance().getCharPosx();
  double targetPosY = Player.getInstance().getCharPosy();
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 20;

  public OrcBerserkAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 370;
  }

  @Override
  public void attack() {
    if(!monster.detectCollision(Player.getInstance())) {
      MonsterUtils.jumpToDirection(monster, targetPosX, targetPosY, 0.05);
    }
    if (randomAttackAccumulator == attackCoolDown) {
      transition(EnumEvents.CAST_ATTACK);
    if (monster.detectCollision(Player.getInstance())) {
            Player.getInstance().receiveDamage(10);
    }
      randomAttackAccumulator = 0;
    }
    else {
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
