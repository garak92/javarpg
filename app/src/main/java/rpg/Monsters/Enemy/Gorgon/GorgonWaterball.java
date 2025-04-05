package rpg.Monsters.Enemy.Gorgon;

import javafx.geometry.Rectangle2D;
import rpg.Abilities.GorgonWaterballAttack;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;

public class GorgonWaterball extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
  private final GorgonWaterballAttack attack;
  private final BaseMonster target;

  public GorgonWaterball(double charPosx, double charPosy, Level level, BaseMonster target,
                         double targetPosX, double targetPosY) {
    super(charPosx, charPosy, 7, 0, alignment, level);

    preCacheSprites(new HashMap<>() {
      {
        put("idle", "/enemies/gorgon/waterball.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));
    getImageView().setFitWidth(60);
    getImageView().setFitHeight(40);

    this.target = target;
    this.attack = new GorgonWaterballAttack(this, target, targetPosX, targetPosY);

  }

  @Override
  public void die() {
    level.removeThing(this);
  }

  @Override
  public void update(List<Usable> usables) {
    if (target.getCharPosx() - getCharPosx() > 0) {
      getImageView().setScaleX(1);
    } else {
      getImageView().setScaleX(-1);
    }
    attack.update();
  }
}
