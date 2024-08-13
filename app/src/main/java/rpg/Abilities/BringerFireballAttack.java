package rpg.Abilities;

import rpg.Monsters.BaseMonster;

public class BringerFireballAttack extends BaseAbility {
  private double normalizedX = 0;
  private double normalizedY = 0;
  private BaseMonster target;

  public BringerFireballAttack(BaseMonster monster, BaseMonster target) {
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
    if (monster.getMonster().detectCollision(monster.getLevel().getSolidTiles())) {
      monster.die();
    }

    monster.setCharPosx(monster.getCharPosx() + normalizedX * monster.getCharVelx());
    monster.setCharPosy(monster.getCharPosy() + normalizedY * monster.getCharVely());

    monster.getImageView().setLayoutX(monster.getCharPosx());
    monster.getImageView().setLayoutY(monster.getCharPosy());
  }
}
