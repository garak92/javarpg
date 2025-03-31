package rpg.Monsters.Werewolf;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.*;
import rpg.SpriteAnimation;

import java.util.HashMap;
import java.util.List;

public class Werewolf extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new WerewolfAI(this);
  private final AnimationController animationController = new AnimationController(ai, this);

  public Werewolf(double charPosx, double charPosy,
                  int shield, String name, Level level) {

    super(charPosx, charPosy, 0.01, 90, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/werewolf/black_werewolf/Idle.png");
        put("dead", "/enemies/werewolf/black_werewolf/Dead.png");
        put("walk", "/enemies/werewolf/black_werewolf/Run.png");
        put("attack", "/enemies/werewolf/black_werewolf/Charge.png");
      }
    });

    preCacheAnimations(new HashMap<String, SpriteAnimation>() {
      {
        put("idle", SpriteAnimation.newInstance(imageView, 300, 8, Animation.INDEFINITE));
        put("dead", SpriteAnimation.newInstance(imageView, 300, 2, 1));
        put("walk", SpriteAnimation.newInstance(imageView, 500,9 , Animation.INDEFINITE));
        put("attack", SpriteAnimation.newInstance(imageView, 300,7, 1));
      }
    });
  }

  @Override
  public void die() {
    getMonster().setDead();
    QuestLog.INSTANCE.updateActiveQuests(this);
  }

  @Override
  public void update(List<Usable> usables) throws Throwable {
    ai.update(usables);
    animationController.update();
  }

}
