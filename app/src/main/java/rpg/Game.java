package rpg;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import rpg.Levels.Level;

public class Game extends Application {
  AnimationTimer gameLoop;
  Image tileSheet;
  ImageView imageView;
  ImageView worldView;

  final int WIDTH = 1920;
  final int HEIGHT = 1080;

  @Override
  public void start(Stage primaryStage) {
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

    // Initialize level
    Level level = new Level("level1", "sheet1.png", root, primaryStage).load();

    gameLoop = new AnimationTimer() {
      @Override
      public void handle(long now) {
        level.update();
      }
    };

    gameLoop.start();

  }
}
