package rpg.engine.levels;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.common.Thing;
import rpg.engine.common.Usable;
import rpg.engine.common.camera.Camera;
import rpg.engine.monster.BaseMonster;
import rpg.engine.monster.EnumMonsterAlignment;
import rpg.engine.monster.EnumMonsterKind;
import rpg.engine.monster.MonsterUtils;
import rpg.engine.music.MusicSystem;
import rpg.game.entities.igrenne.Igrenne;
import rpg.game.entities.player.Player;

import javax.sound.midi.InvalidMidiDataException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Level {
    final static Logger logger = LoggerFactory.getLogger(Level.class);
    private final int TILE_SIZE = 128;
    private final List<List<String>> tileMap = new ArrayList<List<String>>();
    private final List<String> dialogList = new ArrayList<>();
    private final List<List<String>> thingMap = new ArrayList<List<String>>();
    private final String title;
    private final String textureFile;
    private final Pane pane;
    private final Stage stage;
    private final Camera camera = new Camera();
    private final List<Thing> thingQueue = new ArrayList<>();
    private final List<Thing> removeThingQueue = new ArrayList<>();
    protected List<LevelNode> tiles = new ArrayList<>();
    protected List<Thing> things = new ArrayList<>();
    protected List<Usable> usables = new ArrayList<>();
    protected List<LevelNode> solidNodes = new ArrayList<>();
    protected List<BaseMonster> envProps = new ArrayList<>();
    private Image tileSheet;
    private List<BaseMonster> enemies = new LinkedList<>();
    private Player player;

    public Level(String levelName, String textureFile, Pane pane, Stage stage) {
        this.title = levelName;
        this.textureFile = textureFile;
        this.pane = pane;
        this.stage = stage;
    }

    public Level(String levelName) {
        Level level = Player.getInstance().getLevel();
        this.title = levelName;
        this.textureFile = "sheet1.png"; // Default
        this.pane = level.pane;
        this.stage = level.stage;
    }

    public Level load() {
        try (
                // Common level background
                InputStream backgroundImageFile = this.getClass().getResourceAsStream("/sprites/levels/common-background.jpg");

                // Common level background
                InputStream music = this.getClass().getResourceAsStream("/music/" + this.title + ".mid");

                // Level tiles file
                BufferedReader tileReader = new BufferedReader(
                        new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".tiles"),
                                StandardCharsets.UTF_8));

                // Level monsters file
                BufferedReader thingReader = new BufferedReader(
                        new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".monsters"),
                                StandardCharsets.UTF_8));

                // Level textures file
                InputStream stream = this.getClass().getResourceAsStream("/sprites/levels/" + this.textureFile)) {

                // Level dialogs file
                Object dialogsFile = this.getClass().getResourceAsStream("/levels/" + this.title + ".dialogs");
                BufferedReader dialogReader = null;
                if(dialogsFile != null) {
                    dialogReader = new BufferedReader(
                            new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".dialogs"),
                                    StandardCharsets.UTF_8));
                }

            // Cleanup
            this.pane.getChildren().clear();

            // Initialize tile list and monster list
            cacheLevelData(tileReader, NodeTypeEnum.LEVEL);
            cacheLevelData(thingReader, NodeTypeEnum.MONSTER);
            cacheDialogData(dialogReader);

            // Create tile sheet image
            tileSheet = new Image(stream);

            // Load tiles and monsters
            loadTiles();
            loadMonsters();

            // Load common background
            Image commonBackgroundImage = new Image(backgroundImageFile);
            ImagePattern commonBackground = new ImagePattern(commonBackgroundImage);
            pane.getScene().setFill(commonBackground);

            // Load music
            MusicSystem.INSTANCE.playFile(music);

        } catch (IOException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return this;
    }

    private void loadTiles() {
        if (tileMap.isEmpty()) {
            logger.error("No tile data found for this level");
            return;
        }
        try {
            for (int i = 0; i < tileMap.size(); i++) {
                for (int j = 0; j < tileMap.get(i).size(); j++) {
                    String currentTileValue = tileMap.get(i).get(j);
                    TileData tileData = TileMapper.getTileData(currentTileValue);

                    if (tileData != null) {
                        tiles.add(createLevelNode(j, i, tileData.getColumn(), tileData.getRow(), tileData.isSolid(), NodeTypeEnum.LEVEL));
                    }
                }
            }
            pane.getChildren().addAll(getTiles());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cacheDialogData(BufferedReader reader) {
        if(reader == null) {
            return;
        }

        try {
            String line = reader.readLine();
            while (line != null) {
                dialogList.add(line);
                line = reader.readLine();
            }

        } catch (Exception e) {
            logger.error("There was an error caching dialog data");
            e.printStackTrace();
        }
    }

    private void loadMonsters() {
        if (tileMap.isEmpty()) {
            logger.error("No monster data found for this level");
            return;
        }
        try {
            for (int i = 0; i < thingMap.size(); i++) {
                for (int j = 0; j < thingMap.get(i).size(); j++) {
                    String currentTileValue = thingMap.get(i).get(j);
                    switch (currentTileValue) {
                        case "1":
                            Player player = Player.initialize(TILE_SIZE * j, TILE_SIZE * i, 12, 100, 10, "Player 1", stage,
                                    pane, this);
                            things.add(player);
                            player.spawn(pane);
                            this.player = player;
                            break;
                        case "2":
                            Igrenne igrene = Igrenne.initialize(TILE_SIZE * j, TILE_SIZE * i, 10, 30, 10, "Igrenne",
                                    this);
                            usables.add(igrene);
                            things.add(igrene);
                            igrene.spawn(pane);
                            break;
                        case "3":
                            MonsterUtils.spawnMonster(EnumMonsterKind.SATYR, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "4":
                            MonsterUtils.spawnMonster(EnumMonsterKind.WEREWOLF, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "5":
                            MonsterUtils.spawnMonster(EnumMonsterKind.MINOTAUR, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "6":
                            MonsterUtils.spawnMonster(EnumMonsterKind.SKELETON_ARCHER, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "7":
                            MonsterUtils.spawnMonster(EnumMonsterKind.ORC_BERSERK, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "8":
                            MonsterUtils.spawnMonster(EnumMonsterKind.GORGON, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "@":
                            MonsterUtils.spawnMonster(EnumMonsterKind.PORTAL_CITY_HUB, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "A":
                            MonsterUtils.spawnMonster(EnumMonsterKind.PORTAL_LEVEL1, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "h":
                            MonsterUtils.spawnMonster(EnumMonsterKind.MINI_HEALTH_PICKUP, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "e":
                            MonsterUtils.spawnMonster(EnumMonsterKind.ELIXIR_OF_YOUTH, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "T":
                            MonsterUtils.spawnMonster(EnumMonsterKind.TREE, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "R":
                            MonsterUtils.spawnMonster(EnumMonsterKind.ROCK, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "B":
                            MonsterUtils.spawnMonster(EnumMonsterKind.BUSH, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "H":
                            MonsterUtils.spawnMonster(EnumMonsterKind.HOUSE, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "v":
                            MonsterUtils.spawnMonster(EnumMonsterKind.VILLAGER, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "↑":
                            MonsterUtils.spawnMonster(EnumMonsterKind.LASER_CANNON_UP, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "↓":
                            MonsterUtils.spawnMonster(EnumMonsterKind.LASER_CANNON_DOWN, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "→":
                            MonsterUtils.spawnMonster(EnumMonsterKind.LASER_CANNON_RIGHT, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                        case "←":
                            MonsterUtils.spawnMonster(EnumMonsterKind.LASER_CANNON_LEFT, TILE_SIZE * j, TILE_SIZE * i, this);
                            break;
                    }
                    // Get initial enemy list
                    enemies = things.stream().filter(v -> {
                        return v.getMonster().getAlignment() == EnumMonsterAlignment.ENEMY;
                    }).map(v -> v.getMonster()).collect(Collectors.toList());
                }
            }

            if (this.player == null) {
                logger.error("No player start data found for this level! Aborting...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public List<LevelNode> getTiles() {
        return this.tiles;
    }

    public List<BaseMonster> getEnvProps() {
        return envProps;
    }

    public List<LevelNode> getSolidTiles() {
        if (!solidNodes.isEmpty()) {
            return solidNodes;
        }
        solidNodes = tiles
                .stream()
                .filter(c -> c.isSolid())
                .collect(Collectors.toList());
        return solidNodes;
    }

    private LevelNode createLevelNode(int col, int row, int offsetX, int offsetY, boolean solid, NodeTypeEnum type) {
        LevelNode levelNode = new LevelNode(type, solid, tileSheet);
        levelNode.setViewport(new Rectangle2D(offsetX * TILE_SIZE, offsetY * TILE_SIZE, TILE_SIZE, TILE_SIZE));
        levelNode.setLayoutX(col * TILE_SIZE);
        levelNode.setLayoutY(row * TILE_SIZE);
        levelNode.toBack();

        return levelNode;

    }

    public List<Thing> getThings() {
        return this.things;
    }

    public List<Usable> getUsables() {
        return usables;
    }

    public void update() throws Throwable {

        if (!thingQueue.isEmpty()) {
            for (int i = 0; i < thingQueue.size(); i++) {
                things.add(thingQueue.get(i).getMonster());
                thingQueue.get(i).getMonster().spawn(this.pane);
                thingQueue.remove(thingQueue.get(i));
            }
        }
        if (!removeThingQueue.isEmpty()) {
            for (int i = 0; i < removeThingQueue.size(); i++) {
                things.remove(removeThingQueue.get(i).getMonster());
                enemies.remove(removeThingQueue.get(i).getMonster());
                removeThingQueue.get(i).getMonster().deSpawn(this.pane);
                removeThingQueue.remove(removeThingQueue.get(i));
            }
        }
        for (int i = 0; i < things.size(); i++) {
            Thing currentThing = things.get(i);
            currentThing.getMonster().setPrevCharPosx(currentThing.getMonster().getImageView().getTranslateX());
            currentThing.getMonster().setPrevCharPosy(currentThing.getMonster().getImageView().getTranslateY());
            currentThing.update(usables);
        }

        camera.updateCamera(player);
    }

    public void interpolate(double alpha) throws Throwable {
        for (Thing i : things) {
            i.interpolate(alpha);
        }
        camera.interpolate(alpha, pane);
    }

    public void render() throws Throwable {
        for (Thing i : things) {
            i.render();
        }

        camera.render(pane);
    }


    private void cacheLevelData(BufferedReader reader, NodeTypeEnum type) {
        try {

            String line = reader.readLine();

            while (line != null) {
                List<String> currentRow = new ArrayList<>();
                for (char i : line.toCharArray()) {
                    currentRow.add(String.valueOf(i));
                }
                switch (type) {
                    case LEVEL:
                        tileMap.add(currentRow);
                        break;
                    case MONSTER:
                        thingMap.add(currentRow);
                        break;
                    default:
                        break;
                }

                line = reader.readLine();
            }

        } catch (Exception e) {
            logger.error("There was an error caching level data");
            e.printStackTrace();
        }
    }

    public void addThing(Thing thing) {
        thingQueue.add(thing);
    }

    public void removeThing(Thing thing) {
        removeThingQueue.add(thing);
    }

    public Player getPlayer() {
        return player;
    }

    public List<BaseMonster> getEnemies() {
        return enemies;
    }

    public void updateEnemyList() {
        enemies = things.stream().filter(v -> {
            return v.getMonster().getAlignment() == EnumMonsterAlignment.ENEMY;
        }).map(v -> v.getMonster()).collect(Collectors.toList());
    }

    public List<BaseMonster> getAgenticMonsters() {
        return things.stream().map(Thing::getMonster).filter(monster -> {
            return !monster.isDead() &&
                    (monster.getAlignment() == EnumMonsterAlignment.ENEMY
                            || monster.getAlignment() == EnumMonsterAlignment.PLAYER);
        }).collect(Collectors.toList());
    }

    public Pane getPane() {
        return pane;
    }

    public Stage getStage() {
        return stage;
    }

    public List<String> getDialogList() {
        return dialogList;
    }
}
