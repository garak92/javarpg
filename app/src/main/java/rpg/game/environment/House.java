package rpg.game.environment;

import javafx.geometry.Rectangle2D;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;

public class House extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PROP;
    private final int TILE_SIZE = 1024;
    private final int spriteWidthOffset = (int) (Math.random() * 2);
    private final int spriteHeightOffset = (int) (Math.random() * 2);

    public House(double charPosx, double charPosy, Level level) {
        super(charPosx, charPosy, 0, 0, alignment, level);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/environment/houses.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(TILE_SIZE * spriteWidthOffset, TILE_SIZE * spriteHeightOffset,
                TILE_SIZE, TILE_SIZE));
        getImageView().setFitHeight(200);
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
