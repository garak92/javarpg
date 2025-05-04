package rpg.engine.monster;

import rpg.engine.common.misc.Portal;
import rpg.game.environment.Bush;
import rpg.game.environment.House;
import rpg.game.environment.Rock;
import rpg.game.environment.Tree;
import rpg.engine.levels.Level;
import rpg.game.entities.enemy.gorgon.Gorgon;
import rpg.game.entities.enemy.malesatyr.MaleSatyr;
import rpg.game.entities.enemy.minotaur.Minotaur;
import rpg.game.entities.enemy.orcberserk.OrcBerserk;
import rpg.game.entities.enemy.cannon.CannonOrientation;
import rpg.game.entities.enemy.cannon.LaserCannon;
import rpg.game.entities.enemy.skeletonarcher.SkeletonArcher;
import rpg.game.entities.enemy.werewolf.Werewolf;
import rpg.game.entities.igrenne.Igrenne;
import rpg.game.entities.item.ElixirOfYouth;
import rpg.game.entities.item.MiniHealthPickup;
import rpg.game.entities.player.Player;
import rpg.game.entities.villager.Villager;

public class MonsterFactory {

    public static BaseMonster getMonster(EnumMonsterKind kind, int x, int y, Level level) {
        return switch (kind) {
            case PLAYER -> Player.initialize(x, y, 12, 100, 10, "Player 1", level.getStage(), null, level);
            case IGRENE -> Igrenne.initialize(x, y, 10, 30, 10, "Igrene", level);

            case SATYR -> new MaleSatyr(x, y, 2, "Male Satyr", level);
            case WEREWOLF -> new Werewolf(x, y, 2, "Werewolf", level);
            case MINOTAUR -> new Minotaur(x, y, 2, "Minotaur", level);
            case SKELETON_ARCHER -> new SkeletonArcher(x, y, 2, "Skeleton Archer", level);
            case ORC_BERSERK -> new OrcBerserk(x, y, 2, "Orc Berserk", level);
            case GORGON -> new Gorgon(x, y, 2, "Gorgon", level);

            case PORTAL_CITY_HUB -> new Portal(x, y, 2, 50, 10, "Portal to City Hub", level, "cityhub", "sheet1.png");
            case PORTAL_LEVEL1 -> new Portal(x, y, 2, 50, 10, "Portal to level 1", level, "level1", "sheet1.png");

            case MINI_HEALTH_PICKUP -> new MiniHealthPickup(x, y, level);
            case ELIXIR_OF_YOUTH -> new ElixirOfYouth(x, y, level);

            case TREE -> new Tree(x, y, level);
            case ROCK -> new Rock(x, y, level);
            case BUSH -> new Bush(x, y, level);
            case HOUSE -> new House(x, y, level);

            case VILLAGER -> new Villager(x, y, 1, 30, 10, "Villager", level);

            case LASER_CANNON_UP -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.UP);
            case LASER_CANNON_DOWN -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.DOWN);
            case LASER_CANNON_RIGHT -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.RIGHT);
            case LASER_CANNON_LEFT -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.LEFT);
        };
    }
}

