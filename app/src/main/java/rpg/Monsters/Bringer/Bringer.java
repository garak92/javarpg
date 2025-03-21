package rpg.Monsters.Bringer;

import java.util.HashMap;
import java.util.List;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnemyAI;
import rpg.Monsters.EnumEnemyStates;
import rpg.Monsters.EnumMonsterAlignment;

public class Bringer extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new BringerAI(this, level.getPlayer());

  public Bringer(double charPosx, double charPosy, double velocity, int health,
      int shield, String name, EnumEnemyStates currentState, Level level) {

    super(charPosx, charPosy, velocity, health, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/satyr/satyr_3/Idle.png");
        put("dead", "/enemies/satyr/satyr_3/Dead.png");
        put("walk", "/enemies/satyr/satyr_3/Walk.png");
        put("attack", "/enemies/satyr/satyr_3/Attack.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 140, 93));

    setAnimation(new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);
  }

  @Override
  public void die() {
    getImageView().setImage(images.get("die"));
    level.removeThing(this);
    QuestLog.INSTANCE.updateActiveQuests(this);
  }

  @Override
  public void update(List<Usable> usables) {
    ai.update(usables);
  }
}
