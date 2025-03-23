package rpg.Monsters;

import javafx.animation.Animation;
import javafx.util.Duration;
import rpg.SpriteAnimation;

import java.util.HashMap;
import java.util.Map;

import static rpg.Monsters.BaseMonster.logger;

public class AnimationController {
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
                playAnimation(animations.get("attack"));
            }
        }
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
