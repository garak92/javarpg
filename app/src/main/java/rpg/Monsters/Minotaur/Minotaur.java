package rpg.Monsters.Minotaur;

import javafx.animation.Animation;
import javafx.util.Duration;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.AnimationController;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnemyAI;
import rpg.Monsters.EnumMonsterAlignment;
import rpg.SpriteAnimation;

import java.util.HashMap;
import java.util.List;

public class Minotaur extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new MinotaurAI(this);
  private final AnimationController animationController = new AnimationController(ai, this);

  public Minotaur(double charPosx, double charPosy,
                  int shield, String name, Level level) {

    super(charPosx, charPosy, 0.005, 130, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/minotaur/Idle.png");
        put("dead", "/enemies/minotaur/Dead.png");
        put("walk", "/enemies/minotaur/Walk.png");
        put("attack", "/enemies/minotaur/Attack.png");
      }
    });

    preCacheAnimations(new HashMap<String, SpriteAnimation>() {
      {
        put("idle", SpriteAnimation.newInstance(imageView, 500, 10, Animation.INDEFINITE));
        put("dead", SpriteAnimation.newInstance(imageView, 300, 5, 1));
        put("walk", SpriteAnimation.newInstance(imageView, 600, 12, Animation.INDEFINITE));
        put("attack", SpriteAnimation.newInstance(imageView, 300,5, 1));
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
