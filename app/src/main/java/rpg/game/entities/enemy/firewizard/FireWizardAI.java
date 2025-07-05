package rpg.game.entities.enemy.firewizard;

import rpg.engine.ai.BaseEnemyAI;
import rpg.engine.ai.EnumEnemyStates;
import rpg.engine.ai.EnumEvents;
import rpg.engine.monster.AttackPhase;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.MonsterUtils;
import rpg.game.entities.player.Player;

import java.util.SplittableRandom;

public class FireWizardAI extends BaseEnemyAI {
    private AttackPhase attackPhase = AttackPhase.IDLE;
    private int attackTimer = 0;
    private final SplittableRandom rngGenerator = new SplittableRandom();

    private double targetX, targetY;

    private static final int WINDUP_TIME = 15;
    private static final int COOLDOWN_TIME = 15;
    private static final double JUMP_SPEED = 22;
    private static final int MELEE_ATTACK_DAMAGE = 10;
    private static final int MEELE_ATTACK_RANGE = 520;

    public FireWizardAI(BaseMonster monster) {
        super(monster, Player.getInstance());
        attackRange = 800;
    }

    @Override
    public void attack() {
        if (attackPhase == AttackPhase.IDLE && rngGenerator.nextDouble() < 0.6) {
            transition(EnumEvents.CAST_ATTACK_SECONDARY);
            shootFireball();
            return;
        }

        if(!checkMonsterInAttackRange(this.monster, MEELE_ATTACK_RANGE)) {
            transition(EnumEvents.FINISH_ATTACK);
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
                    player.receiveDamage(MELEE_ATTACK_DAMAGE);
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

    private void shootFireball() {
                monster.getLevel()
                        .addThing(new FireWizardFireball(monster.getImageView().getBoundsInParent().getCenterX(),
                                monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance()));
            }

    @Override
    public EnumEnemyStates currentState() {
        return super.getCurrentState();
    }
}


