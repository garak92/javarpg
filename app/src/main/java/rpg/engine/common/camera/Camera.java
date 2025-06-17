package rpg.engine.common.camera;

import javafx.scene.layout.Pane;
import rpg.engine.monster.BaseMonster;

public class Camera {
    private double cameraX = 0;
    private double cameraY = 0;
    private double prevCameraX = 0;
    private double prevCameraY = 0;

    private static final double CAMERA_LERP_SPEED = 0.05;

    public void updateCamera(BaseMonster monster) {
        double targetX = (monster.getImageView().getLayoutX() + monster.getImageView().getTranslateX())
                - monster.getLevel().getPane().getScene().getWidth() / 2;
        double targetY = (monster.getImageView().getLayoutY() + monster.getImageView().getTranslateY()) -
                monster.getLevel().getPane().getScene().getHeight() / 2;
        prevCameraX = cameraX;
        prevCameraY = cameraY;

        cameraX += (targetX - cameraX) * CAMERA_LERP_SPEED;
        cameraY += (targetY - cameraY) * CAMERA_LERP_SPEED;
    }

    public void interpolate(double alpha, Pane pane) throws Throwable {
        pane.setTranslateX(alpha * -cameraX + (1 - alpha) * -prevCameraX);
        pane.setTranslateY(alpha * -cameraY + (1 - alpha) * -prevCameraY);
    }

    public void render(Pane pane) {
        pane.setTranslateX(-cameraX);
        pane.setTranslateY(-cameraY);
    }

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }
}
