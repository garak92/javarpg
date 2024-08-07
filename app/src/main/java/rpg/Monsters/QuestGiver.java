package rpg.Monsters;

import java.util.HashMap;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.Thing;
import rpg.Common.Usable;
import rpg.Levels.LevelNode;

public class QuestGiver extends BaseMonster implements Usable {
  private String name;
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.FRIEND;

  public QuestGiver(double charPosx, double charPosy, double velocity, int health,
      int shield, String name, List<Thing> things, List<LevelNode> solidTiles) {
    super(charPosx, charPosy, velocity, health, alignment, things, solidTiles);
    this.name = name;

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/npc/Idle.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 160, 128));

    setAnimation(new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);
  }

  @Override
  public void update(List<Usable> usables) {

  }

  @Override
  public void use(Player player) {
    System.out.println("Hello, my name is " + this.name);
  }

  @Override
  public LevelNode getLevelNode() {
    return getImageView();
  }

}
