package rpg.game.environment;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;

import java.util.HashMap;
import java.util.List;
import java.util.SplittableRandom;

public class Fountain extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PROP;
    private final int TILE_HEIGHT = 64;
    private final int TILE_WIDTH = 64;

    public Fountain(double charPosx, double charPosy, Level level) {
        super(charPosx, charPosy, 0, 0, alignment, level);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/environment/decorations-medieval.png");
            }
        });



        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(TILE_WIDTH, TILE_HEIGHT * 8, TILE_WIDTH, TILE_HEIGHT));
        getImageView().setFitHeight(TILE_HEIGHT * 1.5);
        getImageView().setPreserveRatio(true);

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 500, 3, Animation.INDEFINITE,
                        TILE_WIDTH, TILE_HEIGHT, 0, TILE_HEIGHT * 8));
            }
        });

        setAnimation(animations.get("idle"));
        animation.play();
    }

    @Override
    public void die() {
        level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
    }
}

