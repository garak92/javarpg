package rpg.Monsters.Igrene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Levels.LevelNode;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.QuestGiver;
import rpg.Monsters.EnumEnemyStates;
import rpg.Monsters.EnumMonsterAlignment;
import rpg.Monsters.Player;

public class Igrene extends BaseMonster implements Usable {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.FRIEND;
  private QuestGiver questGiver;
  private static Igrene instance;
  private Level level;
  private final String[] defaultDialogueList = { "This level is cool, uh?", "Long live the king!",
      "For the Allia...oh, wrong game, sorry" };

  private Igrene(
      double charPosx,
      double charPosy,
      double velocity,
      int health,
      int shield, String name,
      EnumEnemyStates currentState, Level level) {
    super(charPosx, charPosy, velocity, health, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/npc/Idle.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 160, 128));
    setAnimation(new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);

    this.questGiver = new QuestGiver(this, defaultDialogueList);
  }

  public static Igrene initialize(double charPosx,
      double charPosy,
      double velocity,
      int health,
      int shield, String name,
      EnumEnemyStates currentState, Level level) {
    if (instance == null) {
      instance = new Igrene(charPosx, charPosy, velocity, health,
          shield, name, currentState, level);
    } else {
      // Player should maintain their stats throughout a level transition
      // but other stuff, such as the level data, must obviously change
      instance.setCharPosx(charPosx);
      instance.setCharPosy(charPosy);
      instance.level = level;
      instance.getImageView().setLayoutX(charPosx);
      instance.getImageView().setLayoutY(charPosy);
    }
    return instance;
  }

  @Override
  public void die() {
    // level.removeThing(this);
  }

  @Override
  public void update(List<Usable> usables) {
  }

  @Override
  public void use(Player player) {
    questGiver.use(player);
  }

  @Override
  public LevelNode getLevelNode() {
    return this.imageView;
  }

  @Override
  public BaseMonster getBaseMonster() {
    return this;
  }
}
