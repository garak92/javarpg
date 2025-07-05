package rpg.game.entities.enemy.firewizard;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.game.abilities.MaleSatyrFireballAttack;

import java.util.HashMap;
import java.util.List;

public class FireWizardFireball extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ATTACK;
    private final MaleSatyrFireballAttack attack;
    private final BaseMonster target;

    public FireWizardFireball(double charPosx, double charPosy, Level level, BaseMonster target) {
        super(charPosx, charPosy, 10, 0, alignment, level);

        preCacheSprites(new HashMap<String, String>() {
            {
                put("idle", "/enemies/wizard/fire_wizard/Charge.png");
            }
        });


        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 300, 8, 1, 64,
                        64));
            }
        });

        getImageView().setImage(images.get("idle"));
        setAnimation(getAnimations().get("idle"));

        this.target = target;
        this.attack = new MaleSatyrFireballAttack(this, target);

        getAnimation().setOnFinished(e -> {
          getAnimation().pause();
        });
        getAnimation().play();
    }

    @Override
    public void die() {
        level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
        if(getAnimation().getStatus() != Animation.Status.PAUSED) {
            if (target.getCharPosx() - getCharPosx() > 0) {
                getImageView().setScaleX(1);
            } else {
                getImageView().setScaleX(-1);
            }
        }

        attack.update();
    }
}
