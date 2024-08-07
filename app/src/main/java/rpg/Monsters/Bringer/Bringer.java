package rpg.Monsters.Bringer;

import java.util.HashMap;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.Thing;
import rpg.Common.Usable;
import rpg.Levels.LevelNode;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnemyAI;
import rpg.Monsters.EnumEnemyStates;
import rpg.Monsters.EnumMonsterAlignment;

public class Bringer extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new BringerAI(this);

  public Bringer(double charPosx, double charPosy, double velocity, int health,
      int shield, String name, EnumEnemyStates currentState, List<Thing> things, List<LevelNode> solidTiles) {

    super(charPosx, charPosy, velocity, health, alignment, things, solidTiles);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/bringer-of-death/bringer.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 140, 93));

    setAnimation(new SpriteAnimation(imageView, new Duration(600), 7, 7,
        0, 0, 140, 93));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);
  }

  @Override
  public void update(List<Usable> usables) {
    ai.update(usables);
  }
}
