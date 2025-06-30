package rpg.game.abilities;

import rpg.engine.common.ability.BaseAbility;
import rpg.engine.monster.BaseMonster;

public class FemaleSatyrFireballAttack extends BaseAbility {
    private final BaseMonster target;
    private double normalizedX = 0;
    private double normalizedY = 0;
    private double perpendicularX = 0;
    private double perpendicularY = 0;

    private double time = 0; // Controls sine wave phase

    public FemaleSatyrFireballAttack(BaseMonster monster, BaseMonster target) {
        super(15, 0, 0, monster);
        double directionX = target.getCharPosx() - monster.getCharPosx();
        double directionY = target.getCharPosy() - monster.getCharPosy();

        double length = Math.sqrt(directionX * directionX + directionY * directionY);
        normalizedX = directionX / length;
        normalizedY = directionY / length;

        // Compute perpendicular vector
        perpendicularX = -normalizedY;
        perpendicularY = normalizedX;

        this.target = target;
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

        if (monster.getMonster().detectCollision(monster.getLevel().getSolidTiles(),
                monster.getCharPosx(), monster.getCharPosy())) {
            monster.die();
        }

        double forwardStepX = normalizedX * monster.getVelocity();
        double forwardStepY = normalizedY * monster.getVelocity();

        // Amplitude of sine wave in pixels
        double waveAmplitude = 18;
        double sineOffset = Math.sin(time) * waveAmplitude;
        double waveOffsetX = perpendicularX * sineOffset;
        double waveOffsetY = perpendicularY * sineOffset;

        double newX = monster.getCharPosx() + forwardStepX + waveOffsetX;
        double newY = monster.getCharPosy() + forwardStepY + waveOffsetY;

        monster.setCharPosx(newX);
        monster.setCharPosy(newY);

        // How fast the wave oscillates
        double waveFrequency = 0.2;
        time += waveFrequency;
    }
}

