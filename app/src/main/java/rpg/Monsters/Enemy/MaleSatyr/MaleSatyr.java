package rpg.Monsters.Enemy.MaleSatyr;

import java.util.HashMap;
import java.util.List;

import javafx.animation.Animation;
import rpg.Monsters.*;
import rpg.SpriteAnimation;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Levels.Level;

public class MaleSatyr extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new MaleSatyrAI(this);
  private final AnimationController animationController = new AnimationController(ai, this);

  public MaleSatyr(double charPosx, double charPosy,
                   int shield, String name, Level level) {

    super(charPosx, charPosy, 0.01, 50, alignment, level, name);

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
        put("idle", SpriteAnimation.newInstance(imageView, 300, 6, Animation.INDEFINITE));
        put("dead", SpriteAnimation.newInstance(imageView, 300, 4, 1));
        put("walk", SpriteAnimation.newInstance(imageView, 800, 12, Animation.INDEFINITE));
        put("attack", SpriteAnimation.newInstance(imageView, 300,9, 1));
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
