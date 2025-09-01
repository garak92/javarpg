package rpg.engine.levels;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EntityNode extends ImageView {
    // This class represents those JavaFX nodes that are not part of the level (monsters, basically)
    NodeTypeEnum type;
    boolean solid;

    public EntityNode(NodeTypeEnum type, boolean solid, Image image) {
        super(image);
        this.solid = solid;
        this.type = type;
    }

    public EntityNode(NodeTypeEnum type) {
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
