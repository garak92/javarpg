package rpg.game.entities.enemy.skeletonarcher;

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

public class SkeletonArcher extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new SkeletonArcherAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public SkeletonArcher(double charPosx, double charPosy,
                          int shield, String name, Level level) {

        super(charPosx, charPosy, 0.003, 100, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/skeleton/skeleton_archer/Idle.png");
                put("dead", "/enemies/skeleton/skeleton_archer/Dead.png");
                put("walk", "/enemies/skeleton/skeleton_archer/Walk.png");
                put("attack", "/enemies/skeleton/skeleton_archer/Shot_1.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 300, 7, Animation.INDEFINITE));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 5, 1));
                put("walk", SpriteAnimation.newInstance(imageView, 500, 8, Animation.INDEFINITE));
                put("attack", SpriteAnimation.newInstance(imageView, 500, 15, 1));
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
