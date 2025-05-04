package rpg.engine.animation;

import javafx.animation.Animation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.ai.EnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.BaseMonster;

import java.util.Map;

public class AnimationController {
    private static final Logger log = LoggerFactory.getLogger(AnimationController.class);
    EnemyAI ai;
    BaseMonster monster;

    public AnimationController(EnemyAI ai, BaseMonster monster) {
        this.ai = ai;
        this.monster = monster;
    }

    private void selectSpriteAndPlayAnimation() {
        EnumEnemyStates currentState = ai.currentState();
        Map<String, SpriteAnimation> animations = monster.getAnimations();
        switch (currentState) {
            case IDLE -> {
                monster.setImage("idle");
                playAnimation(animations.get("idle"));
            }
            case DEAD -> {
                monster.setImage("dead");
                playAnimation(animations.get("dead"));
            }
            case CHASE -> {
                monster.setImage("walk");
                playAnimation(animations.get("walk"));
            }
            case ATTACK -> {
                monster.setImage("attack");
                playAnimation(animations.get("attack"), EnumEvents.FINISH_ATTACK);
            }
        }
    }

    private void playAnimation(SpriteAnimation animation, EnumEvents monsterEvent) {
        if(animation.equals(monster.getAnimation())) {
            return;
        }
        if(monster.getAnimation() != null) {
            monster.getAnimation().stop();
        }
        monster.setAnimation(animation);

        if(monster.getAnimation().getCycleCount() != Animation.INDEFINITE && !monster.isDead()) {
            ai.setIsPerformingAction(true);
            monster.getAnimation().setOnFinished(e -> {
                ai.setIsPerformingAction(false);
                ai.transition(monsterEvent); // Event to fire when animation is over
            });
        }

        monster.getAnimation().play();
    }

    private void playAnimation(SpriteAnimation animation) {
        if(animation.equals(monster.getAnimation())) {
            return;
        }
        if(monster.getAnimation() != null) {
            monster.getAnimation().stop();
        }
        monster.setAnimation(animation);

        if(monster.getAnimation().getCycleCount() != Animation.INDEFINITE && !monster.isDead()) {
            ai.setIsPerformingAction(true);
            monster.getAnimation().setOnFinished(e -> {
                ai.setIsPerformingAction(false);
            });
        }

        monster.getAnimation().play();
    }

    public void update() throws Throwable {
        selectSpriteAndPlayAnimation();
    }
}
