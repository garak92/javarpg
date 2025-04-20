package rpg.Common.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rpg.Common.LevelLoader;
import rpg.Levels.Level;
import rpg.Monsters.MonsterUtils;
import rpg.Monsters.Player;

public class CommandLineInterpreter {
   final static Logger logger = LoggerFactory.getLogger(CommandLineInterpreter.class);

   private CommandLineInterpreter() {
   }

   public static void executeCommand(CliCommand cliCommand) throws Throwable {
      switch (cliCommand.getCliOperation()) {
          case SPAWN_ENTITY -> {
              MonsterUtils.spawnMonster(cliCommand.getValue());
              logger.info("Trying to spawn entity");
          }
          case LOAD_LEVEL -> {
              LevelLoader.loadLevel(
                      new Level(cliCommand.getValue()).load());
              logger.info("Trying to load level");
          }
          case RESURRECT -> {
              if(Player.getInstance().isDead()) {
                  Player.getInstance().resurrect();
              }

              logger.info("Trying to resurrect player");
          }
      }
      }

}
