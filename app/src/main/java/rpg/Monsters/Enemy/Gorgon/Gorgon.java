package rpg.Monsters.Enemy.Gorgon;

import javafx.animation.Animation;
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

public class Gorgon extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new GorgonAI(this);
  private final AnimationController animationController = new AnimationController(ai, this);

  public Gorgon(double charPosx, double charPosy,
                int shield, String name, Level level) {

    super(charPosx, charPosy, 0.008, 50, alignment, level, name);

    preCacheSprites(new HashMap<>() {
      {
        put("idle", "/enemies/gorgon/Idle.png");
        put("dead", "/enemies/gorgon/Dead.png");
        put("walk", "/enemies/gorgon/Walk.png");
        put("attack", "/enemies/gorgon/Attack_1.png");
      }
    });

    preCacheAnimations(new HashMap<>() {
        {
            put("idle", SpriteAnimation.newInstance(imageView, 400, 7, Animation.INDEFINITE));
            put("dead", SpriteAnimation.newInstance(imageView, 300, 3, 1));
            put("walk", SpriteAnimation.newInstance(imageView, 800, 13, Animation.INDEFINITE));
            put("attack", SpriteAnimation.newInstance(imageView, 1100, 16, 1));
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
