package rpg.game.entities.igrenne;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.levels.EntityNode;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.quest.QuestGiver;
import rpg.game.entities.player.Player;

import java.util.HashMap;
import java.util.List;

public class Igrenne extends BaseMonster implements Usable {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.FRIEND;
    private static final String[] defaultDialogueList = {"This level is cool, uh?", "Long live the king!",
            "For the Allia...oh, wrong game, sorry"};
    private static Igrenne instance;
    private QuestGiver questGiver;
    private Level level;

    private Igrenne(
            double charPosx,
            double charPosy,
            double velocity,
            int health,
            int shield, String name,
            Level level) {
        super(charPosx, charPosy, velocity, health, alignment, level, name);

        preCacheSprites(new HashMap<String, String>() {
            {
                put("idle", "/npc/givers/enchantress/Idle.png");
            }
        });

        getImageView().setImage(images.get("idle"));
        getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 160, 128));
        setAnimation(SpriteAnimation.newInstance(imageView, 500, 5, Animation.INDEFINITE));

        this.questGiver = new QuestGiver(this, defaultDialogueList);
    }

    public static Igrenne initialize(double charPosx,
                                     double charPosy,
                                     double velocity,
                                     int health,
                                     int shield, String name,
                                     Level level) {
        if (instance == null) {
            instance = new Igrenne(charPosx, charPosy, velocity, health,
                    shield, name, level);
        } else {
            // Player should maintain their stats throughout a level transition
            // but other stuff, such as the level data, must obviously change
            instance.setCharPosx(charPosx);
            instance.setCharPosy(charPosy);
            instance.level = level;
            instance.questGiver = new QuestGiver(instance, defaultDialogueList);
            instance.questGiver.showExclamation();
        }
        return instance;
    }

    @Override
    public void die() {
        // level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
    }

    @Override
    public void use(Player player) {
        questGiver.use(player);
    }

    @Override
    public EntityNode getLevelNode() {
        return this.imageView;
    }

    @Override
    public BaseMonster getBaseMonster() {
        return this;
    }

}
