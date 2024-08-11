package rpg.Abilities;

import java.util.List;
import java.util.stream.Collectors;

import rpg.Common.Thing;
import rpg.Common.Usable;
import rpg.Monsters.BaseMonster;

public class BringerFireballAttack extends BaseEnemyAttack {
  public BringerFireballAttack(BaseMonster monster) {
    super(2, 0, 0, monster);
  }

  @Override
  public void dealDamage() {
    List<Thing> things = monster.getLevel().getThings();
    List<BaseMonster> monsters = things.stream().map(v -> v.getMonster()).collect(Collectors.toList());

    for (BaseMonster i : monsters) {
      if (monster.getMonster().detectCollision(i)) {
        i.receiveDamage(damageDealt);
      }
      ;
    }
    ;
  }

  @Override
  public void update(List<Usable> usables) {
    monster.update(usables);
  }
}
