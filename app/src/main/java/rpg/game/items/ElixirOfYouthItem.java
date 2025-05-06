package rpg.game.items;

import rpg.engine.common.item.BaseItem;
import rpg.engine.monster.BaseMonster;
import rpg.game.entities.player.Player;

public class ElixirOfYouthItem extends BaseItem {
  private Player target;

  public ElixirOfYouthItem(BaseMonster monster, Player target) {
    super(0, monster);
    this.target = target;
  }

  @Override
  protected void useItem() {
    if (monster.getMonster().detectCollision(target)) {
      monster.die();
    }
  }

  @Override
  public void update() {
    useItem();
  }
}
