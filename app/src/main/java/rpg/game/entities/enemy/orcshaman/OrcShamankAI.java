package rpg.game.entities.enemy.orcshaman;

import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.*;
import rpg.game.entities.enemy.malesatyr.MaleSatyrFireball;
import rpg.game.entities.player.Player;

import java.util.Random;

public class OrcShamankAI extends BaseEnemyAI {
    Random random = new Random();
    private final int attackCoolDown = 100;
    EnumMonsterKind[] monsterKinds = {EnumMonsterKind.GORGON, EnumMonsterKind.MINOTAUR, EnumMonsterKind.SATYR,
            EnumMonsterKind.FEM_SATYR, EnumMonsterKind.SKELETON_ARCHER, EnumMonsterKind.SKELETON_SPEARMAN};
    private int randomAttackAccumulator = 100;    public OrcShamankAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 600;
    }

    @Override
    public void attack() {
        if (randomAttackAccumulator == attackCoolDown) {
            transition(EnumEvents.CAST_ATTACK);
            MonsterUtils.spawnMonster(monsterKinds[random.nextInt(monsterKinds.length)],
                            (int) monster.getImageView().getBoundsInParent().getCenterX() + 30,
                            (int) monster.getImageView().getBoundsInParent().getCenterY() + 30);
            randomAttackAccumulator = 0;
        } else {
            transition(EnumEvents.FINISH_ATTACK);
            randomAttackAccumulator++;
        }
    }

    @Override
    public void chase() {
        // This monster is static
    }

    @Override
    public EnumEnemyStates currentState() {
        return super.getCurrentState();
    }
}
