package rpg.Monsters;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import rpg.Levels.LevelNode;
import rpg.Levels.NodeTypeEnum;
import rpg.SpriteAnimation;
import rpg.Common.Thing;

public abstract class BaseMonster implements Thing {
  protected LevelNode imageView = new LevelNode(NodeTypeEnum.MONSTER);
  protected Map<String, Image> images = new HashMap<>();
  protected Animation animation;
  protected EnumMonsterAlignment alignment;
  protected int health;
  protected double charPosx;
  protected double charPosy;
  protected double charVelx;
  protected double charVely;

  public LevelNode getImageView() {
    return imageView;
  }

  public void spawn(Pane root) {
    root.getChildren().add(this.imageView);
  }

  protected BaseMonster(double charPosx, double charPosy, double velocity, int health,
      EnumMonsterAlignment alignment) {
    this.charPosx = charPosx;
    this.charPosy = charPosy;
    this.charVelx = velocity;
    this.charVely = velocity;
    this.health = health;
    this.alignment = alignment;
  }

  protected void preCacheSprites(Map<String, String> sprites) {
    for (Map.Entry<String, String> i : sprites.entrySet()) {
      try (InputStream stream = this.getClass().getResourceAsStream("/sprites/" + i.getValue())) {
        Image image = new Image(stream);
        images.put(i.getKey(), image);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  protected void setAnimation(SpriteAnimation animation) {
    this.animation = animation;
    animation.setCycleCount(Animation.INDEFINITE);
    animation.play();
  }

  protected boolean detectCollision(List<LevelNode> nodeList) {
    // We take 20% of the entity's dimensions for a nicer feel
    double boundingBoxHeight = this.imageView.getBoundsInParent().getHeight() * 20 / 100;
    double boundingBoxWidth = this.imageView.getBoundsInParent().getWidth() * 20 / 100;
    for (Node b : nodeList) {
      if (b.getBoundsInParent().intersects(this.imageView.getBoundsInParent().getCenterX(),
          this.imageView.getBoundsInParent().getCenterY(),
          boundingBoxWidth, boundingBoxHeight)) {
        return true;
      }
    }
    return false;
  }

}
