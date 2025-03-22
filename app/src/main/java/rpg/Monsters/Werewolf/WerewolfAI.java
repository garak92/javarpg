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
      isAttacking = true;
      attackLogic();
      randomAttackAccumulator = 0;
    } else {
      isAttacking = false;
      randomAttackAccumulator++;
    }
  }

    public void attackLogic() {
    monster.getAnimation().stop();
    monster.getAnimation().setCycleCount(1);
    monster.getAnimation().setOnFinished(event -> {
      isAttacking = false;
      if(!monster.isDead()) {
        monster.getAnimation().stop();
        monster.getAnimation().setCycleCount(Animation.INDEFINITE);
        monster.getAnimation().play();
      }
    });

    monster.getAnimation().play();
  }

  @Override
  public EnumEnemyStates currentState() {
    return super.getCurrentState();
  }

}
