package rpg.game.entities.player;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import rpg.engine.common.camera.Camera;
import rpg.engine.cli.CommandLine;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.monster.MonsterUtils;
import rpg.engine.levels.Level;
import rpg.engine.levels.LevelNode;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.misc.PlayerStatusBar;
import rpg.engine.common.Usable;

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
  private final Camera camera = new Camera();


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
    double projectedX = 0;
    double projectedY = 0;

    if (health <= 0) {
      if (isDead) {
        this.statusBar.update(camera.getCameraX() + 10, camera.getCameraY() + 20);
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
      projectedX = charPosx + velocity;
      if (detectCollision(solidTiles, projectedX, charPosy)) {
        return;
      }
      charPosx = projectedX;
    } else if (moveLeft) {
      imageView.setScaleX(-1);
      imageView.setImage(images.get("run"));
      projectedX = charPosx - velocity;
      if (detectCollision(solidTiles, projectedX, charPosy)) {
        return;
      }
      charPosx = projectedX;
    } else if (moveUp) {
      imageView.setImage(images.get("run"));
      projectedY = charPosy - velocity;
      if (detectCollision(solidTiles, charPosx, projectedY)) {
        return;
      }
      charPosy = projectedY;
    } else if (moveDown) {
      imageView.setImage(images.get("run"));
      projectedY = charPosy + velocity;
      if (detectCollision(solidTiles, charPosx, projectedY)) {
        return;
      }
      charPosy = projectedY;
    } else if (iceBallAttack) {
      imageView.setImage(images.get("attack"));
    } else {
      imageView.setImage(images.get("idle"));
    }

    if (using) {
      interact(usables);
    }

    stopInteraction();

    camera.updateCamera(this);
    this.statusBar.update(camera.getCameraX() + 10, camera.getCameraY() + 20);
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
