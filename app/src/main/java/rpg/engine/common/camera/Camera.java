package rpg.engine.common.camera;

import rpg.engine.monster.BaseMonster;

public class Camera {
    private double cameraX = 0;
    private double cameraY = 0;

    private static final double CAMERA_LERP_SPEED = 0.1;

    public void updateCamera(BaseMonster monster) {
        double targetX = (monster.getImageView().getLayoutX() + monster.getImageView().getTranslateX())
                - monster.getLevel().getPane().getScene().getWidth() / 2;
        double targetY = (monster.getImageView().getLayoutY() + monster.getImageView().getTranslateY()) -
                monster.getLevel().getPane().getScene().getHeight() / 2;

        cameraX += (targetX - cameraX) * CAMERA_LERP_SPEED;
        cameraY += (targetY - cameraY) * CAMERA_LERP_SPEED;

        monster.getLevel().getPane().setTranslateX(-cameraX);
        monster.getLevel().getPane().setTranslateY(-cameraY);
    }

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }
}
