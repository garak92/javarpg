package rpg.engine.notification;

public interface INotificationService {
    public void pushNotification(String notificationText, long delayInMillis);

    void pushNotificationWithoutDelay(String notificationText);

    void removeCurrentNotification();
}
