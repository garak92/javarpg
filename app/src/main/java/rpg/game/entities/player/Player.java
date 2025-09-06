package rpg.game.entities.player;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import rpg.engine.animation.SpriteAnimation;
import rpg.engine.cli.CommandLine;
import rpg.engine.common.Game;
import rpg.engine.common.Usable;
import rpg.engine.common.misc.PlayerStatusBar;
import rpg.engine.levels.Level;
import rpg.engine.levels.LevelNode;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.monster.MonsterUtils;
import rpg.engine.notification.INotificationService;
import rpg.engine.notification.NotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player extends BaseMonster {
    private static final EnumMonsterAlignment alignment = EnumMonsterAlignment.PLAYER;
    private static Player instance;
    private final int shield;
    private final String name;
    private final CopyOnWriteArrayList<Usable> usedEntities = new CopyOnWriteArrayList<>();
    private final PlayerStatusBar statusBar;
    private boolean iceBallAttack = false;
    private boolean moveRight = false;
    private boolean moveLeft = false;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean using = false;
    private int attackAccumulator = 30;
    private List<LevelNode> solidTiles;
    private int experiencePoints = 0;
    private int playerLevel = 1;
    private INotificationService notificationService = new NotificationService();


    private Player(double charPosx, double charPosy, double velocity, int health,
                   int shield, String name, Stage primaryStage, Level level) {
        super(charPosx, charPosy, velocity, health, alignment, level, name);
        this.shield = shield;
        this.name = name;
        this.solidTiles = level.getSolidTiles();

        Pane playerPane = new Pane();
        statusBar = new PlayerStatusBar(0, 0, playerPane, this);
        Game.getInstance().getRoot().getChildren().add(playerPane);

        preCacheSprites(new HashMap<>() {
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


    }

    public static Player initialize(double charPosx, double charPosy, double velocity, int health,
                                    int shield, String name, Stage primaryStage, Pane root, Level level) {
        if (instance == null) {
            instance = new Player(charPosx, charPosy, velocity, health,
                    shield, name, primaryStage, level);
        } else {
            // Player should maintain their stats throughout a level transition
            // but other stuff, such as the level data, must obviously change
            instance.setCharPosx(charPosx);
            instance.setCharPosy(charPosy);
            instance.setLevel(level);
        }
        return instance;
    }

    public static Player getInstance() throws NullPointerException {
        if (instance == null) {
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
                    b.use(this);
                    usedEntities.add(b);
            } else {
                using = false;
            }
        }
    }

    public void stopInteraction() {
        double boundingBoxHeight = this.imageView.getBoundsInParent().getHeight() * 20 / 100;
        double boundingBoxWidth = this.imageView.getBoundsInParent().getWidth() * 20 / 100;
        using = false;
        for (Usable b : usedEntities) {
            if (!b.getLevelNode().getBoundsInParent().intersects(this.imageView.getBoundsInParent().getCenterX(),
                    this.imageView.getBoundsInParent().getCenterY(),
                    boundingBoxWidth, boundingBoxHeight)) {
                b.stopUsing(this);
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
        if(experiencePoints >= 70 && playerLevel != 2) {
            this.playerLevel = 2;
            notificationService
                    .pushNotification("YOU ARE NOW LEVEL 2! NEW ABILITY UNLOCKED: TERROR OF ICE.\n PRESS 2 TO TRY IT! USE IT WISELY.",
                            5000);
        }

        statusBar.update(0, 0);
        double projectedX = 0;
        double projectedY = 0;
        double moveX = 0;
        double moveY = 0;

        if(isPlayerMoving()) {
            if (moveRight) moveX += 1;
            if (moveLeft) moveX -= 1;
            if (moveDown) moveY += 1;
            if (moveUp) moveY -= 1;

            double length = Math.sqrt(moveX * moveX + moveY * moveY);
            if (length != 0) {
                moveX = (moveX / length) * velocity;
                moveY = (moveY / length) * velocity;
            }
        }

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
            projectedX = charPosx + moveX;
            if (detectCollision(solidTiles, projectedX, charPosy)) {
                return;
            }
            charPosx = projectedX;
        }

        if (moveLeft) {
            imageView.setScaleX(-1);
            imageView.setImage(images.get("run"));
            projectedX = charPosx + moveX;
            if (detectCollision(solidTiles, projectedX, charPosy)) {
                return;
            }
            charPosx = projectedX;
        }

        if (moveUp) {
            imageView.setImage(images.get("run"));
            projectedY = charPosy + moveY;
            if (detectCollision(solidTiles, charPosx, projectedY)) {
                return;
            }
            charPosy = projectedY;
        }

        if (moveDown) {
            imageView.setImage(images.get("run"));
            projectedY = charPosy + moveY;
            if (detectCollision(solidTiles, charPosx, projectedY)) {
                return;
            }
            charPosy = projectedY;
        }

        if (iceBallAttack) {
            imageView.setImage(images.get("attack"));
        }

        if(!isPlayerMoving() && !iceBallAttack) {
            imageView.setImage(images.get("idle"));
        }

        if (using) {
            interact(usables);
        }

        stopInteraction();
    }


    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public void addExperiencePoints(int experiencePoints) {
        this.experiencePoints += experiencePoints;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    private void setLevel(Level level) {
        this.level = level;
        this.solidTiles = level.getSolidTiles();
    }

    public void heal(int healingValue) {
        if(health < MAX_HEALTH) {
            health = Math.min((health + healingValue), MAX_HEALTH);
        }

    }

    private boolean isPlayerMoving() {
        return moveDown || moveRight || moveLeft || moveUp;
    }
}
