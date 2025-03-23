package rpg.Common;

import java.util.List;

import rpg.Monsters.BaseMonster;

public interface Thing {
  public void update(List<Usable> usables) throws Throwable;

  public BaseMonster getMonster();
}
