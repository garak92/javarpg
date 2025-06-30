package rpg.game.entities.enemy.orcberserk;

import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.AttackPhase;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.MonsterUtils;
import rpg.game.entities.player.Player;

public class OrcBerserkAI extends BaseEnemyAI {
    private AttackPhase attackPhase = AttackPhase.IDLE;
    private int attackTimer = 0;

    private double targetX, targetY;

    private static final int WINDUP_TIME = 15;
    private static final int COOLDOWN_TIME = 30;
    private static final double JUMP_SPEED = 40;
    private static final int DAMAGE = 10;

    public OrcBerserkAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 370;
    }

    @Override
    public void attack() {
        BaseMonster player = target;

        switch (attackPhase) {
            case IDLE:
                // Start the attack
                attackPhase = AttackPhase.WINDUP;
                attackTimer = 0;
                break;

            case WINDUP:
                // Face the player, charge the attack
                attackTimer++;
                if (attackTimer >= WINDUP_TIME) {
                    // Lock onto player’s position and start jump
                    targetX = player.getCharPosx();
                    targetY = player.getCharPosy();
                    attackPhase = AttackPhase.JUMPING;
                }
                break;

            case JUMPING:
                boolean reached = MonsterUtils.jumpToDirection(monster, targetX, targetY, JUMP_SPEED);
                if (reached) {
                    attackPhase = AttackPhase.LAND;
                }
                break;

            case LAND:
                transition(EnumEvents.CAST_ATTACK);
                if (monster.detectCollision(player)) {
                    player.receiveDamage(DAMAGE);
                }
                attackPhase = AttackPhase.COOLDOWN;
                attackTimer = 0;
                break;

            case COOLDOWN:
                attackTimer++;
                if (attackTimer >= COOLDOWN_TIME) {
                    attackPhase = AttackPhase.IDLE;
                }
                break;
        }
    }

    @Override
    public EnumEnemyStates currentState() {
        return super.getCurrentState();
    }
}
