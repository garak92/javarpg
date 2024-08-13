package rpg.Monsters;

import java.util.HashMap;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Abilities.PlayerIceBallAttack;
import rpg.Common.Usable;
import rpg.Levels.Level;

public class PlayerIceBall extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PLAYER_ATTACK;
  private final PlayerIceBallAttack attack;

  public PlayerIceBall(double charPosx, double charPosy, Level level, List<BaseMonster> targetList, double direction) {
    super(charPosx, charPosy, 7, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/player/iceball.png");
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

    this.attack = new PlayerIceBallAttack(this, targetList, direction);

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
