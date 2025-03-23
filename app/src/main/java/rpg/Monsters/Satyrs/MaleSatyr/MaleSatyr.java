package rpg.Monsters.Satyrs.MaleSatyr;

import java.util.HashMap;
import java.util.List;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.Monsters.*;
import rpg.SpriteAnimation;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Levels.Level;

public class MaleSatyr extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new MaleSatyrAI(this);
  private final AnimationController animationController = new AnimationController(ai, this);

  public MaleSatyr(double charPosx, double charPosy, double velocity, int health,
                   int shield, String name, Level level) {

    super(charPosx, charPosy, velocity, health, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/satyr/satyr_3/Idle.png");
        put("dead", "/enemies/satyr/satyr_3/Dead.png");
        put("walk", "/enemies/satyr/satyr_3/Walk.png");
        put("attack", "/enemies/satyr/satyr_3/Attack.png");
      }
    });

    preCacheAnimations(new HashMap<String, SpriteAnimation>() {
      {
        put("idle", new SpriteAnimation(imageView, new Duration(300), 6, 6, 0, 0, 128, 160, Animation.INDEFINITE));
        put("dead", new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160, 1));
        put("walk", new SpriteAnimation(imageView, new Duration(800), 12, 12, 0, 0, 128, 160, Animation.INDEFINITE));
        put("attack", new SpriteAnimation(imageView, new Duration(300),9 , 9, 0, 0, 128, 160, 1));
      }
    });
  }

  @Override
  public void die() {
    QuestLog.INSTANCE.updateActiveQuests(this);
    getMonster().setDead();
  }

  @Override
  public void update(List<Usable> usables) throws Throwable {
    ai.update(usables);
    animationController.update();
  }
}
