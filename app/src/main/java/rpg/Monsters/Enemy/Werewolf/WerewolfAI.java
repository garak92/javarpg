package rpg.Monsters.Enemy.Werewolf;

import rpg.Monsters.*;

public class WerewolfAI extends BaseEnemyAI {
  double targetPosX = Player.getInstance().getCharPosx();
  double targetPosY = Player.getInstance().getCharPosy();
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 10;

  public WerewolfAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 1000;
  }

  @Override
  public void attack() {
    if(!monster.detectCollision(Player.getInstance())) {
      MonsterUtils.jumpToDirection(monster, targetPosX, targetPosY, 25);
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
