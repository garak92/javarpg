package rpg.game.entities.enemy.skeletonarcher;

import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class SkeletonArcherAI extends BaseEnemyAI {
    private final int attackCoolDown = 20;
    private int randomAttackAccumulator = 0;

    public SkeletonArcherAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 400;
    }

    @Override
    public void attack() {
        if (randomAttackAccumulator == attackCoolDown) {
            transition(EnumEvents.CAST_ATTACK);
            monster.getLevel()
                    .addThing(new SkeletonArcherArrow(monster.getImageView().getBoundsInParent().getCenterX(),
                            monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance()));
            randomAttackAccumulator = 0;
        } else {
            transition(EnumEvents.FINISH_ATTACK);
            randomAttackAccumulator++;
        }
    }

    @Override
    public EnumEnemyStates currentState() {
        return super.getCurrentState();
    }


}
