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
import rpg.Monsters.Quest;

public class Igrene extends BaseMonster implements Usable {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.FRIEND;
  List<Quest> questList = new ArrayList<>();
  String[] dialogChain = { "There is a monster in a cave!" };
  QuestGiver questGiver;

  public Igrene(
      double charPosx,
      double charPosy,
      double velocity,
      int health,
      int shield, String name,
      EnumEnemyStates currentState, Level level) {
    super(charPosx, charPosy, velocity, health, alignment, level, name);

    questList.add(new Quest(dialogChain));
    this.questGiver = new QuestGiver(this, questList);

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
}
