package rpg.Levels;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import rpg.Common.MusicSystem;
import rpg.Common.QuestLog;
import rpg.Common.Thing;
import rpg.Common.Usable;
import rpg.Monsters.Bringer.Bringer;
import rpg.Monsters.Igrene.Igrene;
import rpg.Monsters.Villager.Villager;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumEnemyStates;
import rpg.Monsters.EnumMonsterAlignment;
import rpg.Monsters.MiniHealthPickup;
import rpg.Monsters.Player;
import rpg.Monsters.Portal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Level {
  final static Logger logger = LoggerFactory.getLogger(Level.class);
  private final int TILE_SIZE = 32;
  private List<List<String>> tileMap = new ArrayList<List<String>>();
  private List<List<String>> thingMap = new ArrayList<List<String>>();
  private String title;
  protected List<LevelNode> tiles = new ArrayList<>();
  protected List<Thing> things = new ArrayList<>();
  protected List<Usable> usables = new ArrayList<>();
  private String textureFile;
  private Image tileSheet;
  private Pane pane;
  private Stage stage;
  private Queue<Thing> thingQueue = new LinkedList<>();
  private Queue<Thing> removeThingQueue = new LinkedList<>();
  private List<BaseMonster> enemies = new LinkedList<>();
  private Player player;

  public Level(String levelName, String textureFile, Pane pane, Stage stage) {
    this.title = levelName;
    this.textureFile = textureFile;
    this.pane = pane;
    this.stage = stage;
  }

  public Level load() {
    try (
        // Common level background
        InputStream backgroundImageFile = this.getClass().getResourceAsStream("/sprites/levels/common-background.jpg");

        // Common level background
        InputStream music = this.getClass().getResourceAsStream("/music/" + this.title + ".mid");

        // Level tiles file
        BufferedReader tileReader = new BufferedReader(
            new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".tiles")));

        // Level monsters file
        BufferedReader thingReader = new BufferedReader(
            new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".monsters")));

        // Level textures file
        InputStream stream = this.getClass().getResourceAsStream("/sprites/levels/" + this.textureFile)) {

      // Cleanup
      this.pane.getChildren().clear();

      // Initialize tile list and monster list
      cacheLevelData(tileReader, NodeTypeEnum.LEVEL);
      cacheLevelData(thingReader, NodeTypeEnum.MONSTER);

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
    if (tileMap.size() == 0) {
      logger.error("No tile data found for this level");
      return;
    }
    try (InputStream stream = this.getClass().getResourceAsStream("/sprites/levels/" + textureFile)) {
      for (int i = 0; i < tileMap.size(); i++) {
        for (int j = 0; j < tileMap.get(i).size(); j++) {
          String currentTileValue = tileMap.get(i).get(j);
          switch (currentTileValue) {
            case "0":
              tiles.add(createLevelNode(j, i, 2, 1, false, NodeTypeEnum.LEVEL));
              break;
            case "1":
              tiles.add(createLevelNode(j, i, 1, 5, false, NodeTypeEnum.LEVEL));
              break;
            case "a":
              tiles.add(createLevelNode(j, i, 5, 1, true, NodeTypeEnum.LEVEL));
              break;
            case "b":
              tiles.add(createLevelNode(j, i, 5, 5, true, NodeTypeEnum.LEVEL));
              break;
            case "c":
              tiles.add(createLevelNode(j, i, 5, 6, true, NodeTypeEnum.LEVEL));
              break;
            case "d":
              tiles.add(createLevelNode(j, i, 6, 7, true, NodeTypeEnum.LEVEL));
              break;
            case "e":
              tiles.add(createLevelNode(j, i, 3, 6, true, NodeTypeEnum.LEVEL));
              break;
            case "f":
              tiles.add(createLevelNode(j, i, 2, 5, true, NodeTypeEnum.LEVEL));
              break;
            case "g":
              tiles.add(createLevelNode(j, i, 0, 0, true, NodeTypeEnum.LEVEL));
              break;
            default:
              break;
          }
        }
      }
      pane.getChildren().addAll(getTiles());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadMonsters() {
    if (tileMap.size() == 0) {
      logger.error("No monster data found for this level");
      return;
    }
    try {
      for (int i = 0; i < thingMap.size(); i++) {
        for (int j = 0; j < thingMap.get(i).size(); j++) {
          String currentTileValue = thingMap.get(i).get(j).toString();
          switch (currentTileValue) {
            case "1":
              System.out.println("creating player");
              Player player = Player.initialize(TILE_SIZE * j, TILE_SIZE * i, 12, 100, 10, "Player 1", stage,
                  pane, this);
              things.add(player);
              player.spawn(pane);
              this.player = player;
              break;
            case "2":
              Igrene igrene = Igrene.initialize(TILE_SIZE * j, TILE_SIZE * i, 10, 30, 10, "Igrene",
                  EnumEnemyStates.IDLE, this);
              usables.add(igrene);
              igrene.spawn(pane);
              break;
            case "3":
              Bringer bringer = new Bringer(TILE_SIZE * j, TILE_SIZE * i, 2, 50, 10, "Bringer of Death",
                  EnumEnemyStates.IDLE, this);
              things.add(bringer);
              bringer.spawn(pane);
              break;
            case "@":
              Portal portal = new Portal(TILE_SIZE * j, TILE_SIZE * i, 2, 50, 10, "Portal to City Hub",
                  EnumEnemyStates.IDLE, this, "cityhub", "sheet1.png");
              usables.add(portal);
              portal.spawn(pane);
              break;
            case "╡": // Extended ascii 181
              Portal portalToLevel1 = new Portal(TILE_SIZE * j, TILE_SIZE * i, 2, 50, 10, "Portal to level 1",
                  EnumEnemyStates.IDLE, this, "level1", "sheet1.png");
              usables.add(portalToLevel1);
              portalToLevel1.spawn(pane);
              break;
            case "h":
              MiniHealthPickup miniHealthPickup = new MiniHealthPickup(TILE_SIZE * j, TILE_SIZE * i,
                  this);
              things.add(miniHealthPickup);
              miniHealthPickup.spawn(pane);
              break;
            case "v":
              Villager villager = new Villager(TILE_SIZE * j, TILE_SIZE * i, 1, 30, 10, "Villager",
                  EnumEnemyStates.IDLE, this);
              usables.add(villager);
              things.add(villager);
              villager.spawn(pane);
              break;
            default:
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

  public List<LevelNode> getSolidTiles() {
    List<LevelNode> solidNodes = tiles
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

  public void update() {
    logger.info("Thing queue size: " + thingQueue.size());
    if (thingQueue.size() > 0) {
      for (Thing i : thingQueue) {
        things.add(i.getMonster());
        i.getMonster().spawn(this.pane);
        thingQueue.remove(i);
      }
    }
    if (removeThingQueue.size() > 0) {
      for (Thing i : removeThingQueue) {
        things.remove(i.getMonster());
        enemies.remove(i.getMonster());
        i.getMonster().deSpawn(this.pane);
        removeThingQueue.remove(i);
      }
    }
    for (Thing i : things) {
      i.update(usables);
    }
    logger.info("Thing list size: " + things.size());
    logger.info("Number of visible things: " + pane.getChildren().size());
    logger.info("Current Player Exp: " + player.getExperiencePoints());
    QuestLog.INSTANCE.displayQuests();
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

  public Pane getPane() {
    return pane;
  }

  public Stage getStage() {
    return stage;
  }

}
