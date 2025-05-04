package rpg.game.entities.enemy.skeletonarcher;

import javafx.geometry.Rectangle2D;
import rpg.game.abilities.MaleSatyrFireballAttack;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;

public class SkeletonArcherArrow extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
  private final MaleSatyrFireballAttack attack;
  private final BaseMonster target;

  public SkeletonArcherArrow(double charPosx, double charPosy, Level level, BaseMonster target) {
    super(charPosx, charPosy, 10, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/skeleton/skeleton_archer/Arrow.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));

    this.target = target;
    this.attack = new MaleSatyrFireballAttack(this, target);

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
