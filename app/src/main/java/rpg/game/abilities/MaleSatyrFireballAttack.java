package rpg.game.abilities;

import rpg.engine.common.ability.BaseAbility;
import rpg.engine.monster.BaseMonster;

public class MaleSatyrFireballAttack extends BaseAbility {
  private double normalizedX = 0;
  private double normalizedY = 0;
  private final BaseMonster target;

  public MaleSatyrFireballAttack(BaseMonster monster, BaseMonster target) {
    super(15, 0, 0, monster);
    double directionX = target.getCharPosx() - monster.getCharPosx();
    double directionY = target.getCharPosy() - monster.getCharPosy();

    double length = Math.sqrt(directionX * directionX + directionY * directionY);
    normalizedX = directionX / length;
    normalizedY = directionY / length;
    this.target = target;
  }

  @Override
  protected void dealDamage() {
    if (monster.getMonster().detectCollision(target)) {
      target.receiveDamage(damageDealt);
      monster.die();
    }
  }

  @Override
  public void update() {
    dealDamage();
    if (monster.getMonster().detectCollision(monster.getLevel().getSolidTiles(), monster.getCharPosx(),
            monster.getCharPosy())) {
      monster.die();
    }

    monster.setCharPosx(monster.getCharPosx() + normalizedX * monster.getVelocity());
    monster.setCharPosy(monster.getCharPosy() + normalizedY * monster.getVelocity());
  }
}
