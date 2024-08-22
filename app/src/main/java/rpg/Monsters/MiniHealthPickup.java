package rpg.Monsters;

import java.util.HashMap;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.Usable;
import rpg.Items.MiniHealthPickupItem;
import rpg.Levels.Level;

public class MiniHealthPickup extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ITEM;
  private final MiniHealthPickupItem item;

  public MiniHealthPickup(double charPosx, double charPosy, Level level) {
    super(charPosx, charPosy, 0, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/items/miniHealthPickup.png");
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

    this.item = new MiniHealthPickupItem(this, this.getLevel().getPlayer());

    logger.info("Item: " + this);

  }

  @Override
  public void die() {
    level.removeThing(this);
  }

  @Override
  public void update(List<Usable> usables) {
    item.update();
  }
}