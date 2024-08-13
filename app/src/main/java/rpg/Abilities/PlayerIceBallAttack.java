package rpg.Abilities;

import java.util.ArrayList;
import java.util.List;

import rpg.Monsters.BaseMonster;

public class PlayerIceBallAttack extends BaseAbility {
  private List<BaseMonster> enemies = new ArrayList<>();
  double direction = 1;

  public PlayerIceBallAttack(BaseMonster monster, List<BaseMonster> enemies, double direction) {
    super(15, 0, 0, monster);
    this.enemies = enemies;
    this.direction = direction;
  }

  @Override
  protected void dealDamage() {
    for (BaseMonster i : enemies) {
      if (monster.getMonster().detectCollision(i)) {
        i.receiveDamage(damageDealt);
        monster.die();
      }
    }
  }

  @Override
  public void update() {
    dealDamage();
    if (monster.getMonster().detectCollision(monster.getLevel().getSolidTiles())) {
      monster.die();
    }

    if (direction == -1) {
      monster.setCharPosx(monster.getCharPosx() - monster.getCharVelx());
    } else {
      monster.setCharPosx(monster.getCharPosx() + monster.getCharVelx());
    }

    monster.getImageView().setLayoutX(monster.getCharPosx());
    monster.getImageView().setLayoutY(monster.getCharPosy());
  }
}
