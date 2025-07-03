package rpg.game.entities.enemy.orcshaman;

import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.*;
import rpg.game.entities.player.Player;

import java.util.Random;

public class OrcShamanAI extends BaseEnemyAI {
    Random random = new Random();
    private final int attackCoolDown = 150;
    EnumMonsterKind[] monsterKinds = {EnumMonsterKind.GORGON, EnumMonsterKind.MINOTAUR, EnumMonsterKind.SATYR,
            EnumMonsterKind.FEM_SATYR, EnumMonsterKind.SKELETON_ARCHER, EnumMonsterKind.SKELETON_SPEARMAN};
    private int randomAttackAccumulator = 150;

    public OrcShamanAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 800;
    }

    @Override
    public void attack() {
        if (randomAttackAccumulator == attackCoolDown) {
            transition(EnumEvents.CAST_ATTACK);

            BaseMonster player = monster.getLevel().getPlayer(); // Adjust this line if needed
            double summonerX = monster.getImageView().getBoundsInParent().getCenterX();
            double summonerY = monster.getImageView().getBoundsInParent().getCenterY();

            double playerX = player.getImageView().getBoundsInParent().getCenterX();
            double playerY = player.getImageView().getBoundsInParent().getCenterY();

            // Direction vector from summoner to player
            double dirX = playerX - summonerX;
            double dirY = playerY - summonerY;

            // Normalize
            double length = Math.sqrt(dirX * dirX + dirY * dirY);
            if (length == 0) length = 1; // Prevent divide by zero

            double normalizedX = dirX / length;
            double normalizedY = dirY / length;

            // Spawn offset distance (adjust as needed)
            int spawnDistance = 250;

            int spawnX = (int) (summonerX + normalizedX * spawnDistance);
            int spawnY = (int) (summonerY + normalizedY * spawnDistance);

            if(monster.detectCollision(monster.getLevel().getSolidTiles(), spawnX, spawnY)) {
                return;
            }

            // To do: look at the feasibility/desirability of having a global hard-coded entity quantity limit

            MonsterUtils.spawnMonster(
                    monsterKinds[random.nextInt(monsterKinds.length)],
                    spawnX,
                    spawnY
            );

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
