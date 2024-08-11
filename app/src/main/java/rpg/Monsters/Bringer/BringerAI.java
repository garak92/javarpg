package rpg.Monsters.Bringer;

import rpg.Monsters.BaseEnemyAI;
import rpg.Monsters.BaseMonster;

public class BringerAI extends BaseEnemyAI {
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 40;

  public BringerAI(BaseMonster monster) {
    super(monster);
    attackRange = 600;
  }

  @Override
  public void attack(BaseMonster target) {
    if (randomAttackAccumulator == attackCoolDown) {
      System.out.println("Bringer of death casts special attack FIREBALL!! ");
      monster.getLevel()
          .addThing(new BringerFireball(monster.getCharPosx(),
              monster.getCharPosy(), monster.getLevel(), target.getCharPosx(), target.getCharPosy()));

      randomAttackAccumulator = 0;
    } else {
      randomAttackAccumulator++;
    }
  }
}
