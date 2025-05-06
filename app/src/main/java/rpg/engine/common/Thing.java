package rpg.engine.common;

import rpg.engine.monster.BaseMonster;

import java.util.List;

public interface Thing {
  public void update(List<Usable> usables) throws Throwable;
  public void render() throws Throwable;

  public BaseMonster getMonster();
}
