package rpg.game.entities.enemy.malesatyr;

import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class MaleSatyrAI extends BaseEnemyAI {
    private final int attackCoolDown = 20;
    private int randomAttackAccumulator = 0;

    public MaleSatyrAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 500;
    }

    @Override
    public void attack() {
        if (randomAttackAccumulator == attackCoolDown) {
            transition(EnumEvents.CAST_ATTACK);
            monster.getLevel()
                    .addThing(new MaleSatyrFireball(monster.getImageView().getBoundsInParent().getCenterX(),
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
