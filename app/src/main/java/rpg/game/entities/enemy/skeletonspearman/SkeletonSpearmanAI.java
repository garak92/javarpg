package rpg.game.entities.enemy.skeletonspearman;

import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;
import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.AttackPhase;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.MonsterUtils;
import rpg.game.entities.player.Player;

import java.util.SplittableRandom;

public class SkeletonSpearmanAI extends BaseEnemyAI {
    private AttackPhase attackPhase = AttackPhase.IDLE;
    private int attackTimer = 0;
    private final SplittableRandom rngGenerator = new SplittableRandom();

    private double targetX, targetY;

    private static final int WINDUP_TIME = 15;
    private static final int COOLDOWN_TIME = 20;
    private static final double JUMP_SPEED = 20;
    private static final int DAMAGE = 10;

    public SkeletonSpearmanAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 530;
    }

    @Override
    public void attack() {
        if (attackPhase == AttackPhase.IDLE && rngGenerator.nextDouble() < 0.4) {
            transition(EnumEvents.PARRY);
            return;
        }

        BaseMonster player = target;

        switch (attackPhase) {
            case IDLE:
                attackPhase = AttackPhase.WINDUP;
                attackTimer = 0;
                break;

            case WINDUP:
                attackTimer++;
                if (attackTimer >= WINDUP_TIME) {
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


