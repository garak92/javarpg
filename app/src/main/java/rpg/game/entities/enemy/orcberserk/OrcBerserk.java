package rpg.game.entities.enemy.orcberserk;

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

public class OrcBerserk extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
    private final EnemyAI ai = new OrcBerserkAI(this);
    private final AnimationController animationController = new AnimationController(ai, this);

    public OrcBerserk(double charPosx, double charPosy,
                      int shield, String name, Level level) {

        super(charPosx, charPosy, 0.01, 200, alignment, level, name);

        preCacheSprites(new HashMap<>() {
            {
                put("idle", "/enemies/orc/orc_berserk/Idle.png");
                put("dead", "/enemies/orc/orc_berserk/Dead.png");
                put("walk", "/enemies/orc/orc_berserk/Run.png");
                put("attack", "/enemies/orc/orc_berserk/Charge.png");
            }
        });

        preCacheAnimations(new HashMap<>() {
            {
                put("idle", SpriteAnimation.newInstance(imageView, 300, 5, Animation.INDEFINITE, 96,
                        96));
                put("dead", SpriteAnimation.newInstance(imageView, 300, 4, 1, 96, 96));
                put("walk", SpriteAnimation.newInstance(imageView, 800, 6, Animation.INDEFINITE, 96,
                        96));
                put("attack", SpriteAnimation.newInstance(imageView, 200, 5, 1, 96, 96));
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
