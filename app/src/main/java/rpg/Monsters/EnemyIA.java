package rpg.Monsters;

public interface EnemyIA {
  public EnumEnemyStates getCurrentState();

  public void transition(EnumEvents event);
}
