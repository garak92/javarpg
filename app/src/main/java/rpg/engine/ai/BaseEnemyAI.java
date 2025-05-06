package rpg.engine.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rpg.engine.common.Usable;
import rpg.engine.monster.BaseMonster;

public abstract class BaseEnemyAI extends EnemyAI {
  private List<StateTransition> transitionTable = new ArrayList<>();
  private EnumEnemyStates currentState = EnumEnemyStates.IDLE;
  private Random rngGenerator = new Random();
  private boolean shouldMoveRandomly = false;
  protected boolean isAttacking = false;
  protected boolean isPerformingAction = false;
  Line lineOfSight =  new Line();
  private int randomMovementAccumulator = 0;
  private BaseMonster target;
  private final int movementChangeFrequency = 200;
  protected double attackRange = 500.0;
  protected static Logger logger = LoggerFactory.getLogger(BaseEnemyAI.class);

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
    transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // From ATTACK to CHASE if AGGROED
    transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.FINISH_ATTACK, EnumEnemyStates.CHASE));  // From ATTACK to CHASE if AGGROED
    transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.KILLED, EnumEnemyStates.DEAD));  // From ATTACK to DEAD when KILLED
    transitionTable.add(new StateTransition(EnumEnemyStates.ATTACK, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // Target KILLED in ATTACK -> back to IDLE

// TRY_ATTACK state transitions
    transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.CAST_ATTACK, EnumEnemyStates.ATTACK));  // From TRY_ATTACK to ATTACK when CAST_ATTACK
    transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.FINISH_ATTACK, EnumEnemyStates.CHASE));  // Finish ATTACK -> back to CHASE
    transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.AGGROED, EnumEnemyStates.CHASE));  // From TRY_ATTACK to CHASE if AGGROED
    transitionTable.add(new StateTransition(EnumEnemyStates.TRY_ATTACK, EnumEvents.TARGET_KILLED, EnumEnemyStates.IDLE));  // Target KILLED in TRY_ATTACK -> IDLE

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

  protected EnumEnemyStates getCurrentState() {
    return currentState;
  }

  @Override
  public void update(List<Usable> usables) {
    if(monster.isDead() && currentState() != EnumEnemyStates.DEAD) {
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
            && monster.isTargeInLineOfSight(monster.getLevel().getSolidTiles(),
            lineOfSight)) {
      transition(EnumEvents.CAN_ATTACK);
    } else if(!target.isDead() && monster.isTargeInLineOfSight(monster.getLevel().getSolidTiles(),
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
        //chase();
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
      if (target == null) {
        return;
      }

      if (randomMovementAccumulator >= movementChangeFrequency) {
        shouldMoveRandomly = rngGenerator.nextFloat(0, 1) <= 0.2;
        randomMovementAccumulator = 0;
      } else {
        randomMovementAccumulator++;
      }

      if (shouldMoveRandomly) {
        monster.setCharPosx(monster.getCharPosx()  -  (target.getCharPosx() - monster.getCharPosx()) * monster.getVelocity());
        monster.setCharPosy(monster.getCharPosy()  -  (target.getCharPosy() - monster.getCharPosy()) * monster.getVelocity());
      } else {
        monster.setCharPosx(monster.getCharPosx() + (target.getCharPosx() - monster.getCharPosx()) * monster.getVelocity());
        monster.setCharPosy(monster.getCharPosy() + (target.getCharPosy() - monster.getCharPosy()) * monster.getVelocity());
      }

      if (monster.detectCollision(monster.getLevel().getSolidTiles(), monster.getCharPosx(), monster.getCharPosy())) {
        if (shouldMoveRandomly) {
          monster.setCharPosx(monster.getCharPosx() + (target.getCharPosx() - monster.getCharPosx()) * monster.getVelocity());
          monster.setCharPosy(monster.getCharPosy() + (target.getCharPosy() - monster.getCharPosy()) * monster.getVelocity());
        } else {
          monster.setCharPosx(monster.getCharPosx()  -  (target.getCharPosx() - monster.getCharPosx()) * monster.getVelocity());
          monster.setCharPosy(monster.getCharPosy()  -  (target.getCharPosy() - monster.getCharPosy()) * monster.getVelocity());
        }
        shouldMoveRandomly = rngGenerator.nextFloat(0, 1) <= 0.5;
        randomMovementAccumulator = movementChangeFrequency - 30;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
