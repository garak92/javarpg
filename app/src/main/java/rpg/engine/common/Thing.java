package rpg.engine.common;

import java.util.List;

import rpg.engine.monster.BaseMonster;
import rpg.engine.render.IRenderer;

public interface Thing {
  public void update(List<Usable> usables) throws Throwable;
  public void render() throws Throwable;

  public BaseMonster getMonster();
}
