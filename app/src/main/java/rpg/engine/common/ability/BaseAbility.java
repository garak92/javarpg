package rpg.engine.common.ability;

import rpg.engine.monster.BaseMonster;

public abstract class BaseAbility extends Ability {
  protected int damageDealt;
  protected int coolDownTime;
  protected float splashDamage;

  public BaseAbility(int damageDealt, int coolDownTime, float splashDamage, BaseMonster monster) {
    super(monster);
    this.damageDealt = damageDealt;
    this.coolDownTime = coolDownTime;
    this.splashDamage = splashDamage;
  }

  protected abstract void dealDamage();
}
