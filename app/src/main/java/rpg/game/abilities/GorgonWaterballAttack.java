package rpg.game.abilities;

import rpg.engine.common.ability.BaseAbility;
import rpg.engine.monster.BaseMonster;

public class GorgonWaterballAttack extends BaseAbility {
    private final BaseMonster target;
    private double normalizedX = 0;
    private double normalizedY = 0;

    public GorgonWaterballAttack(BaseMonster monster, BaseMonster target, double targetPosX, double targetPosY) {
        super(15, 0, 0, monster);
        this.target = target;

        double directionX = targetPosX - monster.getCharPosx();
        double directionY = targetPosY - monster.getCharPosy();

        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        normalizedX = directionX / length;
        normalizedY = directionY / length;
    }

    @Override
    protected void dealDamage() {
        if (monster.getMonster().detectCollision(target)) {
            target.receiveDamage(damageDealt);
            monster.die();
        }
    }

    @Override
    public void update() {
        dealDamage();
        if (monster.getMonster().detectCollision(monster.getLevel().getSolidTiles(), monster.getCharPosx(),
                monster.getCharPosy())) {
            monster.die();
        }

        monster.setCharPosx(monster.getCharPosx() + normalizedX * monster.getVelocity());
        monster.setCharPosy(monster.getCharPosy() + normalizedY * monster.getVelocity());

    }
}
