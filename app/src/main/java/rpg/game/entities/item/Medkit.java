package rpg.game.entities.item;

import javafx.geometry.Rectangle2D;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.game.items.MedKitItem;
import rpg.game.items.MiniHealthPickupItem;

import java.util.HashMap;
import java.util.List;

public class Medkit extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ITEM;
    private final MedKitItem item;

    public Medkit(double charPosx, double charPosy, Level level) {
        super(charPosx, charPosy, 0, 0, alignment, level);

        preCacheSprites(new HashMap<String, String>() {
            {
                put("idle", "/items/potions/Icon25.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));
        getImageView().setFitWidth(40);
        getImageView().setFitHeight(40);

        this.item = new MedKitItem(this);

        logger.info("Item: " + this);

    }

    @Override
    public void die() {
        level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
        item.update();
    }
}
