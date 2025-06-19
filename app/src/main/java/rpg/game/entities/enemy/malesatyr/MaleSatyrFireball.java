package rpg.game.entities.enemy.malesatyr;

import javafx.geometry.Rectangle2D;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.game.abilities.MaleSatyrFireballAttack;

import java.util.HashMap;
import java.util.List;

public class MaleSatyrFireball extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
    private final MaleSatyrFireballAttack attack;
    private final BaseMonster target;

    public MaleSatyrFireball(double charPosx, double charPosy, Level level, BaseMonster target) {
        super(charPosx, charPosy, 7, 0, alignment, level);

        preCacheSprites(new HashMap<String, String>() {
            {
                put("idle", "/enemies/satyr/satyr_3/fireball.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));
        getImageView().setFitWidth(30);
        getImageView().setFitHeight(30);

        this.target = target;
        this.attack = new MaleSatyrFireballAttack(this, target);

    }

    @Override
    public void die() {
        level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
        if (target.getCharPosx() - getCharPosx() > 0) {
            getImageView().setScaleX(1);
        } else {
            getImageView().setScaleX(-1);
        }
        attack.update();
    }
}
