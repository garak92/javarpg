package rpg.game.entities.enemy.femalesatyr;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.game.abilities.FemaleSatyrFireballAttack;
import rpg.game.abilities.MaleSatyrFireballAttack;

import java.util.HashMap;
import java.util.List;

public class FemaleSatyrFireball extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
    private final FemaleSatyrFireballAttack attack;
    private final BaseMonster target;

    public FemaleSatyrFireball(double charPosx, double charPosy, Level level, BaseMonster target) {
        super(charPosx, charPosy, 9, 0, alignment, level);

        preCacheSprites(new HashMap<String, String>() {
            {
                put("idle", "/enemies/satyr/satyr_1/fireball.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 0, 0));

        this.target = target;
        this.attack = new FemaleSatyrFireballAttack(this, target);
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
