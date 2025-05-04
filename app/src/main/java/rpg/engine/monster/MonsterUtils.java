package rpg.engine.monster;

import javafx.animation.Animation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.engine.common.Usable;
import rpg.engine.levels.Level;
import rpg.game.entities.player.Player;

public class MonsterUtils {
    final static Logger logger = LoggerFactory.getLogger(MonsterUtils.class);

    private MonsterUtils() {
    }

    public static void playAnimationOnlyOnce(Animation animation) {
        animation.stop();
        animation.setCycleCount(1);
        animation.play();
    }

    public static void jumpToDirection(BaseMonster monster, double targetPosX, double targetPosY, double lerpFactor) {
        lerpFactor = Math.max(0, Math.min(1, lerpFactor));

        double currentX = monster.getCharPosx();
        double currentY = monster.getCharPosy();

        if(monster.detectCollision(monster.getLevel().getSolidTiles())) {
            double newX = currentX - (targetPosX - currentX) * lerpFactor;
            double newY = currentY - (targetPosY - currentY) * lerpFactor;

            monster.setCharPosx(newX);
            monster.setCharPosy(newY);

            monster.getImageView().setLayoutX(newX);
            monster.getImageView().setLayoutY(newY);
            return;
        }

        double newX = currentX + (targetPosX - currentX) * lerpFactor;
        double newY = currentY + (targetPosY - currentY) * lerpFactor;

        monster.setCharPosx(newX);
        monster.setCharPosy(newY);

        monster.getImageView().setLayoutX(newX);
        monster.getImageView().setLayoutY(newY);
    }


    public static void spawnMonster(String monsterClassName) {
        EnumMonsterKind selectedMonsterKind = null;
        Level level = Player.getInstance().level;
        int howFarFromThePlayerToSpawn = 400;

        for(EnumMonsterKind monsterKind: EnumMonsterKind.values()) {
            if(monsterClassName.equals(monsterKind.toString())) {
               selectedMonsterKind = monsterKind;
            }
        }

        assert selectedMonsterKind != null;
        assert level != null;
        BaseMonster baseMonster = MonsterFactory.getMonster(selectedMonsterKind, (int) (Player
                .getInstance().getCharPosx() + howFarFromThePlayerToSpawn), (int) Player
                .getInstance().getCharPosy(), level);

        if (baseMonster instanceof Usable) {
            level.getUsables().add((Usable) baseMonster);
        } else if(baseMonster.getAlignment() == EnumMonsterAlignment.PROP) {
            level.getEnvProps().add(baseMonster);
        }


        level.getThings().add(baseMonster);

        level.updateEnemyList();
        baseMonster.spawn(level.getPane());
    }

    public static void spawnMonster(EnumMonsterKind monsterKind, int charPosX, int charPosY, Level level) {
        BaseMonster baseMonster = MonsterFactory.getMonster(monsterKind, charPosX, charPosY, level);

        if (baseMonster instanceof Usable) {
            level.getUsables().add((Usable) baseMonster);
        } else if(baseMonster.getAlignment() == EnumMonsterAlignment.PROP) {
            level.getEnvProps().add(baseMonster);
        }

        level.getThings().add(baseMonster);

        baseMonster.spawn(level.getPane());
    }
}
