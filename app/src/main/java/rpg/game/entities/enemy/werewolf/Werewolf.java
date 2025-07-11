package rpg.game.entities.enemy.werewolf;

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

public class Werewolf extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new WerewolfAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public Werewolf(double charPosx, double charPosy,
                    int shield, String name, Level level) {

        super(charPosx, charPosy, 0.01, 90, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/werewolf/black_werewolf/Idle.png");
                put("dead", "/enemies/werewolf/black_werewolf/Dead.png");
                put("walk", "/enemies/werewolf/black_werewolf/Run.png");
                put("attack", "/enemies/werewolf/black_werewolf/Charge.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 300, 8, Animation.INDEFINITE));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 2, 1));
                put("walk", SpriteAnimation.newInstance(imageView, 500, 9, Animation.INDEFINITE));
                put("attack", SpriteAnimation.newInstance(imageView, 300, 7, 1));
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
