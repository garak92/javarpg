package rpg.Monsters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import rpg.Common.Usable;
import rpg.Levels.LevelNode;

public abstract class BaseEnemyAI extends EnemyAI {
  private List<StateTransition> transitionTable = new ArrayList<>();
  private EnumEnemyStates currentState = EnumEnemyStates.IDLE;
  private List<LevelNode> aggroList;
  private Random rngGenerator = new Random();
  private boolean shouldMoveRandomly = false;
  private int randomMovementAccumulator = 0;
  private BaseMonster target;
  private final int movementChangeFrequency = 30;
  protected double attackRange = 500.0;

  public BaseEnemyAI(BaseMonster monster) {
    super(monster);

    // Set default transition table
    transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.AGGROED, EnumEnemyStates.CHASE));
    transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.DAMAGED, EnumEnemyStates.STUN));
    transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.KILLED, EnumEnemyStates.DEAD));
    transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.KILLED, EnumEnemyStates.DEAD));

    aggroList = monster.things.stream() // Just pass the player here if infighting won't be a mechanic, we'll see
        .filter(v -> v.getMonster().getAlignment() == EnumMonsterAlignment.PLAYER).map(v -> {
          setTarget(v.getMonster());
          return v.getMonster().getImageView();
        }).collect(Collectors.toList());
  }

  public void setTarget(BaseMonster target) {
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

  public boolean checkMonsterInAttackRange(BaseMonster target) {
    double currentMonsterDistance = Math
        .sqrt(Math.pow(monster.charPosx - target.charPosx, 2) + Math.pow(monster.charPosy - target.charPosy, 3));
    return currentMonsterDistance <= attackRange;

  }

  @Override
  public void update(List<Usable> usables) {
    if (monster.detectCollision(aggroList)) {
      transition(EnumEvents.AGGROED);
    }
    switch (this.currentState) {
      case IDLE:
        idle();
        break;
      case CHASE:
        chase();
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

  public abstract void attack(BaseMonster target);

  public void chase() {
    try {
      if (target == null) {
        return;
      }

      if (randomMovementAccumulator == movementChangeFrequency) {
        shouldMoveRandomly = rngGenerator.nextFloat(0, 1) <= 0.4;
        randomMovementAccumulator = 0;
      } else {
        randomMovementAccumulator++;
      }

      if (shouldMoveRandomly) {
        monster.charPosx -= (target.charPosx - monster.charPosx) * 0.01;
        monster.charPosy -= (target.charPosy - monster.charPosy) * 0.01;
      } else {
        monster.charPosx += (target.charPosx - monster.charPosx) * 0.01;
        monster.charPosy += (target.charPosy - monster.charPosy) * 0.01;
      }

      monster.imageView.setLayoutX(monster.charPosx);
      monster.imageView.setLayoutY(monster.charPosy);

      if (monster.detectCollision(monster.solidTiles)) {
        if (shouldMoveRandomly) {
          monster.charPosx += (target.charPosx - monster.charPosx) * 0.01;
          monster.charPosy += (target.charPosy - monster.charPosy) * 0.01;
        } else {
          monster.charPosx -= (target.charPosx - monster.charPosx) * 0.01;
          monster.charPosy -= (target.charPosy - monster.charPosy) * 0.01;
        }
        monster.imageView.setLayoutX(monster.charPosx);
        monster.imageView.setLayoutY(monster.charPosy);
        return;
      }

      if (checkMonsterInAttackRange(target)) {
        attack(target);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
