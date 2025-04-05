package rpg.Abilities;

import rpg.Monsters.BaseMonster;

public class GorgonWaterballAttack extends BaseAbility {
  private double normalizedX = 0;
  private double normalizedY = 0;
  private BaseMonster target;

  public GorgonWaterballAttack(BaseMonster monster, BaseMonster target, double targetPosX, double targetPosY) {
    super(15, 0, 0, monster);
    this.target = target;

    double directionX = targetPosX - monster.getCharPosx();
    double directionY = targetPosY - monster.getCharPosy();

    double length = Math.sqrt(directionX * directionX + directionY * directionY);
    normalizedX = directionX / length;
    normalizedY = directionY / length;
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
    if (monster.getMonster().detectCollision(monster.getLevel().getSolidTiles())) {
      monster.die();
    }

    monster.setCharPosx(monster.getCharPosx() + normalizedX * monster.getVelocity());
    monster.setCharPosy(monster.getCharPosy() + normalizedY * monster.getVelocity());

    monster.getImageView().setLayoutX(monster.getCharPosx());
    monster.getImageView().setLayoutY(monster.getCharPosy());
  }
}
