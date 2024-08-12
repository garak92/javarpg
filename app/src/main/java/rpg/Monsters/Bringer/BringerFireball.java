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
  private final BringerFireballAttack attack = new BringerFireballAttack(this);
  double normalizedX = 0;
  double normalizedY = 0;

  public BringerFireball(double charPosx, double charPosy, Level level, double targetX, double targetY) {
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

    double directionX = targetX - charPosx;
    double directionY = targetY - charPosy;

    double length = Math.sqrt(directionX * directionX + directionY * directionY);
    normalizedX = directionX / length;
    normalizedY = directionY / length;

  }

  @Override
  public void update(List<Usable> usables) {
    attack.dealDamage();
    charPosx += normalizedX * charVelx;
    charPosy += normalizedY * charVely;

    imageView.setLayoutX(charPosx);
    imageView.setLayoutY(charPosy);
  }
}
