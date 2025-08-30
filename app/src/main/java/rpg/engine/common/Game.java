package rpg.engine.common;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.levels.Level;
import rpg.engine.music.MusicSystem;
import rpg.engine.quest.QuestLoader;

import java.util.function.Consumer;

public class Game extends Application {
    private static final Logger log = LoggerFactory.getLogger(Game.class);
    private static Game applicationInstance;
    final int WIDTH = 1920;
    final int HEIGHT = 1080;
    GameLoop gameLoop = null;
    Stage primaryStage = null;
    Consumer<Integer> fpsReporter = fps -> log.info("FPS: {}", fps);
    Runnable renderer;
    StackPane root = new StackPane();
    private Level currentLevel = null;
    private boolean isPaused = false;

    public static Game getInstance() {
        return applicationInstance;
    }

    public void setCurrentLevel(Level newLevel) {
        this.currentLevel = newLevel;
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
        Pane levelPane = new Pane();
        root.getChildren().add(levelPane);
        primaryStage = new Stage();
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
            Level currentLevel = new Level("SunVillage", "sheet1.png", levelPane, primaryStage).load();

            // Initialize game loop

            renderer = () -> {
                try {
                    currentLevel.render();
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };

            gameLoop = new GameLoop(currentLevel, fpsReporter, renderer);
            gameLoop.setMaximumStep(0.0166f);
            gameLoop.start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void restart() {
        cleanup();
        gameLoop.stop();
        renderer = () -> {
            try {
                currentLevel.render();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
        gameLoop = new GameLoop(currentLevel, fpsReporter, renderer);
        gameLoop.setMaximumStep(0.0166f);
        gameLoop.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        MusicSystem.INSTANCE.close();
    }

    public void pauseGame() {
        gameLoop.stop();
        isPaused = true;
    }

    public void unpauseGame() {
        gameLoop.start();
        isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public StackPane getRoot() {
        return root;
    }
}
