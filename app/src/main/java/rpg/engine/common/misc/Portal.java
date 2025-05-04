package rpg.engine.common.misc;

import java.util.HashMap;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.levels.LevelLoader;
import rpg.engine.levels.Level;
import rpg.engine.levels.LevelNode;
import rpg.game.entities.player.Player;

public class Portal extends BaseMonster implements Usable {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PORTAL;
  private final String destinationLevelName;
  private final String destinationLevelSpritesheet;

  public Portal(
      double charPosx,
      double charPosy,
      double velocity,
      int health,
      int shield, String name,
      Level level, String destinationLevelName, String destinationLevelSpritesheet) {
    super(charPosx, charPosy, velocity, health, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/levels/portal.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 64, 64));
    setAnimation(new SpriteAnimation(imageView, new Duration(600), 8, 8, 0, 0,
        64, 64));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);

    getImageView().setFitHeight(128);
    getImageView().setFitWidth(128);
    getImageView().setPreserveRatio(true);

    this.destinationLevelName = destinationLevelName;
    this.destinationLevelSpritesheet = destinationLevelSpritesheet;

  }

  @Override
  public void die() {
    // level.removeThing(this);
  }

  @Override
  public void update(List<Usable> usables) {

  }

  @Override
  public void use(Player player) {
    logger.info("Using portal to " + destinationLevelName);
    LevelLoader.loadLevel(
        new Level(destinationLevelName, destinationLevelSpritesheet, level.getPane(), level.getStage()).load());
  }

  @Override
  public LevelNode getLevelNode() {
    return this.imageView;
  }

  @Override
  public BaseMonster getBaseMonster() {
    return this;
  }
}
