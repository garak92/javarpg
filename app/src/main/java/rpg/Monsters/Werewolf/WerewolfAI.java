package rpg.Monsters.Werewolf;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;
import rpg.Monsters.*;
import rpg.Monsters.Satyrs.MaleSatyr.MaleSatyrFireball;

public class WerewolfAI extends BaseEnemyAI {
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 20;

  public WerewolfAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 300;
  }

  @Override
  public void attack() {
    if (randomAttackAccumulator == attackCoolDown) {
      transition(EnumEvents.CAST_ATTACK);
      randomAttackAccumulator = 0;
    } else {
      transition(EnumEvents.FINISH_ATTACK);
      randomAttackAccumulator++;
    }
  }

  @Override
  public EnumEnemyStates currentState() {
    return super.getCurrentState();
  }


}
