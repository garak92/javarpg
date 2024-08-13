package rpg.Abilities;

import rpg.Monsters.BaseMonster;

public abstract class BaseEnemyAttack extends EnemyAttack {
  protected int damageDealt;
  protected int coolDownTime;
  protected float splashDamage;

  public BaseEnemyAttack(int damageDealt, int coolDownTime, float splashDamage, BaseMonster monster) {
    super(monster);
    this.damageDealt = damageDealt;
    this.coolDownTime = coolDownTime;
    this.splashDamage = splashDamage;
  }

  protected abstract void dealDamage();
}
