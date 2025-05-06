package rpg.game.entities.player;

import javafx.geometry.Rectangle2D;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.game.abilities.PlayerIceBallAttack;

import java.util.HashMap;
import java.util.List;

public class PlayerIceBall extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
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
