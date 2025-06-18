package rpg.engine.monster;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Thing;
import rpg.engine.levels.Level;
import rpg.engine.levels.LevelNode;
import rpg.engine.levels.NodeTypeEnum;
import rpg.engine.render.IRenderer;
import rpg.engine.render.Renderer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseMonster implements Thing {
    protected LevelNode imageView = new LevelNode(NodeTypeEnum.MONSTER);
    protected Map<String, Image> images = new HashMap<>();
    protected Animation animation;
    protected Map<String, SpriteAnimation> animations;
    protected EnumMonsterAlignment alignment;
    protected IRenderer renderer;
    protected int health;
    protected double charPosx;
    protected double charPosy;
    protected double prevCharPosx;
    protected double prevCharPosy;
    protected double velocity;
    protected Level level;
    protected boolean isDead = false;
    protected boolean isHurt = false;
    protected String name;
    protected double boundingBoxHeight;
    protected double boundingBoxWidth;
    protected final Rectangle hitBox = new Rectangle(0, 0, 0, 0);
    protected final double HITBOX_SCALING_FACTOR_Y = 0.3;
    protected final double HITBOX_SCALING_FACTOR_X = 0.03;
    protected final DropShadow dropShadow = new DropShadow();
    protected static Logger logger = LoggerFactory.getLogger(BaseMonster.class);

    public abstract void die();

    public LevelNode getImageView() {
        return imageView;
    }

    public void spawn(Pane root) {
        if (alignment == EnumMonsterAlignment.PLAYER || alignment == EnumMonsterAlignment.ENEMY) {
            imageView.setFitHeight(170);
            imageView.setFitWidth(170);
            imageView.setPreserveRatio(true);
        }

        dropShadow.setOffsetY(4.0f);
        dropShadow.setOffsetX(4.0f);
        dropShadow.setColor(Color.BLACK);

        imageView.setEffect(dropShadow);
        root.getChildren().add(this.imageView);
        boundingBoxWidth = imageView.getBoundsInLocal().getWidth() * HITBOX_SCALING_FACTOR_X;
        boundingBoxHeight = imageView.getBoundsInLocal().getHeight() * HITBOX_SCALING_FACTOR_Y;
        hitBox.setWidth(boundingBoxWidth);
        hitBox.setHeight(boundingBoxHeight);

        updateHitBoxPosition(imageView.getBoundsInLocal().getCenterX(), imageView.getBoundsInLocal().getCenterY());
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
        this.renderer = new Renderer(this.imageView);
    }

    protected BaseMonster(double charPosx, double charPosy, double velocity, int health,
                          EnumMonsterAlignment alignment, Level level) {
        this.charPosx = charPosx;
        this.charPosy = charPosy;
        this.velocity = velocity;
        this.health = health;
        this.alignment = alignment;
        this.level = level;
        this.renderer = new Renderer(this.imageView);
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

    public void setAnimation(SpriteAnimation animation) {
        this.animation = animation;
        if (animations == null) {
            animation.stop();
            animation.setCycleCount(Animation.INDEFINITE);
            animation.play();
        }

    }

    protected void updateHitBoxPosition(double coordinateX, double coordinateY) {
        hitBox.setX((coordinateX + this.imageView.getBoundsInLocal().getWidth() / 2)
                - ((boundingBoxWidth) / 2));
        hitBox.setY((coordinateY + this.imageView.getBoundsInLocal().getHeight() / 2)
                - ((boundingBoxHeight) / 2));
    }

    public boolean detectCollision(List<LevelNode> nodeList, double targetX, double targetY) {
        updateHitBoxPosition(targetX, targetY);

        // Detect collision with solid tiles
        for (Node b : nodeList) {
            if (b.getBoundsInParent().intersects(hitBox.getBoundsInLocal())) {
                return true;
            }
        }

        // Detect collision with solid environmental props (trees, rocks, etc.)
        for (BaseMonster m : level.getEnvProps()) {
            if (m.getImageView().getBoundsInParent().intersects(hitBox.getBoundsInLocal())) {
                return true;
            }
        }

        return false;
    }

    // Use this function when you want to allow friendly fire among monsters
    public BaseMonster detectCollisionWithMonsterList(List<BaseMonster> monsterList) {
        updateHitBoxPosition(charPosx, charPosy);

        for (BaseMonster b : monsterList) {
            if (b.getImageView().getBoundsInParent().intersects(hitBox.getBoundsInLocal())) {
                return b;
            }
        }
        return null;
    }


    public boolean detectCollision(BaseMonster monster) {
        updateHitBoxPosition(charPosx, charPosy);
        LevelNode monsterNode = monster.getImageView();
        return monsterNode.getBoundsInParent().intersects(hitBox.getBoundsInLocal());
    }

    public boolean isTargetInLineOfSight(List<LevelNode> nodeList, Line line) {
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
                imageView.setEffect(dropShadow);
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

    public boolean isHurt() {
        return isHurt;
    }

    public void setHurt(boolean hurt) {
        isHurt = hurt;
    }

    public void setPrevCharPosx(double prevCharPosx) {
        this.prevCharPosx = prevCharPosx;
    }

    public void setPrevCharPosy(double prevCharPosy) {
        this.prevCharPosy = prevCharPosy;
    }

    @Override
    public void interpolate(double alpha) throws Throwable {
        imageView.setTranslateX(alpha * charPosx + (1 - alpha) * prevCharPosx);
        imageView.setTranslateY(alpha * charPosy + (1 - alpha) * prevCharPosy);
    }

    @Override
    public void render() throws Throwable {
        if (!isOutsideOfScreen()) {
            this.renderer.updatePosition(charPosx, charPosy);
        }
    }

    public boolean isOutsideOfScreen() {
        Node node = this.imageView;
        if (node.getScene() == null) return true;

        Bounds bounds = node.localToScene(node.getBoundsInLocal());

        double sceneWidth = node.getScene().getWidth();
        double sceneHeight = node.getScene().getHeight();

        return bounds.getMaxX() < 0 || bounds.getMinX() > sceneWidth ||
                bounds.getMaxY() < 0 || bounds.getMinY() > sceneHeight;
    }

}
