package rpg.Monsters.Enemy.Props.Cannon;

import javafx.geometry.Rectangle2D;
import rpg.Abilities.LaserCannonAttack;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;

public class LaserCannonLaser extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
  private LaserCannonAttack attack = null;

  public LaserCannonLaser(double charPosx, double charPosy, Level level,
                          CannonOrientation orientation) {
    super(charPosx, charPosy, 13, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/props/MG2/Bullet_MG.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));

    switch (orientation) {
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

    this.attack = new LaserCannonAttack(this, orientation);

  }

  @Override
  public void die() {
    level.removeThing(this);
  }

  @Override
  public void update(List<Usable> usables) {
    attack.update();
  }
}
