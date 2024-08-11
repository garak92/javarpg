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
  private double targetX;
  private double targetY;

  public BringerFireball(double charPosx, double charPosy, Level level, double targetX, double targetY) {
    super(charPosx, charPosy, 5, 0, alignment, level);
    this.targetX = targetX;
    this.targetY = targetY;

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
  }

  @Override
  public void update(List<Usable> usables) {
    // attack.dealDamage();
    System.out.println(charPosx);
    charPosx -= 5;

    imageView.setLayoutX(charPosx);
  }
}
