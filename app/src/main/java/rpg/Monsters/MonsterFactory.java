package rpg.Monsters;

import rpg.Environment.Bush;
import rpg.Environment.House;
import rpg.Environment.Rock;
import rpg.Environment.Tree;
import rpg.Game;
import rpg.Levels.Level;
import rpg.Monsters.Enemy.Gorgon.Gorgon;
import rpg.Monsters.Enemy.MaleSatyr.MaleSatyr;
import rpg.Monsters.Enemy.Minotaur.Minotaur;
import rpg.Monsters.Enemy.OrcBerserk.OrcBerserk;
import rpg.Monsters.Enemy.Props.Cannon.CannonOrientation;
import rpg.Monsters.Enemy.Props.Cannon.LaserCannon;
import rpg.Monsters.Enemy.SkeletonArcher.SkeletonArcher;
import rpg.Monsters.Enemy.Werewolf.Werewolf;
import rpg.Monsters.Igrene.Igrene;
import rpg.Monsters.Villager.Villager;

public class MonsterFactory {

    public static BaseMonster getMonster(EnumMonsterKind kind, int x, int y, Level level) {
        return switch (kind) {
            case PLAYER -> Player.initialize(x, y, 12, 100, 10, "Player 1", level.getStage(), null, level);
            case IGRENE -> Igrene.initialize(x, y, 10, 30, 10, "Igrene", level);

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

