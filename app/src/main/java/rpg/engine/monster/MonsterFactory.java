package rpg.engine.monster;

import rpg.engine.common.misc.Portal;
import rpg.engine.levels.Level;
import rpg.game.entities.enemy.cannon.CannonOrientation;
import rpg.game.entities.enemy.cannon.LaserCannon;
import rpg.game.entities.enemy.femalesatyr.FemaleSatyr;
import rpg.game.entities.enemy.firewizard.FireWizard;
import rpg.game.entities.enemy.gorgon.Gorgon;
import rpg.game.entities.enemy.karasu.Karasu;
import rpg.game.entities.enemy.malesatyr.MaleSatyr;
import rpg.game.entities.enemy.minotaur.Minotaur;
import rpg.game.entities.enemy.orcberserk.OrcBerserk;
import rpg.game.entities.enemy.orcshaman.OrcShaman;
import rpg.game.entities.enemy.skeletonarcher.SkeletonArcher;
import rpg.game.entities.enemy.skeletonspearman.SkeletonSpearman;
import rpg.game.entities.enemy.werewolf.Werewolf;
import rpg.game.entities.igrenne.Igrenne;
import rpg.game.entities.item.*;
import rpg.game.entities.player.Player;
import rpg.game.entities.villager.Villager;
import rpg.game.environment.*;

public class MonsterFactory {
    public static BaseMonster getMonster(EnumMonsterKind kind, int x, int y, Level level) {
        return switch (kind) {
            case PLAYER -> Player.initialize(x, y, 12, 100, 10, "Player 1", level.getStage(), null, level);
            case IGRENNE -> Igrenne.initialize(x, y, 10, 30, 10, "Igrenne", level);

            case SATYR -> new MaleSatyr(x, y, 2, "Male Satyr", level);
            case FEM_SATYR -> new FemaleSatyr(x, y, 2, "Female Satyr", level);
            case WEREWOLF -> new Werewolf(x, y, 2, "Werewolf", level);
            case MINOTAUR -> new Minotaur(x, y, 2, "Minotaur", level);
            case SKELETON_ARCHER -> new SkeletonArcher(x, y, 2, "Skeleton Archer", level);
            case SKELETON_SPEARMAN -> new SkeletonSpearman(x, y, 2, "Skeleton Spearman", level);
            case ORC_BERSERK -> new OrcBerserk(x, y, 2, "Orc Berserk", level);
            case ORC_SHAMAN -> new OrcShaman(x, y, 2, "Orc Shaman", level);
            case GORGON -> new Gorgon(x, y, 2, "Gorgon", level);
            case KARASU -> new Karasu(x, y, 2, "Karasu", level);
            case FIRE_WIZARD -> new FireWizard(x, y, 2, "Fire Wizard", level);
            case MINI_HEALTH_PICKUP -> new MiniHealthPickup(x, y, level);
            case MEDKIT -> new Medkit(x, y, level);
            case MEGA_HEALTH -> new MegaHealth(x, y, level);

            // Quest items
            case VESSEL_OF_MINERALS -> new VesselOfMinerals(x, y, level);
            case SCROLL_OF_ANTIBIOTICS -> new ScrollOfAntibiotics(x, y, level);
            case SCIENTIFIC_INSTRUMENT -> new ScientificInstrument(x, y, level);

            // Props
            case TREE -> new Tree(x, y, level);
            case ROCK -> new Rock(x, y, level);
            case BUSH -> new Bush(x, y, level);
            case HOUSE -> new House(x, y, level);

            case VILLAGER -> new Villager(x, y, 1, 30, 10, "Villager", level);
            case CATTLE -> new Cattle(x, y, 1, 30, 10, "Cattle", level);

            case LASER_CANNON_UP -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.UP);
            case LASER_CANNON_DOWN -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.DOWN);
            case LASER_CANNON_RIGHT -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.RIGHT);
            case LASER_CANNON_LEFT -> new LaserCannon(x, y, 1, "Laser Cannon", level, CannonOrientation.LEFT);
            case STATUE -> new Statue(x, y, level);
            case TENT -> new Tent(x, y, level);
            case FOUNTAIN -> new Fountain(x, y, level);
            case STABLE -> new Stable(x, y, level);
            case TOMBSTONE -> new Tombstone(x, y, level);
            case FURNACE -> new Furnace(x, y, level);
            case ANVIL -> new Anvil(x, y, level);
        };
    }
}

