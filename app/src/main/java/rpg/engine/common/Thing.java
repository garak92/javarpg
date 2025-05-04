package rpg.engine.common;

import java.util.List;

import rpg.engine.monster.BaseMonster;

public interface Thing {
  public void update(List<Usable> usables) throws Throwable;

  public BaseMonster getMonster();
}
