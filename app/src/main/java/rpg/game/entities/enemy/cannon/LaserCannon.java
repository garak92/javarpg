package rpg.game.entities.enemy.cannon;

import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.quest.QuestLog;

import java.util.HashMap;
import java.util.List;

public class LaserCannon extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.TURRET;
  CannonOrientation orientation = null;
  int attackCoolDown = 100;
  int attackCoolDownCounter = (int)(Math.random() * attackCoolDown);

  public LaserCannon(double charPosx, double charPosy,
                int shield, String name, Level level, CannonOrientation orientation) {

    super(charPosx, charPosy, 0.01, 0, alignment, level, name);

    preCacheSprites(new HashMap<>() {
      {
        put("idle", "/props/MG2/MG2.png");
      }
    });

    imageView.setImage(images.get("idle"));
    imageView.setFitWidth(60);
    imageView.setFitHeight(60);

    this.orientation = orientation;

    switch (this.orientation) {
      case UP:
        imageView.setScaleY(1);
        break;
      case DOWN:
        imageView.setScaleY(-1);
        break;
      case LEFT:
        imageView.setRotate(imageView.getRotate() - 90);
        break;
      case RIGHT:
        imageView.setRotate(imageView.getRotate() + 90);
        break;
    }
  }

  @Override
  public void die() {
    QuestLog.INSTANCE.updateActiveQuests(this);
    getMonster().setDead();
  }

  @Override
  public void update(List<Usable> usables) throws Throwable {
    if(attackCoolDownCounter == attackCoolDown) {
      this.getMonster().getLevel()
              .addThing(new LaserCannonLaser(this.getMonster().getCharPosx(),
                      this.getMonster().getCharPosy(),
                      this.getMonster().getLevel(), this.orientation));
      attackCoolDownCounter = 0;
    } else {
      attackCoolDownCounter++;
    }
  }
}
