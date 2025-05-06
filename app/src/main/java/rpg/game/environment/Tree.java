package rpg.game.environment;

import javafx.geometry.Rectangle2D;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;

public class Tree extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PROP;
    private final int TILE_HEIGHT = 70;
    private final int TILE_WIDTH = 64;

    public Tree(double charPosx, double charPosy, Level level) {
        super(charPosx, charPosy, 0, 0, alignment, level);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/environment/trees.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(TILE_WIDTH, 0, TILE_WIDTH, TILE_HEIGHT));
        getImageView().setFitHeight(100);
        getImageView().setPreserveRatio(true);

    }

    @Override
    public void die() {
        level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
    }
}
