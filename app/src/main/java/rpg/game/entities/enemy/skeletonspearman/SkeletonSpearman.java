package rpg.game.entities.enemy.skeletonspearman;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;
import rpg.engine.ai.EnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.animation.AnimationController;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.quest.QuestLog;

import java.util.HashMap;
import java.util.List;

public class SkeletonSpearman extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new SkeletonSpearmanAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public SkeletonSpearman(double charPosx, double charPosy,
                            int shield, String name, Level level) {

        super(charPosx, charPosy, 0.007, 180, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/skeleton/skeleton_spearman/Idle.png");
                put("dead", "/enemies/skeleton/skeleton_spearman/Dead.png");
                put("walk", "/enemies/skeleton/skeleton_spearman/Walk.png");
                put("attack", "/enemies/skeleton/skeleton_spearman/Charge.png");
                put("parry", "/enemies/skeleton/skeleton_spearman/Protect.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 500, 7, Animation.INDEFINITE));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 5, 1));
                put("walk", SpriteAnimation.newInstance(imageView, 600, 7, Animation.INDEFINITE));
                put("attack", SpriteAnimation.newInstance(imageView, 300, 5, 1));
                put("parry", SpriteAnimation.newInstance(imageView, 1500, 12, 1));
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

    @Override
    public void receiveDamage(int damage) {
        if(ai.currentState() == EnumEnemyStates.PARRY) {
            charPosx -= 10 * getMonster().getImageView().getScaleX(); // Recoil from parrying
            return;
        }

        if (health > 0) {
            ColorAdjust colorAdjust = new ColorAdjust();
            double oldBrightness = colorAdjust.getBrightness();
            colorAdjust.setBrightness(0.8);
            imageView.setEffect(colorAdjust);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));

            this.isHurt = true;
            pause.setOnFinished(event -> {
                this.isHurt = false;
                this.health -= damage;
                colorAdjust.setBrightness(oldBrightness);
                imageView.setEffect(dropShadow);
            });
            pause.play();
        }
        logger.info("Remaining health: " + this.health);
    }

}
