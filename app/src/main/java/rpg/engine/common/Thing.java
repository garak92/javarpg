package rpg.engine.common;

import rpg.engine.monster.BaseMonster;

import java.util.List;

public interface Thing {
  void update(List<Usable> usables) throws Throwable;
  void render() throws Throwable;

  BaseMonster getMonster();
}
