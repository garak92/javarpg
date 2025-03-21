package rpg.Monsters;

import javafx.animation.Animation;

public class MonsterUtils {
    private MonsterUtils() {
    }

    public static void playAnimationOnlyOnce(Animation animation) {
        animation.stop();
        animation.setCycleCount(1);
        animation.play();
    }
}
