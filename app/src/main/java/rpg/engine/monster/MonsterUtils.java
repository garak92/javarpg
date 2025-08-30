package rpg.engine.monster;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
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

    public static boolean jumpToDirection(BaseMonster monster, double targetX, double targetY, double speed) {
        double dx = targetX - monster.getCharPosx();
        double dy = targetY - monster.getCharPosy();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            monster.setCharPosx(targetX);
            monster.setCharPosy(targetY);
            return true; // Reached destination
        }

        double nx = dx / distance;
        double ny = dy / distance;

        double newX = monster.getCharPosx() + nx * speed;
        double newY = monster.getCharPosy() + ny * speed;

        // Check for collisions
        if (monster.detectCollisionWithNodesPropsAndMonsters(monster.getLevel().getSolidTiles(), newX, newY)) {
            return true; // Blocked by environment
        }

        // Apply movement if not blocked
        monster.setCharPosx(newX);
        monster.setCharPosy(newY);
        return false; // Still moving toward target
    }



    public static void spawnMonster(String monsterClassName) {
        EnumMonsterKind selectedMonsterKind = null;
        Level level = Player.getInstance().level;
        int howFarFromThePlayerToSpawn = 400;

        for (EnumMonsterKind monsterKind : EnumMonsterKind.values()) {
            if (monsterClassName.equals(monsterKind.toString())) {
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
        } else if (baseMonster.getAlignment() == EnumMonsterAlignment.PROP) {
            level.getEnvProps().add(baseMonster);
        }


        level.getThings().add(baseMonster);

        level.updateEnemyList();
        baseMonster.spawn(level.getPane());
    }

    public static void spawnMonster(EnumMonsterKind monsterKind, int charPosX, int charPosY) {
        Level level = Player.getInstance().level;
        BaseMonster baseMonster = MonsterFactory.getMonster(monsterKind, charPosX, charPosY, level);

        if (baseMonster instanceof Usable) {
            level.getUsables().add((Usable) baseMonster);
        } else if (baseMonster.getAlignment() == EnumMonsterAlignment.PROP) {
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
        } else if (baseMonster.getAlignment() == EnumMonsterAlignment.PROP) {
            level.getEnvProps().add(baseMonster);
        }

        level.getThings().add(baseMonster);
        level.updateEnemyList();
        baseMonster.spawn(level.getPane());
    }

    public static void spawnMonster(BaseMonster baseMonster, Level level) {
        if (baseMonster instanceof Usable) {
            level.getUsables().add((Usable) baseMonster);
        } else if (baseMonster.getAlignment() == EnumMonsterAlignment.PROP) {
            level.getEnvProps().add(baseMonster);
        }

        level.getThings().add(baseMonster);
        level.updateEnemyList();
        baseMonster.spawn(level.getPane());
    }

    public static Rectangle2D calculateOpaqueBounds(Image image) {
        PixelReader reader = image.getPixelReader();
        if (reader == null) return new Rectangle2D(0, 0, image.getWidth(), image.getHeight());

        int minX = (int) image.getWidth();
        int minY = (int) image.getHeight();
        int maxX = 0;
        int maxY = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = reader.getColor(x, y);
                if (color.getOpacity() > 0.01) {
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;
                }
            }
        }

        if (minX > maxX || minY > maxY) {
            // Image is fully transparent
            return new Rectangle2D(0, 0, 0, 0);
        }

        return new Rectangle2D(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }
}
