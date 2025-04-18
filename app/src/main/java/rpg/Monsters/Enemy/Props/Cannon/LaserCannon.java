package rpg.Monsters.Enemy.Props.Cannon;

import javafx.geometry.Rectangle2D;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.*;

import java.util.HashMap;
import java.util.List;

public class LaserCannon extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PROP;
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
              .addThing(new LaserCannonLaser(this.getMonster().getImageView().getLayoutX(),
                      this.getMonster().getImageView().getLayoutY(),
                      this.getMonster().getLevel(), this.orientation));
      attackCoolDownCounter = 0;
    } else {
      attackCoolDownCounter++;
    }
  }
}
