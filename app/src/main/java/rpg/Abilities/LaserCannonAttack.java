package rpg.Abilities;

import rpg.Monsters.BaseMonster;
import rpg.Monsters.Enemy.Props.Cannon.CannonOrientation;

public class LaserCannonAttack extends BaseAbility {
  private double normalizedX = 0;
  private double normalizedY = 0;

    public LaserCannonAttack(BaseMonster monster, CannonOrientation orientation) {
    super(15, 0, 0, monster);

    // Set direction based on the orientation
    switch (orientation) {
      case UP:
        normalizedX = 0;
        normalizedY = -1; // Move upwards
        break;
      case DOWN:
        normalizedX = 0;
        normalizedY = 1; // Move downwards
        break;
      case LEFT:
        normalizedX = -1; // Move left
        normalizedY = 0;
        break;
      case RIGHT:
        normalizedX = 1; // Move right
        normalizedY = 0;
        break;
    }
  }

  @Override
  protected void dealDamage() {
    BaseMonster hitMonster = monster.getMonster().detectCollisionWithMonsterList(monster.getLevel().getAgenticMonsters());
    if (hitMonster != null && hitMonster.getClass() != rpg.Monsters.Enemy.Props.Cannon.LaserCannon.class) {
      hitMonster.receiveDamage(damageDealt);
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

