package rpg.Monsters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.Thing;
import rpg.Common.Usable;
import rpg.Levels.LevelNode;

public class Bringer extends BaseMonster implements EnemyIA {
  private String name;
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private List<StateTransition> transitionTable = new ArrayList<>();
  private EnumEnemyStates currentState = EnumEnemyStates.IDLE;
  private BaseMonster target;
  private List<Thing> things;
  private List<LevelNode> solidTiles;
  private List<LevelNode> aggroList;
  private Random rngGenerator = new Random();
  private boolean shouldMoveRandomly = false;
  private int randomMovementAccumulator = 0;
  private int randomAttackAccumulator = 0;

  private final int movementChangeFrequency = 30;
  private final int attackCoolDown = 12;

  double attackRange = 500.0;

  public Bringer(double charPosx, double charPosy, double velocity, int health,
      int shield, String name, EnumEnemyStates currentState, List<Thing> things, List<LevelNode> solidTiles) {

    super(charPosx, charPosy, velocity, health, alignment);
    this.name = name;
    this.currentState = currentState;
    this.things = things;
    this.solidTiles = solidTiles;

    aggroList = things.stream()
        .filter(v -> v.getMonster().getAlignment() == EnumMonsterAlignment.PLAYER).map(v -> {
          setTarget(v.getMonster());
          return v.getMonster().getImageView();
        }).collect(Collectors.toList());

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/bringer-of-death/bringer.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 140, 93));

    setAnimation(new SpriteAnimation(imageView, new Duration(600), 7, 7,
        0, 0, 140, 93));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);

    // Set transition table for this monster
    transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.AGGROED, EnumEnemyStates.CHASE));
    transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.DAMAGED, EnumEnemyStates.STUN));
    transitionTable.add(new StateTransition(EnumEnemyStates.CHASE, EnumEvents.KILLED, EnumEnemyStates.DEAD));
    transitionTable.add(new StateTransition(EnumEnemyStates.IDLE, EnumEvents.KILLED, EnumEnemyStates.DEAD));
  }

  public void setTarget(BaseMonster target) {
    this.target = target;
  }

  @Override
  public void update(List<Usable> usables) {
    if (detectCollision(aggroList)) {
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

  @Override
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
  public EnumEnemyStates getCurrentState() {
    return null;
  }

  public void idle() {
    try {
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean checkMonsterInAttackRange(BaseMonster target) {
    double currentMonsterDistance = Math
        .sqrt(Math.pow(charPosx - target.charPosx, 2) + Math.pow(charPosy - target.charPosy, 3));
    return currentMonsterDistance <= attackRange;

  }

  public void attack(BaseMonster target) {
    if (!checkMonsterInAttackRange(target)) {
      return;
    }
    if (randomAttackAccumulator == attackCoolDown) {
      System.out.println("pew pew!");
      randomAttackAccumulator = 0;
    } else {
      randomAttackAccumulator++;
    }
  }

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
        charPosx -= (target.charPosx - charPosx) * 0.01;
        charPosy -= (target.charPosy - charPosy) * 0.01;
      } else {
        charPosx += (target.charPosx - charPosx) * 0.01;
        charPosy += (target.charPosy - charPosy) * 0.01;
      }

      imageView.setLayoutX(charPosx);
      imageView.setLayoutY(charPosy);

      if (detectCollision(solidTiles)) {
        if (shouldMoveRandomly) {
          charPosx += (target.charPosx - charPosx) * 0.01;
          charPosy += (target.charPosy - charPosy) * 0.01;
        } else {
          charPosx -= (target.charPosx - charPosx) * 0.01;
          charPosy -= (target.charPosy - charPosy) * 0.01;
        }
        imageView.setLayoutX(charPosx);
        imageView.setLayoutY(charPosy);
        return;
      }

      attack(target);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
