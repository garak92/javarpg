package rpg.Monsters;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Items.MiniHealthPickupItem;
import rpg.Levels.Level;
import rpg.SpriteAnimation;

import java.util.HashMap;
import java.util.List;

public class ElixirOfYouth extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ITEM;
  private final MiniHealthPickupItem item;

  public ElixirOfYouth(double charPosx, double charPosy, Level level) {
    super(charPosx, charPosy, 0, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/items/questitems/17.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));
    getImageView().setFitWidth(30);
    getImageView().setFitHeight(30);

    this.item = new MiniHealthPickupItem(this, this.getLevel().getPlayer());

    logger.info("Item: " + this);
  }

  @Override
  public void die() {
    level.removeThing(this);
    QuestLog.INSTANCE.updateActiveQuests(this);
  }

  @Override
  public void update(List<Usable> usables) {
    item.update();
  }
}
