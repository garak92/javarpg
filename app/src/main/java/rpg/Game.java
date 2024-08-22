package rpg;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import rpg.Common.MusicSystem;
import rpg.Common.QuestLoader;
import rpg.Levels.Level;

public class Game extends Application {
  private static Game applicationInstance;
  AnimationTimer gameLoop;
  private Level currentLevel = null;
  Image tileSheet;
  ImageView imageView;
  ImageView worldView;
  Stage primaryStage = null;
  Pane pane = null;

  final int WIDTH = 1920;
  final int HEIGHT = 1080;

  public void setCurrentLevel(Level newLevel) {
    this.currentLevel = newLevel;
  }

  public static Game getInstance() {
    return applicationInstance;
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

  Level startGame(Stage primaryStage) throws Exception {
    restart(primaryStage);
    // Initialize JavaFx
    Pane root = new Pane();
    Scene scene = new Scene(root, WIDTH, HEIGHT);
    primaryStage.setTitle("My RPG");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.setWidth(WIDTH);
    primaryStage.setHeight(HEIGHT);
    primaryStage.setFullScreen(true);
    primaryStage.show();
    root.setStyle("-fx-background-color: transparent;");
    root.setPrefSize(WIDTH, HEIGHT);

    try {
      // Initialize quests
      QuestLoader.loadQuests();

      // Initialize level
      Level currentLevel = new Level("cityhub", "sheet1.png", root, primaryStage).load();

      return currentLevel;

    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  public void restart(Stage stage) throws Exception {
    cleanup();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    currentLevel = startGame(primaryStage);
    gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        currentLevel.update();
      }
    };

    gameLoop.start();

  }

  @Override
  public void stop() throws Exception {
    super.stop();
    MusicSystem.INSTANCE.close();
  }
}
