package rpg.Monsters.Enemy.MaleSatyr;

import java.util.HashMap;
import java.util.List;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Abilities.MaleSatyrFireballAttack;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumMonsterAlignment;

public class MaleSatyrFireball extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
  private final MaleSatyrFireballAttack attack;

  public MaleSatyrFireball(double charPosx, double charPosy, Level level, BaseMonster target) {
    super(charPosx, charPosy, 7, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/satyr/satyr_3/fireball.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));
    getImageView().setFitWidth(30);
    getImageView().setFitHeight(30);

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
