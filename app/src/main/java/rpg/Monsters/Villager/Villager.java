package rpg.Monsters.Villager;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.SpriteAnimation;
import rpg.Common.CyclicDialogBox;
import rpg.Common.Usable;
import rpg.Levels.Level;
import rpg.Levels.LevelNode;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumEnemyStates;
import rpg.Monsters.EnumMonsterAlignment;
import rpg.Monsters.Player;

public class Villager extends BaseMonster implements Usable {
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.FRIEND;
  private CyclicDialogBox dialogBox;
  private final Random random = new Random();
  private final String[] defaultDialogueList = { "Hello, are you an adventurer?", "Sorry, I'm busy right now",
      "Can't believe I've lost my money at the horse race again...",
      "Ever since the evil lord came, its getting harder and harder to get a job",
      "Will you have the courage to defeat the evil lord?", "I hope the king is all right",
      "I don't talk to strangers" };
  private final String[] types = { "oldMan", "woman", "boy" };
  private final String type;
  private boolean shouldMoveRandomly = false;
  private boolean shouldWalkX = false;
  private boolean shouldWalkY = false;
  private int randomMovementAccumulator = 0;
  int velocity = 0;
  private final int movementChangeFrequency = 30;

  public Villager(
      double charPosx,
      double charPosy,
      double velocity,
      int health,
      int shield, String name,
      EnumEnemyStates currentState, Level level) {
    super(charPosx, charPosy, velocity, health, alignment, level, name);

    preCacheSprites(new HashMap<String, String>() {
      {
        put("oldManIdle", "/npc/villagers/oldman/Idle.png");
        put("oldManWalk", "/npc/villagers/oldman/Walk.png");
        put("womanIdle", "/npc/villagers/woman/Idle.png");
        put("womanWalk", "/npc/villagers/woman/Walk.png");
        put("boyIdle", "/npc/villagers/boy/Idle.png");
        put("boyWalk", "/npc/villagers/boy/Walk.png");
      }
    });

    this.type = types[random.nextInt(types.length)];

    getImageView().setImage(images.get(this.type + "Idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 48, 48));
    setAnimation(new SpriteAnimation(imageView, new Duration(800), 4, 4, -2, 0, 48, 48));

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);
    getImageView().setFitWidth(80);
    getImageView().setFitHeight(80);

    this.dialogBox = new CyclicDialogBox(level.getPane(), this, defaultDialogueList);
  }

  @Override
  public void die() {
    // level.removeThing(this);
  }

  @Override
  public void update(List<Usable> usables) {
    if (dialogBox.getState()) { // Don't walk and talk at the same time
      getImageView().setImage(images.get(this.type + "Idle"));
      return;
    }
    if (randomMovementAccumulator == movementChangeFrequency) {
      shouldMoveRandomly = random.nextFloat(0, 1) <= 0.4;
      shouldWalkX = random.nextFloat(0, 1) <= 0.2;
      shouldWalkY = random.nextFloat(0, 1) <= 0.2;
      randomMovementAccumulator = 0;
    } else {
      randomMovementAccumulator++;
    }

    if (shouldMoveRandomly) {
      charPosx -= shouldWalkX ? charVelx : 0;
      charPosy -= shouldWalkY ? charVelx : 0;
      if (shouldWalkX) {
        getImageView().setImage(images.get(this.type + "Walk"));
        imageView.setScaleX(-1);
      } else {
        getImageView().setImage(images.get(this.type + "Idle"));
      }
    } else {
      charPosx += shouldWalkX ? charVely : 0;
      charPosy += shouldWalkY ? charVely : 0;
      if (shouldWalkX) {
        getImageView().setImage(images.get(this.type + "Walk"));
        imageView.setScaleX(1);
      } else {
        getImageView().setImage(images.get(this.type + "Idle"));
      }
    }

    imageView.setLayoutX(charPosx);
    imageView.setLayoutY(charPosy);

    if (detectCollision(getLevel().getSolidTiles())) {
      if (shouldMoveRandomly) {
        charPosx += charVelx;
        charPosy += charVely;
      } else {
        charPosx -= charVelx;
        charPosy -= charVely;
      }
      imageView.setLayoutX(charPosx);
      imageView.setLayoutY(charPosy);
      return;
    }

  }

  @Override
  public void use(Player player) {
    dialogBox.use();
  }

  @Override
  public LevelNode getLevelNode() {
    return this.imageView;
  }
}
