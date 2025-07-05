package rpg.game.entities.enemy.firewizard;

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

public class FireWizard extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new FireWizardAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public FireWizard(double charPosx, double charPosy,
                      int shield, String name, Level level) {

        super(charPosx, charPosy, 0.004, 300, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/wizard/fire_wizard/Idle.png");
                put("dead", "/enemies/wizard/fire_wizard/Dead.png");
                put("walk", "/enemies/wizard/fire_wizard/Walk.png");
                put("attack", "/enemies/wizard/fire_wizard/Flame_jet.png");
                put("attack_secondary", "/enemies/wizard/fire_wizard/Fireball.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 500, 7, Animation.INDEFINITE));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 6, 1));
                put("walk", SpriteAnimation.newInstance(imageView, 400, 6, Animation.INDEFINITE));
                put("attack", SpriteAnimation.newInstance(imageView, 1000, 14, 1));
                put("attack_secondary", SpriteAnimation.newInstance(imageView, 400, 8, 1));
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
