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

    public static void lerp(BaseMonster monster, BaseMonster target, double speed) {
        monster.charPosx += (target.charPosx - monster.charPosx) * speed;
        monster.charPosy += (target.charPosy - monster.charPosy) * speed;
        monster.imageView.setLayoutX(monster.charPosx);
        monster.imageView.setLayoutY(monster.charPosy);
    }

    public static void jumpToDirection(BaseMonster monster, double targetPosX, double targetPosY,
                                       double speedBoost) {
        double directionX = targetPosX - monster.getCharPosx();
        double directionY = targetPosY - monster.getCharPosy();
        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        double normalizedX = directionX / length;
        double normalizedY = directionY / length;
        monster.setCharPosx(monster.getCharPosx() + normalizedX * speedBoost);
        monster.setCharPosy(monster.getCharPosy() + normalizedY * speedBoost);

        monster.getImageView().setLayoutX(monster.getCharPosx());
        monster.getImageView().setLayoutY(monster.getCharPosy());

    }

}
