package rpg.Monsters.Satyrs.MaleSatyr;

import javafx.animation.Animation;
import rpg.Monsters.*;

public class MaleSatyrAI extends BaseEnemyAI {
  private int randomAttackAccumulator = 0;
  private final int attackCoolDown = 80;

  public MaleSatyrAI(BaseMonster monster) {
    super(monster, Player.getInstance());
    attackRange = 1000;
  }

  @Override
  public void attack() {
    if (randomAttackAccumulator == attackCoolDown) {
      isAttacking = true;
      attackLogic();
      monster.getLevel()
          .addThing(new MaleSatyrFireball(monster.getImageView().getBoundsInParent().getCenterX(),
              monster.getImageView().getBoundsInParent().getCenterY(), monster.getLevel(), Player.getInstance()));
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
