package rpg.game.entities.villager;

import javafx.geometry.Rectangle2D;
import javafx.util.Duration;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.common.Usable;
import rpg.engine.dialog.CyclicDialogBox;
import rpg.engine.levels.Level;
import rpg.engine.levels.LevelNode;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.game.entities.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Villager extends BaseMonster implements Usable {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.FRIEND;
    private final CyclicDialogBox dialogBox;
    private final Random random = new Random();
    private final String[] defaultDialogueList = {"Hello, are you an adventurer?", "Sorry, I'm busy right now",
            "Can't believe I've lost my money at the horse race again...",
            "Ever since the evil lord came, its getting harder and harder to get a job",
            "Will you have the courage to defeat the evil lord?", "I hope the king is all right",
            "I don't talk to strangers"};
    private final String[] types = {"oldMan", "woman", "boy"};
    private final String type;
    private final int movementChangeFrequency = 30;
    double velocity = 2;
    private boolean shouldMoveRandomly = false;
    private int randomMovementAccumulator = 0;
    private double moveDirX = charPosx;
    private double moveDirY = charPosy;

    public Villager(
            double charPosx,
            double charPosy,
            double velocity,
            int health,
            int shield, String name,
            Level level) {
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

        getImageView().setFitWidth(100);
        getImageView().setFitHeight(100);

        getImageView().setPreserveRatio(true);

        this.dialogBox = new CyclicDialogBox(List.of(defaultDialogueList), level.getPane(), this);
    }

    @Override
    public void die() {
        // level.removeThing(this);
    }

    @Override
    public void update(List<Usable> usables) {
        // 1. If talking, stop movement and show idle
        if (dialogBox.isOpen()) {
            getImageView().setImage(images.get(this.type + "Idle"));
            return;
        }

        // 2. Occasionally change direction
        if (randomMovementAccumulator >= movementChangeFrequency) {
            shouldMoveRandomly = random.nextDouble() < 0.4;
            moveDirX = random.nextBoolean() ? -1 : 1;
            moveDirY = random.nextBoolean() ? -1 : 1;
            randomMovementAccumulator = 0;
        } else {
            randomMovementAccumulator++;
        }

        // 3. Only move if shouldMoveRandomly is true
        if (shouldMoveRandomly) {
            double newX = charPosx + moveDirX * velocity;
            double newY = charPosy + moveDirY * velocity;

            // 4. Check for collisions before moving
            if (!detectCollision(getLevel().getSolidTiles(), newX, newY)) {
                charPosx = newX;
                charPosy = newY;

                // Set walking animation
                getImageView().setImage(images.get(this.type + "Walk"));
                imageView.setScaleX(moveDirX < 0 ? -1 : 1);
            } else {
                // Stop if blocked
                getImageView().setImage(images.get(this.type + "Idle"));
            }
        } else {
            // Not moving
            getImageView().setImage(images.get(this.type + "Idle"));
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

    @Override
    public BaseMonster getBaseMonster() {
        return this;
    }
}
