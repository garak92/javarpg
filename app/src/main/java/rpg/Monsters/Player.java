package rpg.Monsters;

import java.util.HashMap;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import rpg.Levels.Level;
import rpg.Levels.LevelNode;
import rpg.Monsters.PlayerIceBall;
import rpg.SpriteAnimation;
import rpg.Abilities.PlayerIceBallAttack;
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
  private Pane root;
  private int experiencePoints = 0;
  private int playerLevel = 0;
  private PlayerStatusBar statusBar;

  public Player(double charPosx, double charPosy, double velocity, int health,
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
        b.use(this);
        using = false;
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
  }

  @Override
  public void update(List<Usable> usables) {
    double cameraX = imageView.getLayoutX() - (double) root.getScene().getWidth() / 2;
    double cameraY = imageView.getLayoutY() - (double) root.getScene().getHeight() / 2;
    this.statusBar.update(cameraX, cameraY);

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
      charPosx += charVelx;
      imageView.setLayoutX(charPosx);
      if (detectCollision(solidTiles)) {
        charPosx -= charVelx;
        imageView.setLayoutX(charPosx);
        return;
      }
    } else if (moveLeft) {
      imageView.setScaleX(-1);
      imageView.setImage(images.get("run"));
      charPosx -= charVelx;
      imageView.setLayoutX(charPosx);
      if (detectCollision(solidTiles)) {
        charPosx += charVelx;
        imageView.setLayoutX(charPosx);
        return;
      }
    } else if (moveUp) {
      imageView.setImage(images.get("run"));
      charPosy -= charVely;
      imageView.setLayoutY(charPosy);
      if (detectCollision(solidTiles)) {
        charPosy += charVely;
        imageView.setLayoutY(charPosy);
        return;
      }
    } else if (moveDown) {
      imageView.setImage(images.get("run"));
      charPosy += charVely;
      imageView.setLayoutY(charPosy);
      if (detectCollision(solidTiles)) {
        charPosy -= charVely;
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
}
