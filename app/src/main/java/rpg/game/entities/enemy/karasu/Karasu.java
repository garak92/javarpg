package rpg.game.entities.enemy.karasu;

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

public class Karasu extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new KarasuAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public Karasu(double charPosx, double charPosy,
                  int shield, String name, Level level) {

        super(charPosx, charPosy, 0.002, 170, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/yokai/karasu_tengu/Idle_2.png");
                put("dead", "/enemies/yokai/karasu_tengu/Dead.png");
                put("walk", "/enemies/yokai/karasu_tengu/Walk.png");
                put("attack", "/enemies/yokai/karasu_tengu/Attack_2.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 500, 5, Animation.INDEFINITE));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 6, 1));
                put("walk", SpriteAnimation.newInstance(imageView, 1000, 8, Animation.INDEFINITE));
                put("attack", SpriteAnimation.newInstance(imageView, 300, 4, 1));
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
        animationController.update();
        ai.update(usables);
    }

}
