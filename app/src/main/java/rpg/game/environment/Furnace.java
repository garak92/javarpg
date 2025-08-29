package rpg.game.environment;

import javafx.geometry.Rectangle2D;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;
import java.util.SplittableRandom;

public class Furnace extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PROP;
    private final int TILE_HEIGHT = 32;
    private final int TILE_WIDTH = 32;
    private final SplittableRandom rngGenerator = new SplittableRandom();
    private final int spriteOffset = rngGenerator.nextInt(0, 7);

    public Furnace(double charPosx, double charPosy, Level level) {
        super(charPosx, charPosy, 0, 0, alignment, level);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/environment/blacksmith-smelter.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(TILE_WIDTH * spriteOffset, TILE_HEIGHT * 6, TILE_WIDTH, TILE_HEIGHT));
        getImageView().setFitHeight(TILE_HEIGHT * 2);
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

