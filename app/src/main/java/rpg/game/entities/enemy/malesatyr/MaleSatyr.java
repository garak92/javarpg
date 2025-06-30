package rpg.game.entities.enemy.malesatyr;

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

public class MaleSatyr extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new MaleSatyrAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public MaleSatyr(double charPosx, double charPosy,
                     int shield, String name, Level level) {

        super(charPosx, charPosy, 0.01, 50, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/satyr/satyr_3/Idle.png");
                put("dead", "/enemies/satyr/satyr_3/Dead.png");
                put("walk", "/enemies/satyr/satyr_3/Walk.png");
                put("attack", "/enemies/satyr/satyr_3/Attack.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 300, 6, Animation.INDEFINITE));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 4, 1));
                put("walk", SpriteAnimation.newInstance(imageView, 800, 12, Animation.INDEFINITE));
                put("attack", SpriteAnimation.newInstance(imageView, 300, 9, 1));
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
        animationController.update();
        ai.update(usables);
    }
}
