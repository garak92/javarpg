package rpg.engine.ai;

import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.common.Usable;
import rpg.engine.monster.BaseMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.stream.Collectors;

public abstract class BaseEnemyAI extends EnemyAI {
    protected static Logger logger = LoggerFactory.getLogger(BaseEnemyAI.class);
    private final List<StateTransition> transitionTable = new ArrayList<>();
    private final SplittableRandom rngGenerator = new SplittableRandom();
    protected final BaseMonster target;
    private final int movementChangeFrequency = 200;
    protected boolean isAttacking = false;
    protected boolean isPerformingAction = false;
    protected double attackRange = 500.0;
    Line lineOfSight = new Line();
    private EnumEnemyStates currentState = EnumEnemyStates.IDLE;
    private boolean shouldMoveRandomly = false;
    private int randomMovementAccumulator = 0;

    public BaseEnemyAI(BaseMonster monster, BaseMonster target) {
        super(monster);

        // Set default transition table
        // IDLE state transitions
        transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // From IDLE to CHASE when AGGROED
        transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.KILLED, EnumEnemyStates.DEAD));  // From IDLE to DEAD when KILLED
        transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // No state change if target killed while idle (you may want this, depending on behavior)
        transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.CAN_ATTACK, EnumEnemyStates.TRY_ATTACK));  // From IDLE to TRY_ATTACK if CAN_ATTACK
        transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // Aggro while in IDLE should always go to CHASE

// CHASE state transitions
        transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.KILLED, EnumEnemyStates.DEAD));  // From CHASE to DEAD when KILLED
        transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // Target KILLED while chasing -> IDLE
        transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.CAN_ATTACK, EnumEnemyStates.TRY_ATTACK));  // From CHASE to TRY_ATTACK if CAN_ATTACK
        transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // Stay in CHASE if already AGGROED
        transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // Chase should stop if target killed (and switch to idle)
        transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.KILLED, EnumEnemyStates.DEAD));  // If KILLED during chase -> DEAD

// ATTACK state transitions
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // From ATTACK to CHASE if AGGROED
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.FINISH_ATTACK, EnumEnemyStates.CHASE));  // From ATTACK to CHASE if AGGROED
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.KILLED, EnumEnemyStates.DEAD));  // From ATTACK to DEAD when KILLED
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // Target KILLED in ATTACK -> back to IDLE


// ATTACK_SECONDARY state transitions
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK_SECONDARY, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // From ATTACK to CHASE if AGGROED
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK_SECONDARY, EnumEvents.FINISH_ATTACK, EnumEnemyStates.CHASE));  // From ATTACK to CHASE if AGGROED
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK_SECONDARY, EnumEvents.KILLED, EnumEnemyStates.DEAD));  // From ATTACK to DEAD when KILLED
        transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK_SECONDARY, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // Target KILLED in ATTACK -> back to IDLE

// TRY_ATTACK state transitions
        transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.CAST_ATTACK, EnumEnemyStates.ATTACK));  // From TRY_ATTACK to ATTACK when CAST_ATTACK
        transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.CAST_ATTACK_SECONDARY, EnumEnemyStates.ATTACK_SECONDARY));  // From TRY_ATTACK to ATTACK when CAST_ATTACK
        transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.FINISH_ATTACK, EnumEnemyStates.CHASE));  // Finish ATTACK -> back to CHASE
        transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // From TRY_ATTACK to CHASE if AGGROED
        transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // Target KILLED in TRY_ATTACK -> IDLE


//  PARRY state transitions
        transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.PARRY, EnumEnemyStates.PARRY));
        transitionTable.add(new StateTransition(EnumEnemyStates.PARRY, EnumEvents.FINISH_PARRY, EnumEnemyStates.CHASE));
        transitionTable.add(new StateTransition(EnumEnemyStates.PARRY, EnumEvents.KILLED, EnumEnemyStates.DEAD));
        transitionTable.add(new StateTransition(EnumEnemyStates.PARRY, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));

// DEAD state transitions
// No transitions out of DEAD, as the enemy is inactive or has finished its lifecycle


        this.target = target;
    }

    public void transition(EnumEvents event) {
        List<StateTransition> transitionState = transitionTable.stream()
                .filter(tr -> tr.getFromState() == this.currentState
                        && tr.getEvent() == event && tr.getToState() != this.currentState)
                .collect(Collectors.toList());

        if (transitionState.size() == 0) {
            return;
        }

        this.currentState = transitionState.get(0).getToState();
    }

    @Override
    public void setIsPerformingAction(boolean isPerformingAction) {
        this.isPerformingAction = isPerformingAction;
    }

    public boolean checkMonsterInAttackRange(BaseMonster target) {
        double currentMonsterDistance = Math
                .sqrt(Math.pow(monster.getCharPosx() - target.getCharPosx(), 2) + Math.pow(monster.getCharPosy() - target.getCharPosy(), 2));
        return currentMonsterDistance <= attackRange;

    }

    public boolean checkMonsterInAttackRange(BaseMonster target, int ATTACK_RANGE) {
        double currentMonsterDistance = Math
                .sqrt(Math.pow(monster.getCharPosx() - target.getCharPosx(), 2) + Math.pow(monster.getCharPosy() - target.getCharPosy(), 2));
        return currentMonsterDistance <= ATTACK_RANGE;

    }

    protected EnumEnemyStates getCurrentState() {
        return currentState;
    }

    @Override
    public void update(List<Usable> usables) {
        if (monster.isDead() && currentState() != EnumEnemyStates.DEAD) {
            currentState = EnumEnemyStates.DEAD;
            return;
        }
        // Look towards the target
        if (target != null && currentState != EnumEnemyStates.IDLE && currentState != EnumEnemyStates.DEAD) {
            if (target.getCharPosx() - monster.getCharPosx() > 0) {
                monster.getImageView().setScaleX(1);
            } else {
                monster.getImageView().setScaleX(-1);
            }
        }

        lineOfSight.setStartX(monster.getImageView().getBoundsInParent().getCenterX());
        lineOfSight.setEndX(target.getImageView().getBoundsInParent().getCenterX());
        lineOfSight.setStartY(monster.getImageView().getBoundsInParent().getCenterY());
        lineOfSight.setEndY(target.getImageView().getBoundsInParent().getCenterY());

        if (monster.detectCollision(target) && !target.isDead()) {
            transition(EnumEvents.AGGROED);
        }

        if (checkMonsterInAttackRange(target) && !target.isDead()
                && monster.isTargetInLineOfSight(monster.getLevel().getSolidTiles(),
                lineOfSight)) {
            transition(EnumEvents.CAN_ATTACK);
        } else if (!target.isDead() && monster.isTargetInLineOfSight(monster.getLevel().getSolidTiles(),
                lineOfSight)) {
                transition(EnumEvents.AGGROED);
        }

        if (monster.getHealth() <= 0 && !monster.isDead()) {
            monster.die();
            transition(EnumEvents.KILLED);
        }

        if (monster.isHurt()) {
            transition(EnumEvents.AGGROED);
            monster.setHurt(false);
        }

        if (target.isDead()) {
            transition(EnumEvents.TARGET_KILLED);
        }
        switch (this.currentState) {
            case IDLE:
                idle();
                break;
            case CHASE:
                chase();
                break;
            case TRY_ATTACK:
                attack();
                break;
            case DEAD:
                break;
            default:
                break;
        }
    }

    public void idle() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void attack();

    public void chase() {
        try {
            if (target == null) return;

            // Occasionally decide to move randomly
            if (randomMovementAccumulator >= movementChangeFrequency) {
                shouldMoveRandomly = rngGenerator.nextFloat(0, 1) <= 0.2;
                randomMovementAccumulator = 0;
            } else {
                randomMovementAccumulator++;
            }

            // Compute directional delta
            double dx = (target.getCharPosx() - monster.getCharPosx()) * monster.getVelocity();
            double dy = (target.getCharPosy() - monster.getCharPosy()) * monster.getVelocity();

            // Invert direction if doing random movement
            if (shouldMoveRandomly) {
                dx *= -1;
                dy *= -1;
            }

            // Predict next position
            double nextX = monster.getCharPosx() + dx;
            double nextY = monster.getCharPosy() + dy;

            // Check for collisions ahead of moving
            boolean willCollide = monster.detectCollisionWithNodesPropsAndMonsters(
                    monster.getLevel().getSolidTiles(), nextX, nextY);

            if (!willCollide) {
                // Safe to move
                monster.setCharPosx(nextX);
                monster.setCharPosy(nextY);
            } else {
                // Invert movement direction and attempt again
                double altDx = -dx;
                double altDy = -dy;
                double altX = monster.getCharPosx() + altDx;
                double altY = monster.getCharPosy() + altDy;

                if (!monster.detectCollisionWithNodesPropsAndMonsters(monster.getLevel().getSolidTiles(), altX, altY)) {
                    // Move in opposite direction
                    monster.setCharPosx(altX);
                    monster.setCharPosy(altY);
                }

                // Flip random behavior more often after a block
                shouldMoveRandomly = rngGenerator.nextFloat(0, 1) <= 0.5;
                randomMovementAccumulator = movementChangeFrequency - 30;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
