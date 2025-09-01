package rpg.game.environment;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.engine.levels.EntityNode;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.game.entities.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Cattle extends BaseMonster implements Usable {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.FRIEND;
    private final Random random = new Random();
    private final String[] types = {"bull", "calf", "lamb", "piglet", "rooster", "sheep", "turkey"};
    private final String type;
    private final int movementChangeFrequency = 30;
    double velocity = 2;
    private boolean shouldMoveRandomly = false;
    private int randomMovementAccumulator = 0;
    private double moveDirX = charPosx;
    private double moveDirY = charPosy;
    private boolean idle = true;
    private int SIZE = 32;

    public Cattle(
            double charPosx,
            double charPosy,
            double velocity,
            int health,
            int shield, String name,
            Level level) {
        super(charPosx, charPosy, velocity, health, alignment, level, name);

        preCacheSprites(new HashMap<String, String>() {
            {
                put("bull", "/other/cattle/Bull.png");
                put("calf", "/other/cattle/Calf.png");
                put("lamb", "/other/cattle/Lamb.png");
                put("piglet", "/other/cattle/Piglet.png");
                put("rooster", "/other/cattle/Rooster.png");
                put("sheep", "/other/cattle/Sheep.png");
                put("turkey", "/other/cattle/Turkey.png");
            }
        });

        this.type = types[random.nextInt(types.length)];

        getImageView().setImage(images.get(this.type));
        if(this.type.equals("bull") || this.type.equals("calf")) {
            SIZE = 64;
        }
        getImageView().setViewport(new Rectangle2D(charPosx, charPosy, SIZE, SIZE));
        setAnimation(new SpriteAnimation(imageView, new Duration(800), 6, 6, 0, 0, SIZE, SIZE));

        getImageView().setFitHeight(SIZE * 2);
        getImageView().setPreserveRatio(true);
    }

    @Override
    public void die() {
        // level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
        // 1. Occasionally change direction
        if (randomMovementAccumulator >= movementChangeFrequency) {
            shouldMoveRandomly = random.nextDouble() < 0.4;
            moveDirX = random.nextBoolean() ? -1 : 1;
            moveDirY = random.nextBoolean() ? -1 : 1;
            randomMovementAccumulator = 0;
        } else {
            randomMovementAccumulator++;
        }

        // 2. Only move if shouldMoveRandomly is true
        if (shouldMoveRandomly) {
            double newX = charPosx + moveDirX * velocity;
            double newY = charPosy + moveDirY * velocity;

            // 4. Check for collisions before moving
            if (!detectCollision(getLevel().getSolidTiles(), newX, newY)) {
                charPosx = newX;
                charPosy = newY;

                // Set walking animation
                if(idle) {
                    animation.stop();
                    setAnimation(new SpriteAnimation(imageView, new Duration(800), 6, 6, 0, SIZE * 3, SIZE, SIZE));
                    idle = false;
                    animation.play();
                }
                imageView.setScaleX(moveDirX < 0 ? -1 : 1);
            } else {
                // Stop if blocked
                if(!idle) {
                    animation.stop();
                    setAnimation(new SpriteAnimation(imageView, new Duration(800), 6, 6, 0, 0, SIZE, SIZE));
                    idle = true;
                    animation.play();
                }
            }
        } else {
            // Not moving
            if(!idle) {
                animation.stop();
                setAnimation(new SpriteAnimation(imageView, new Duration(800), 6, 6, 0, 0, SIZE, SIZE));
                idle = true;
                animation.play();
            }
        }
    }

    @Override
    public void use(Player player) {
    }

    @Override
    public EntityNode getLevelNode() {
        return this.imageView;
    }

    @Override
    public BaseMonster getBaseMonster() {
        return this;
    }
}
