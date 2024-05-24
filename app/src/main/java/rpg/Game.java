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

  final int WIDTH = 300;
  final int HEIGHT = 300;

  @Override
  public void start(Stage primaryStage) {
    // Initialize JavaFx
    Pane root = new Pane();
    primaryStage.setTitle("My RPG");
    primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
    primaryStage.setResizable(false);
    primaryStage.setWidth(WIDTH);
    primaryStage.setHeight(HEIGHT);
    primaryStage.show();
    root.setStyle("-fx-background-color: black;");
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
