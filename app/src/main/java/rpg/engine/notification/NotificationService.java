package rpg.engine.notification;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import rpg.engine.common.Game;
import rpg.game.entities.player.Player;

public class NotificationService implements INotificationService {
    @Override
    public void pushNotification(String notificationText, long delayInMillis) {
        new Thread(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Notification notification = Notification.valueOf(notificationText);
            Pane pane = Player.getInstance().getLevel().getPane();
            Platform.runLater(
                    () -> {
                        pane.getChildren().addAll(notification.getBox(), notification.getTextFlow());
                    }
            );
            try {
                Game.getInstance().pauseGame();
                Thread.sleep(delayInMillis);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                Platform.runLater(
                        () -> {
                            pane.getChildren().removeAll(notification.getBox(), notification.getTextFlow());
                            Game.getInstance().unpauseGame();
                        }
                );
            }
        }).start();
    }
}
