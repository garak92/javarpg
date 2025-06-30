package rpg.game.entities.enemy.orcshaman;

import javafx.animation.Animation;
import rpg.engine.ai.EnemyAI;
import rpg.engine.animation.AnimationController;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.quest.QuestLog;

import java.util.HashMap;
import java.util.List;

public class OrcShaman extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new OrcShamanAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public OrcShaman(double charPosx, double charPosy,
                     int shield, String name, Level level) {

        super(charPosx, charPosy, 0.01, 200, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/orc/orc_shaman/Idle.png");
                put("dead", "/enemies/orc/orc_shaman/Dead.png");
                put("walk", "/enemies/orc/orc_shaman/Idle.png");
                put("attack", "/enemies/orc/orc_shaman/Magic_2.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 300, 5, Animation.INDEFINITE, 96,
                        96));
                put("walk", SpriteAnimation.newInstance(imageView, 300, 5, 1, 96, 96));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 5, 1, 96, 96));
                put("attack", SpriteAnimation.newInstance(imageView, 600, 6, 1, 96, 96));
            }
        });
    }

    @Override
    public void die() {
        getMonster().setDead();
        QuestLog.INSTANCE.updateActiveQuests(this);
    }

    @Override
    public void update(List<Usable> usables) throws Throwable {
        animationController.update();
        ai.update(usables);
    }

}
