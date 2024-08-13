package rpg.Monsters.Bringer;

import java.util.HashMap;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Abilities.BringerFireballAttack;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumMonsterAlignment;

public class BringerFireball extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final BringerFireballAttack attack;

  public BringerFireball(double charPosx, double charPosy, Level level, BaseMonster target) {
    super(charPosx, charPosy, 7, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/bringer-of-death/fireball.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));
    getImageView().setFitWidth(30);
    getImageView().setFitHeight(30);

    setAnimation(new SpriteAnimation(imageView, new Duration(0), 1, 1,
        0, 0, 128, 128));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);

    this.attack = new BringerFireballAttack(this, target);

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
