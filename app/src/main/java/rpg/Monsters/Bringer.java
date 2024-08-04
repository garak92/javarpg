package rpg.Monsters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.Thing;
import rpg.Common.Usable;
import rpg.Levels.LevelNode;
import rpg.Monsters.Player;

public class Bringer extends BaseMonster implements EnemyIA {
  private String name;
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private List<StateTransition> transitionTable = new ArrayList<>();
  private EnumEnemyStates currentState = EnumEnemyStates.IDLE;
  private BaseMonster target;
  private List<Thing> things;
  private List<LevelNode> solidTiles;
  private List<LevelNode> aggroList;

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

  public void chase() {
    try {
      if (target == null) {
        return;
      }

      charPosx += (target.charPosx - charPosx) * 0.01;
      charPosy += (target.charPosy - charPosy) * 0.01;
      imageView.setLayoutX(charPosx);
      imageView.setLayoutY(charPosy);

      if (detectCollision(solidTiles)) {
        charPosx -= (target.charPosx - charPosx) * 0.01;
        charPosy -= (target.charPosy - charPosy) * 0.01;
        imageView.setLayoutX(charPosx);
        imageView.setLayoutY(charPosy);
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
