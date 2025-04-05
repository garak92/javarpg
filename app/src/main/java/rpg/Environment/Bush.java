package rpg.Environment;

import javafx.geometry.Rectangle2D;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;

public class Bush extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ITEM;
    private final int TILE_HEIGHT = 48;
    private final int TILE_WIDTH = 48;

    public Bush(double charPosx, double charPosy, Level level) {
        super(charPosx, charPosy, 0, 0, alignment, level);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/environment/bushes.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(TILE_WIDTH * 1, 0, TILE_WIDTH, TILE_HEIGHT));

    }

    @Override
    public void die() {
        level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
    }
}
