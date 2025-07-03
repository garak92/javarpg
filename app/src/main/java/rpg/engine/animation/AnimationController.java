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
            case IDLE -> playAnimation("idle", animations.get("idle"));
            case DEAD -> playAnimation("dead", animations.get("dead"));
            case CHASE -> playAnimation("walk", animations.get("walk"));
            case ATTACK -> playAnimation("attack", animations.get("attack"), EnumEvents.FINISH_ATTACK);
            case PARRY -> playAnimation("parry", animations.get("parry"), EnumEvents.FINISH_PARRY);
        }
    }

    private void playAnimation(String imageKey, SpriteAnimation animation, EnumEvents monsterEvent) {
        if (animation == monster.getAnimation()) return;

        if (monster.getAnimation() != null) {
            monster.getAnimation().stop();
        }

        monster.setImage(imageKey); // Moved here, after stopping the current animation
        monster.setAnimation(animation);
        animation.playFromStart();

        if (animation.getCycleCount() != Animation.INDEFINITE && !monster.isDead()) {
            ai.setIsPerformingAction(true);
            animation.setOnFinished(e -> {
                ai.setIsPerformingAction(false);
                ai.transition(monsterEvent);
            });
        }


    }

    private void playAnimation(String imageKey, SpriteAnimation animation) {
        if (animation == monster.getAnimation()) return;

        if (monster.getAnimation() != null) {
            monster.getAnimation().stop();
        }

        monster.setImage(imageKey); // Moved here
        monster.setAnimation(animation);
        animation.playFromStart();

        if (animation.getCycleCount() != Animation.INDEFINITE && !monster.isDead()) {
            ai.setIsPerformingAction(true);
            animation.setOnFinished(e -> {
                ai.setIsPerformingAction(false);
            });
        }


    }

    public void update() throws Throwable {
        selectSpriteAndPlayAnimation();
    }
}
