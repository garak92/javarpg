package rpg.Monsters;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import rpg.Levels.Level;
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
  protected Level level;
  protected static Logger logger = LoggerFactory.getLogger(BaseMonster.class);

  public LevelNode getImageView() {
    return imageView;
  }

  public void spawn(Pane root) {
    root.getChildren().add(this.imageView);
  }

  public EnumMonsterAlignment getAlignment() {
    return alignment;
  }

  protected BaseMonster(double charPosx, double charPosy, double velocity, int health,
      EnumMonsterAlignment alignment, Level level) {
    this.charPosx = charPosx;
    this.charPosy = charPosy;
    this.charVelx = velocity;
    this.charVely = velocity;
    this.health = health;
    this.alignment = alignment;
    this.level = level;
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

  public boolean detectCollision(BaseMonster monster) {
    // We take 20% of the entity's dimensions for a nicer feel
    LevelNode monsterNode = monster.getImageView();
    double boundingBoxHeight = this.imageView.getBoundsInParent().getHeight() * 20 / 100;
    double boundingBoxWidth = this.imageView.getBoundsInParent().getWidth() * 20 / 100;
    if (monsterNode.getBoundsInParent().intersects(this.imageView.getBoundsInParent().getCenterX(),
        this.imageView.getBoundsInParent().getCenterY(),
        boundingBoxWidth, boundingBoxHeight)) {
      return true;
    }
    return false;
  }

  public void receiveDamage(int damage) {
    if (health > 0) {
      this.health -= damage;
    }
  }

  public BaseMonster getMonster() {
    return this;
  }

  public Level getLevel() {
    return level;
  }

  public double getCharPosx() {
    return charPosx;
  }

  public double getCharPosy() {
    return charPosy;
  }

}
