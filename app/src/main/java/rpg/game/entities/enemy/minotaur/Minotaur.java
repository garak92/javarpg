package rpg.game.entities.enemy.minotaur;

import javafx.animation.Animation;
import rpg.engine.ai.EnemyAI;
import rpg.engine.animation.AnimationController;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.quest.QuestLog;

import java.util.HashMap;
import java.util.List;

public class Minotaur extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new MinotaurAI(this);
  private final AnimationController animationController = new AnimationController(ai, this);

  public Minotaur(double charPosx, double charPosy,
                  int shield, String name, Level level) {

    super(charPosx, charPosy, 0.005, 130, alignment, level, name);

    preCacheSprites(new HashMap<>() {
      {
        put("idle", "/enemies/minotaur/Idle.png");
        put("dead", "/enemies/minotaur/Dead.png");
        put("walk", "/enemies/minotaur/Walk.png");
        put("attack", "/enemies/minotaur/Attack.png");
      }
    });

    preCacheAnimations(new HashMap<>() {
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
