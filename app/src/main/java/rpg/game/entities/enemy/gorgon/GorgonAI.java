package rpg.game.entities.enemy.gorgon;

import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class GorgonAI extends BaseEnemyAI {
    private final int attackCoolDown = 30;
    private final int attackPositionOffset = 70;
    private int randomAttackAccumulator = 0;

    public GorgonAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 500;
    }

    @Override
    public void attack() {
        if (randomAttackAccumulator == attackCoolDown) {
            transition(EnumEvents.CAST_ATTACK);
            monster.getLevel()
                    .addThing(new GorgonWaterball(monster.getImageView().getBoundsInParent().getCenterX(),
                            monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance(),
                            Player.getInstance().getCharPosx(), Player.getInstance().getCharPosy() + attackPositionOffset));

            monster.getLevel()
                    .addThing(new GorgonWaterball(monster.getImageView().getBoundsInParent().getCenterX(),
                            monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance(),
                            Player.getInstance().getCharPosx(), Player.getInstance().getCharPosy()));

            monster.getLevel()
                    .addThing(new GorgonWaterball(monster.getImageView().getBoundsInParent().getCenterX(),
                            monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance(),
                            Player.getInstance().getCharPosx(),
                            Player.getInstance().getCharPosy() - attackPositionOffset));
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
