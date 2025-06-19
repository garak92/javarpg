package rpg.engine.levels;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LevelNode extends ImageView {
    NodeTypeEnum type;
    boolean solid;

    public LevelNode(NodeTypeEnum type, boolean solid, Image image) {
        super(image);
        this.solid = solid;
        this.type = type;
    }

    public LevelNode(NodeTypeEnum type) {
        this.solid = true;
        this.type = type;
    }

    public boolean isSolid() {
        return solid;
    }

    public void setSolid(boolean solid) {
        this.solid = solid;
    }

    public NodeTypeEnum getType() {
        return type;
    }

    public void setType(NodeTypeEnum type) {
        this.type = type;
    }
}
