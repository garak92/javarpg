package rpg.Monsters.Enemy.SkeletonArcher;

import javafx.geometry.Rectangle2D;
import rpg.Abilities.MaleSatyrFireballAttack;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;

public class SkeletonArcherArrow extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
  private final MaleSatyrFireballAttack attack;

  public SkeletonArcherArrow(double charPosx, double charPosy, Level level, BaseMonster target) {
    super(charPosx, charPosy, 8, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/skeleton/skeleton_archer/Arrow.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));

    this.attack = new MaleSatyrFireballAttack(this, target);

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
