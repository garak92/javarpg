package rpg.Levels;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import rpg.Common.QuestLog;
import rpg.Common.Thing;
import rpg.Common.Usable;
import rpg.Monsters.Bringer.Bringer;
import rpg.Monsters.Igrene.Igrene;
import rpg.Monsters.BaseMonster;
import rpg.Monsters.EnumEnemyStates;
import rpg.Monsters.EnumMonsterAlignment;
import rpg.Monsters.Player;

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
  private List<List<Integer>> tileMap = new ArrayList<List<Integer>>();
  private List<List<Integer>> thingMap = new ArrayList<List<Integer>>();
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

        // Level tiles file
        BufferedReader tileReader = new BufferedReader(
            new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".tiles")));

        // Level monsters file
        BufferedReader thingReader = new BufferedReader(
            new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".monsters")));

        // Level textures file
        InputStream stream = this.getClass().getResourceAsStream("/sprites/levels/" + this.textureFile)) {

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
    } catch (IOException e) {
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
          int currentTileValue = tileMap.get(i).get(j);
          switch (currentTileValue) {
            case 0:
              tiles.add(createLevelNode(j, i, 2, 1, false, NodeTypeEnum.LEVEL));
              break;
            case 1:
              tiles.add(createLevelNode(j, i, 0, 1, true, NodeTypeEnum.LEVEL));
              break;
            case 2:
              tiles.add(createLevelNode(j, i, 3, 1, true, NodeTypeEnum.LEVEL));
              break;
            case 3:
              tiles.add(createLevelNode(j, i, 1, 5, true, NodeTypeEnum.LEVEL));
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
          int currentTileValue = thingMap.get(i).get(j);
          switch (currentTileValue) {
            case 1:
              System.out.println("creating player");
              Player player = new Player(TILE_SIZE * j, TILE_SIZE * i, 10, 100, 10, "Player 1", stage,
                  pane, this);
              things.add(player);
              player.spawn(pane);
              this.player = player;
              break;
            case 2:
              Igrene igrene = new Igrene(TILE_SIZE * j, TILE_SIZE * i, 10, 30, 10, "Igrene",
                  EnumEnemyStates.IDLE, this);
              usables.add(igrene);
              igrene.spawn(pane);
              break;
            case 3:
              Bringer bringer = new Bringer(TILE_SIZE * j, TILE_SIZE * i, 2, 50, 10, "Bringer of Death",
                  EnumEnemyStates.IDLE, this);
              things.add(bringer);
              bringer.spawn(pane);
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
    levelNode.setViewport(new Rectangle2D(offsetX * TILE_SIZE, TILE_SIZE, TILE_SIZE, TILE_SIZE));
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
        List<Integer> currentRow = new ArrayList<>();
        for (char i : line.toCharArray()) {
          currentRow.add(Character.getNumericValue(i));
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

}
