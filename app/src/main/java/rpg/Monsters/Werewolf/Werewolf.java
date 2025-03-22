package rpg.Monsters.Werewolf;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.Common.QuestLog;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Monsters.*;
import rpg.SpriteAnimation;

import java.util.HashMap;
import java.util.List;

public class Werewolf extends BaseMonster {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.ENEMY;
  private final EnemyAI ai = new WerewolfAI(this);

  public Werewolf(double charPosx, double charPosy, double velocity, int health,
                  int shield, String name, Level level) {

    super(charPosx, charPosy, velocity, health, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/enemies/werewolf/black_werewolf/Idle.png");
        put("dead", "/enemies/werewolf/black_werewolf/Dead.png");
        put("walk", "/enemies/werewolf/black_werewolf/Walk.png");
        put("attack", "/enemies/werewolf/black_werewolf/Charge.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 140, 93));

    setAnimation(new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);
  }

  @Override
  public void die() {
    getMonster().setDead();

    getImageView().setImage(images.get("dead"));
    setAnimationWithoutPlaying(new SpriteAnimation(imageView, new Duration(200), 2, 2, 0, 0, 128, 160));
    MonsterUtils.playAnimationOnlyOnce(animation);

    QuestLog.INSTANCE.updateActiveQuests(this);
  }

  @Override
  public void update(List<Usable> usables) {
    if(ai.currentState() == EnumEnemyStates.CHASE) {
      if(getImageView().getImage() != images.get("walk")) {
        getImageView().setImage(images.get("walk"));
      }
    }

    if(ai.currentState() == EnumEnemyStates.CHASE && ai.isAttacking()) {
      getImageView().setImage(images.get("attack"));
    }

    if(ai.currentState() == EnumEnemyStates.IDLE) {
      getImageView().setImage(images.get("idle"));
    }

    ai.update(usables);
  }

}
