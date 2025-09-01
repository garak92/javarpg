package rpg.engine.levels;

import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class LevelNode extends Region {
    // This class represents those JavaFX nodes that are part of the level (tiles)
    // We extended Region because allows us to add rect with ImagePattern as a children
    // If we used a ImageView and just set its width/height, the texture would appear stretched, when it should repeat
    // instead (ImagePattern)

    private final NodeTypeEnum type;
    private boolean solid;

    public LevelNode(NodeTypeEnum type, boolean solid, Image tileImage, int width, int height) {
        this.type = type;
        this.solid = solid;

        Rectangle rect = new Rectangle(width, height);
        rect.setFill(new ImagePattern(tileImage, 0, 0, 128, 128, false));

        getChildren().add(rect);
    }

    public NodeTypeEnum getType() { return type; }
    public boolean isSolid() { return solid; }
    public void setSolid(boolean solid) { this.solid = solid; }
}
