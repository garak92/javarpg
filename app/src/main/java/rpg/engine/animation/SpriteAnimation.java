package rpg.engine.animation;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import rpg.engine.common.Game;

public class SpriteAnimation extends Transition {

    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    private int lastIndex;

    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height, int cycleCount) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
        setCycleCount(cycleCount);
    }

    public static SpriteAnimation newInstance(ImageView imageView, int duration, int framesQty, int cycleCount) {
        return new SpriteAnimation(imageView, new Duration(duration), framesQty, framesQty, 0, 0,
                128, 128, cycleCount);
    }

    public static SpriteAnimation newInstance(ImageView imageView, int duration, int framesQty, int cycleCount,
                                              int width, int height) {
        return new SpriteAnimation(imageView, new Duration(duration), framesQty, framesQty, 0, 0,
                width, height, cycleCount);
    }

    @Override
    protected void interpolate(double k) {
        if(Game.getInstance().isPaused()) {
            return;
        }
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        }
    }

    @Override
    public String toString() {
        return "SpriteAnimation{" +
                "imageView=" + imageView +
                ", count=" + count +
                ", columns=" + columns +
                ", offsetX=" + offsetX +
                ", offsetY=" + offsetY +
                ", width=" + width +
                ", height=" + height +
                ", lastIndex=" + lastIndex +
                '}';
    }
}
