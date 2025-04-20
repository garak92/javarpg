package rpg.Monsters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import rpg.Common.cli.CommandLine;
import rpg.Game;
import rpg.Levels.Level;
import rpg.Levels.LevelNode;
import rpg.SpriteAnimation;
import rpg.Common.PlayerStatusBar;
import rpg.Common.Usable;

public class Player extends BaseMonster {
  private int shield;
  private String name;
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PLAYER;
  private boolean iceBallAttack = false;
  private boolean moveRight = false;
  private boolean moveLeft = false;
  private boolean moveUp = false;
  private boolean moveDown = false;
  private boolean using = false;
  private int attackAccumulator = 30;
  private List<LevelNode> solidTiles;
  private CopyOnWriteArrayList<Usable> usedEntities = new CopyOnWriteArrayList<>();
  private Pane root;
  private int experiencePoints = 0;
  private int playerLevel = 0;
  private PlayerStatusBar statusBar;
  private static Player instance;

  private Player(double charPosx, double charPosy, double velocity, int health,
      int shield, String name, Stage primaryStage, Pane root, Level level) {
    super(charPosx, charPosy, velocity, health, alignment, level, name);
    this.shield = shield;
    this.name = name;
    this.root = root;
    this.solidTiles = level.getSolidTiles();

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/player/Idle.png");
        put("run", "/player/Run.png");
        put("dead", "/player/Dead.png");
        put("attack", "/player/Attack_4.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 160, 128));

    setAnimation(new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160));

    setKeyBinds(primaryStage);

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);

    this.statusBar = new PlayerStatusBar(0, 0, root, this);

  }

  public static Player initialize(double charPosx, double charPosy, double velocity, int health,
      int shield, String name, Stage primaryStage, Pane root, Level level) {
    if (instance == null) {
      instance = new Player(charPosx, charPosy, velocity, health,
          shield, name, primaryStage, root, level);
    } else {
      // Player should maintain their stats throughout a level transition
      // but other stuff, such as the level data, must obviously change
      instance.setCharPosx(charPosx);
      instance.setCharPosy(charPosy);
      instance.setLevel(level);
      instance.setStatusBar(new PlayerStatusBar(0, 0, root, instance));
      instance.getImageView().setLayoutX(charPosx);
      instance.getImageView().setLayoutY(charPosy);
    }
    return instance;
  }

  public static Player getInstance() throws NullPointerException {
    if(instance == null) {
     throw new NullPointerException();
    }
     return instance;
  }

  private void setKeyBinds(Stage primaryStage) {
    primaryStage.getScene().setOnKeyPressed(event -> {
      // Movement
      KeyCode keyCode = event.getCode();
      if (keyCode == KeyCode.RIGHT) {
        moveRight = true;
      }
      if (keyCode == KeyCode.LEFT) {
        moveLeft = true;
      }
      if (keyCode == KeyCode.UP) {
        moveUp = true;
      }
      if (keyCode == KeyCode.DOWN) {
        moveDown = true;
      }
      if (keyCode == KeyCode.E) {
        using = true;
      }

      if (keyCode == KeyCode.C) {
          try {
              CommandLine.INSTANCE.activate();
          } catch (Throwable e) {
            throw new RuntimeException(e);
          }
      }

      // Attack
      if (keyCode == KeyCode.R) {
        iceBallAttack = true;
      }
    });

    primaryStage.getScene().setOnKeyReleased(event -> {
      KeyCode keyCode = event.getCode();
      // Movement
      if (keyCode == KeyCode.RIGHT) {
        moveRight = false;
      }
      if (keyCode == KeyCode.LEFT) {
        moveLeft = false;
      }
      if (keyCode == KeyCode.UP) {
        moveUp = false;
      }
      if (keyCode == KeyCode.DOWN) {
        moveDown = false;
      }

      // Attack
      if (keyCode == KeyCode.R) {
        iceBallAttack = false;
        attackAccumulator = 30;
      }
    });
  }

  public void interact(List<Usable> usables) {
    double boundingBoxHeight = this.imageView.getBoundsInParent().getHeight() * 20 / 100;
    double boundingBoxWidth = this.imageView.getBoundsInParent().getWidth() * 20 / 100;
    for (Usable b : usables) {
      if (b.getLevelNode().getBoundsInParent().intersects(this.imageView.getBoundsInParent().getCenterX(),
          this.imageView.getBoundsInParent().getCenterY(),
          boundingBoxWidth, boundingBoxHeight)) {
        if (!usedEntities.contains(b)) { // Don't use if already used
          b.use(this);
          if (b.getBaseMonster().getAlignment() == EnumMonsterAlignment.FRIEND) {
            usedEntities.add(b);
          }
        }

      } else {
        using = false;
      }
    }
  }

  public void stopInteraction() {
    double boundingBoxHeight = this.imageView.getBoundsInParent().getHeight() * 20 / 100;
    double boundingBoxWidth = this.imageView.getBoundsInParent().getWidth() * 20 / 100;
    for (Usable b : usedEntities) {
      if (!b.getLevelNode().getBoundsInParent().intersects(this.imageView.getBoundsInParent().getCenterX(),
          this.imageView.getBoundsInParent().getCenterY(),
          boundingBoxWidth, boundingBoxHeight)) {
        b.use(this);
        usedEntities.remove(b);
      }
    }

  }

  public void iceBallAttack() {
    if (attackAccumulator < 30) {
      attackAccumulator++;
      return;
    }
    iceBallAttack = true;
    this.getLevel()
        .addThing(new PlayerIceBall(imageView.getBoundsInParent().getCenterX(),
            imageView.getBoundsInParent().getCenterY(), level, level.getEnemies(), imageView.getScaleX()));

    iceBallAttack = true;
    attackAccumulator = 0;
  }

  @Override
  public void die() {
    imageView.setImage(images.get("dead"));
    isDead = true;

    MonsterUtils.playAnimationOnlyOnce(animation);
  }

  public void resurrect() {
    isDead = false;
    health = 100;
    setAnimation(new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160));
    animation.play();
  }

  @Override
  public void update(List<Usable> usables) {
    double cameraX = imageView.getLayoutX() - root.getScene().getWidth() / 2;
    double cameraY = imageView.getLayoutY() - root.getScene().getHeight() / 2;
    this.statusBar.update(cameraX + 10, cameraY + 10);

    if (health <= 0) {
      if (isDead) {
        return;
      }
      die();
      return;
    }

    if (iceBallAttack) {
      iceBallAttack();
    }

    if (moveRight) {
      imageView.setScaleX(1);
      imageView.setImage(images.get("run"));
      charPosx += velocity;
      imageView.setLayoutX(charPosx);
      if (detectCollision(solidTiles)) {
        charPosx -= velocity;
        imageView.setLayoutX(charPosx);
        return;
      }
    } else if (moveLeft) {
      imageView.setScaleX(-1);
      imageView.setImage(images.get("run"));
      charPosx -= velocity;
      imageView.setLayoutX(charPosx);
      if (detectCollision(solidTiles)) {
        charPosx += velocity;
        imageView.setLayoutX(charPosx);
        return;
      }
    } else if (moveUp) {
      imageView.setImage(images.get("run"));
      charPosy -= velocity;
      imageView.setLayoutY(charPosy);
      if (detectCollision(solidTiles)) {
        charPosy += velocity;
        imageView.setLayoutY(charPosy);
        return;
      }
    } else if (moveDown) {
      imageView.setImage(images.get("run"));
      charPosy += velocity;
      imageView.setLayoutY(charPosy);
      if (detectCollision(solidTiles)) {
        charPosy -= velocity;
        imageView.setLayoutY(charPosy);
        return;
      }

    } else if (iceBallAttack) {
      imageView.setImage(images.get("attack"));
    } else {
      imageView.setImage(images.get("idle"));
    }

    if (using) {
      interact(usables);
    }

    stopInteraction();

    root.setLayoutX(-cameraX);
    root.setLayoutY(-cameraY);

  }

  public void setPlayerLevel(int playerLevel) {
    this.playerLevel = playerLevel;
  }

  public void addExperiencePoints(int experiencePoints) {
    this.experiencePoints += experiencePoints;
  }

  public int getExperiencePoints() {
    return experiencePoints;
  }

  private void setLevel(Level level) {
    this.level = level;
    this.solidTiles = level.getSolidTiles();
  }

  public void setStatusBar(PlayerStatusBar statusBar) {
    this.statusBar = statusBar;
  }

  public void heal(int healingValue) {
    health += healingValue;
  }

}
