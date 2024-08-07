package rpg.Monsters;

import java.util.HashMap;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import rpg.Levels.LevelNode;
import rpg.SpriteAnimation;
import rpg.Common.Thing;
import rpg.Common.Usable;

public class Player extends BaseMonster {
  private int shield;
  private String name;
  private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PLAYER;
  private boolean moveRight = false;
  private boolean moveLeft = false;
  private boolean moveUp = false;
  private boolean moveDown = false;
  private boolean using = false;
  private List<LevelNode> solidTiles;
  private Pane root;

  public Player(double charPosx, double charPosy, double velocity, int health,
      int shield, String name, Stage primaryStage, List<LevelNode> solidTiles, Pane root, List<Thing> things) {
    super(charPosx, charPosy, velocity, health, alignment, things, solidTiles);
    this.shield = shield;
    this.name = name;
    this.root = root;
    this.solidTiles = solidTiles;

    preCacheSprites(new HashMap<String, String>() {
      {
        put("idle", "/player/Idle.png");
        put("run", "/player/Run.png");
      }
    });

    getImageView().setImage(images.get("idle"));
    getImageView().setViewport(new Rectangle2D(charPosx, charPosy, 160, 128));

    setAnimation(new SpriteAnimation(imageView, new Duration(300), 4, 4, 0, 0, 128, 160));

    setKeyBinds(primaryStage);

    getImageView().setLayoutX(charPosx);
    getImageView().setLayoutY(charPosy);

  }

  private void setKeyBinds(Stage primaryStage) {
    primaryStage.getScene().setOnKeyPressed(event -> {
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
    });

    primaryStage.getScene().setOnKeyReleased(event -> {
      KeyCode keyCode = event.getCode();
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

  @Override
  public void update(List<Usable> usables) {
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

    } else {
      imageView.setImage(images.get("idle"));
    }

    if (using) {
      interact(usables);
    }

    double cameraX = imageView.getLayoutX() - (double) 300 / 2;
    double cameraY = imageView.getLayoutY() - (double) 300 / 2;

    root.setLayoutX(-cameraX);
    root.setLayoutY(-cameraY);

  }

}
