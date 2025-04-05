package rpg.Monsters;

import javafx.geometry.Rectangle2D;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Items.ElixirOfYouthItem;
import rpg.Levels.Level;

import java.util.HashMap;
import java.util.List;

public class ElixirOfYouth extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ITEM;
  private final ElixirOfYouthItem item;

  public ElixirOfYouth(double charPosx, double charPosy, Level level) {
    super(charPosx, charPosy, 0, 0, alignment, level);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/items/questitems/17.png");
      }
    });

    imageView.setImage(images.get("idle"));
    imageView.setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));
    imageView.setFitWidth(40);
    imageView.setFitHeight(40);

    this.item = new ElixirOfYouthItem(this, this.getLevel().getPlayer());

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
