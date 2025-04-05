package rpg.Levels;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import rpg.Common.*;
import rpg.Monsters.*;
import rpg.Monsters.Enemy.Gorgon.Gorgon;
import rpg.Monsters.Enemy.Minotaur.Minotaur;
import rpg.Monsters.Enemy.MaleSatyr.MaleSatyr;
import rpg.Monsters.Enemy.OrcBerserk.OrcBerserk;
import rpg.Monsters.Enemy.Props.Cannon.CannonOrientation;
import rpg.Monsters.Enemy.Props.Cannon.LaserCannon;
import rpg.Monsters.Enemy.SkeletonArcher.SkeletonArcher;
import rpg.Monsters.Igrene.Igrene;
import rpg.Monsters.Villager.Villager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.Monsters.Enemy.Werewolf.Werewolf;

public class Level {
  final static Logger logger = LoggerFactory.getLogger(Level.class);
  private final int TILE_SIZE = 128;
  private final List<List<String>> tileMap = new ArrayList<List<String>>();
  private final List<List<String>> thingMap = new ArrayList<List<String>>();
  private final String title;
  protected List<LevelNode> tiles = new ArrayList<>();
  protected List<Thing> things = new ArrayList<>();
  protected List<Usable> usables = new ArrayList<>();
  private final String textureFile;
  private Image tileSheet;
  private final Pane pane;
  private final Stage stage;
  private final CopyOnWriteArrayList<Thing> thingQueue = new CopyOnWriteArrayList<>();
  private final CopyOnWriteArrayList<Thing> removeThingQueue = new CopyOnWriteArrayList<>();
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
            new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".tiles"),
                    StandardCharsets.UTF_8));

            // Level monsters file
            BufferedReader thingReader = new BufferedReader(
            new InputStreamReader(this.getClass().getResourceAsStream("/levels/" + this.title + ".monsters"),
                    StandardCharsets.UTF_8));

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

  private void loadMonsters() {
    if (tileMap.isEmpty()) {
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
                   this);
              usables.add(igrene);
              igrene.spawn(pane);
              break;
            case "3":
              MaleSatyr maleSatyr = new MaleSatyr(TILE_SIZE * j, TILE_SIZE * i, 2, "Male Satyr",
                   this);
              things.add(maleSatyr);
              maleSatyr.spawn(pane);
              break;
            case "4":
              Werewolf werewolf = new Werewolf(TILE_SIZE * j, TILE_SIZE * i, 2, "Werewolf",
                      this);
              things.add(werewolf);
              werewolf.spawn(pane);
              break;
            case "5":
              Minotaur minotaur = new Minotaur(TILE_SIZE * j, TILE_SIZE * i, 2, "Minotaur",
                      this);
              things.add(minotaur);
              minotaur.spawn(pane);
              break;
            case "6":
              SkeletonArcher skeletonArcher = new SkeletonArcher(TILE_SIZE * j, TILE_SIZE * i, 2,
                      "Skeleton Archer", this);
              things.add(skeletonArcher);
              skeletonArcher.spawn(pane);
              break;
            case "7":
              OrcBerserk orcBerserk = new OrcBerserk(TILE_SIZE * j, TILE_SIZE * i, 2,
                      "Orc Berserk", this);
              things.add(orcBerserk);
              orcBerserk.spawn(pane);
              break;
            case "8":
              Gorgon gorgon = new Gorgon(TILE_SIZE * j, TILE_SIZE * i, 2,
                      "Gorgon", this);
              things.add(gorgon);
              gorgon.spawn(pane);
              break;
            case "@":
              Portal portal = new Portal(TILE_SIZE * j, TILE_SIZE * i, 2, 50, 10, "Portal to City Hub",
                   this, "cityhub", "sheet1.png");
              usables.add(portal);
              portal.spawn(pane);
              break;
            case "A": // Extended ascii 181
              Portal portalToLevel1 = new Portal(TILE_SIZE * j, TILE_SIZE * i, 2, 50, 10, "Portal to level 1",
                   this, "level1", "sheet1.png");
              usables.add(portalToLevel1);
              portalToLevel1.spawn(pane);
              break;
            case "h":
              MiniHealthPickup miniHealthPickup = new MiniHealthPickup(TILE_SIZE * j, TILE_SIZE * i,
                  this);
              things.add(miniHealthPickup);
              miniHealthPickup.spawn(pane);
              break;
            case "e":
              ElixirOfYouth elixirOfYouth = new ElixirOfYouth(TILE_SIZE * j, TILE_SIZE * i, this);
              things.add(elixirOfYouth);
              elixirOfYouth.spawn(pane);
              break;
            case "v":
              Villager villager = new Villager(TILE_SIZE * j, TILE_SIZE * i, 1, 30, 10, "Villager",
                   this);
              usables.add(villager);
              things.add(villager);
              villager.spawn(pane);
              break;
            case "↑":
            case "↓":
            case "→":
            case "←":
              CannonOrientation orientation = null;
              if(currentTileValue.equals("↑")) {
               orientation = CannonOrientation.UP;
              }
              if(currentTileValue.equals("↓")) {
                orientation = CannonOrientation.DOWN;
              }
              if(currentTileValue.equals("→")) {
                orientation = CannonOrientation.RIGHT;
              }
              if(currentTileValue.equals("←")) {
                orientation = CannonOrientation.LEFT;
              }
                assert orientation != null;
                LaserCannon laserCannon = new LaserCannon(TILE_SIZE * j, TILE_SIZE * i, 1, "Laser Cannon",
                      this, orientation);
              things.add(laserCannon);
              laserCannon.spawn(pane);
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

  public void update() throws Throwable {
    if (!thingQueue.isEmpty()) {
      for (Thing i : thingQueue) {
        things.add(i.getMonster());
        i.getMonster().spawn(this.pane);
        thingQueue.remove(i);
      }
    }
    if (!removeThingQueue.isEmpty()) {
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

}
