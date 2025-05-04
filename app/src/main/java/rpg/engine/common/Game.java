package rpg.engine.common;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.music.MusicSystem;
import rpg.engine.quest.QuestLoader;
import rpg.engine.levels.Level;

import java.util.function.Consumer;

public class Game extends Application {
  private static final Logger log = LoggerFactory.getLogger(Game.class);
  private static Game applicationInstance;
  GameLoop gameLoop = null;
  private Level currentLevel = null;
  Stage primaryStage = null;
  Consumer<Integer> fpsReporter = fps -> log.info("FPS: {}", fps);

  final int WIDTH = 1920;
  final int HEIGHT = 1080;

  public void setCurrentLevel(Level newLevel) {
    this.currentLevel = newLevel;
  }

  public static Game getInstance() {
    return applicationInstance;
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  @Override
  public void init() {
    applicationInstance = this;
  }

  void cleanup() {
    if (gameLoop != null) {
      gameLoop.stop();
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    // Initialize JavaFx
    primaryStage = new Stage();
    Pane root = new Pane();
    Scene scene = new Scene(root, WIDTH, HEIGHT);
    scene.setCursor(Cursor.NONE);
    scene.setFill(null);
    primaryStage.setTitle("My RPG");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.setWidth(WIDTH);
    primaryStage.setHeight(HEIGHT);
    primaryStage.setFullScreen(true);
    primaryStage.setAlwaysOnTop(true);
    primaryStage.show();
    root.setStyle("-fx-background-color: transparent;");
    root.setPrefSize(WIDTH, HEIGHT);
    try {
      // Initialize quests
      QuestLoader.loadQuests();

      // Initialize first level
      Level currentLevel = new Level("cityhub", "sheet1.png", root, primaryStage).load();

      // Initialize game loop
      gameLoop = new GameLoop(currentLevel, fpsReporter);
      gameLoop.setMaximumStep(60);
      gameLoop.start();
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public void restart() {
    cleanup();
    gameLoop.stop();
    gameLoop = new GameLoop(currentLevel, fpsReporter);
    gameLoop.start();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    MusicSystem.INSTANCE.close();
  }
}
