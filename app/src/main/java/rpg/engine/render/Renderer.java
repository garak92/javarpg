package rpg.engine.render;

import javafx.scene.image.ImageView;

public class Renderer implements IRenderer {
    private final ImageView imageView;

    public Renderer(ImageView imageView) {
        this.imageView = imageView;
        this.imageView.setPreserveRatio(true);
    }

    @Override
    public void updatePosition(double x, double y) {
        imageView.setTranslateX(x);
        imageView.setTranslateY(y);
    }
}