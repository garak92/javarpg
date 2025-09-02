package rpg.game.environment;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.quest.QuestLog;

import java.util.HashMap;
import java.util.List;

public class ForceField extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PROP;
    private final int TILE_HEIGHT = 68;
    private final int TILE_WIDTH = 34;
    private String unlockQuestName = null;

    public ForceField(double charPosx, double charPosy, Level level, String unlockingQuestName) {
        super(charPosx, charPosy, 0, 0, alignment, level);

        this.unlockQuestName = unlockingQuestName;

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/levels/forceField.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(0, 0, TILE_WIDTH, TILE_HEIGHT));
        getImageView().setFitHeight(TILE_HEIGHT * 1.5);
        getImageView().setPreserveRatio(true);

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 500, 6, Animation.INDEFINITE,
                        TILE_WIDTH, TILE_HEIGHT));
            }
        });

        setAnimation(animations.get("idle"));
        animation.play();
    }

    @Override
    public void die() {
        level.removeThing(this);
        level.getEnvProps().remove(this);
    }

    @Override
    public void update(List<Usable> usables) {
        if(unlockQuestName != null && !QuestLog.INSTANCE.isQuestTaken(unlockQuestName)) {
            return;
        }

        die();
    }
}

