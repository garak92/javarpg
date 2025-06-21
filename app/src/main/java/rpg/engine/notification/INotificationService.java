package rpg.engine.notification;

public interface INotificationService {
    public void pushNotification(String notificationText, long delayInMillis);
}
