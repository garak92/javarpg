package rpg.Monsters;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rpg.Levels.Level;
import rpg.Levels.LevelNode;
import rpg.Levels.NodeTypeEnum;
import rpg.SpriteAnimation;
import rpg.Common.Thing;

public abstract class BaseMonster implements Thing {
  protected LevelNode imageView = new LevelNode(NodeTypeEnum.MONSTER);
  protected Map<String, Image> images = new HashMap<>();
  protected Animation animation;
  protected Map<String, SpriteAnimation> animations;
  protected EnumMonsterAlignment alignment;
  protected int health;
  protected double charPosx;
  protected double charPosy;
  protected double velocity;
  protected Level level;
  protected boolean isDead = false;
  protected boolean isHurt = false;
  protected String name;
  protected static Logger logger = LoggerFactory.getLogger(BaseMonster.class);

  public abstract void die();

  public LevelNode getImageView() {
    return imageView;
  }

  public void spawn(Pane root) {
    this.imageView.setLayoutX(charPosx);
    this.imageView.setLayoutY(charPosy);

    if(alignment == EnumMonsterAlignment.PLAYER || alignment == EnumMonsterAlignment.ENEMY) {
        imageView.setFitHeight(170);
        imageView.setFitWidth(170);
        imageView.setPreserveRatio(true);
    }

    root.getChildren().add(this.imageView);
  }

  public void deSpawn(Pane root) {
    root.getChildren().remove(this.imageView);
  }

  public EnumMonsterAlignment getAlignment() {
    return alignment;
  }

  public Animation getAnimation() {
    return animation;
  }

  public Map<String, SpriteAnimation> getAnimations() {
        return animations;
  }

    public void setImage(String imageName) {
   imageView.setImage(images.get(imageName));
 }

    protected BaseMonster(double charPosx, double charPosy, double velocity, int health,
                          EnumMonsterAlignment alignment, Level level, String name) {
    this.charPosx = charPosx;
    this.charPosy = charPosy;
    this.velocity = velocity;
    this.health = health;
    this.alignment = alignment;
    this.level = level;
    this.name = name;

  }

  protected BaseMonster(double charPosx, double charPosy, double velocity, int health,
      EnumMonsterAlignment alignment, Level level) {
    this.charPosx = charPosx;
    this.charPosy = charPosy;
    this.velocity = velocity;
    this.health = health;
    this.alignment = alignment;
    this.level = level;
  }

  protected void preCacheSprites(Map<String, String> sprites) {
    for (Map.Entry<String, String> i : sprites.entrySet()) {
      try (InputStream stream = this.getClass().getResourceAsStream("/sprites" + i.getValue())) {
        Image image = new Image(stream);
        images.put(i.getKey(), image);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // Only used with entities with AI, with the purpose of making it easier to match the animation with their state
  protected void preCacheAnimations(Map<String, SpriteAnimation> animations) {
    this.animations = animations;
  }

  public boolean isDead() {
    return this.isDead;
  }

  public void setDead() {
    isDead = true;
  }

  protected void setAnimation(SpriteAnimation animation) {
      this.animation = animation;
      if(animations == null) {
          animation.stop();
          animation.setCycleCount(Animation.INDEFINITE);
          animation.play();
      }

  }

  public boolean detectCollision(List<LevelNode> nodeList) {
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
    // Use this function when you want to allow friendly fire among monsters
    public BaseMonster detectCollisionWithMonsterList(List<BaseMonster> monsterList) {
        // We take 20% of the entity's dimensions for a nicer feel
        double boundingBoxHeight = this.imageView.getBoundsInParent().getHeight() * 20 / 100;
        double boundingBoxWidth = this.imageView.getBoundsInParent().getWidth() * 20 / 100;
        for (BaseMonster b : monsterList) {
            if (b.getImageView().getBoundsInParent().intersects(this.imageView.getBoundsInParent().getCenterX(),
                    this.imageView.getBoundsInParent().getCenterY(),
                    boundingBoxWidth, boundingBoxHeight)) {
                return b;
            }
        }
        return null;
    }


  public boolean detectCollision(BaseMonster monster) {
    // We take 20% of the entity's dimensions for a nicer feel
    LevelNode monsterNode = monster.getImageView();
    double boundingBoxHeight = this.imageView.getBoundsInParent().getHeight() * 20 / 100;
    double boundingBoxWidth = this.imageView.getBoundsInParent().getWidth() * 20 / 100;
      return monsterNode.getBoundsInParent().intersects(this.imageView.getBoundsInParent().getCenterX(),
              this.imageView.getBoundsInParent().getCenterY(),
              boundingBoxWidth, boundingBoxHeight);
  }

 public boolean isTargeInLineOfSight(List<LevelNode> nodeList, Line line) {
    for (Node b : nodeList) {
        if (line.intersects(b.getBoundsInParent())) {
            return false;
        }
    }
    return true;
 }

  public void receiveDamage(int damage) {
    if (health > 0) {
      ColorAdjust colorAdjust = new ColorAdjust();
      double oldBrightness = colorAdjust.getBrightness();
      colorAdjust.setBrightness(0.8);
      imageView.setEffect(colorAdjust);
      PauseTransition pause = new PauseTransition(Duration.seconds(0.1));

      this.isHurt = true;
      pause.setOnFinished(event -> {
        this.isHurt = false;
        this.health -= damage;
        colorAdjust.setBrightness(oldBrightness);
        imageView.setEffect(colorAdjust);
      });

      pause.play();
    }
    logger.info("Remaining health: " + this.health);

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

  public void setCharPosx(double charPosx) {
    this.charPosx = charPosx;
  }

  public void setCharPosy(double charPosy) {
    this.charPosy = charPosy;
  }

  public String getName() {
    return name;
  }

  public int getHealth() {
    return health;
  }

  public double getVelocity() {
     return velocity;
  }
}
