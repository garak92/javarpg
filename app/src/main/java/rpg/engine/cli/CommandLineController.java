package rpg.engine.cli;

import javafx.scene.control.TextInputDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CommandLineController {
    protected static Logger logger = LoggerFactory.getLogger(CommandLineController.class);
    TextInputDialog textInputDialog;

    public CommandLineController(TextInputDialog textInputDialog) {
        this.textInputDialog = textInputDialog;
    }

    public void handleResults() throws Throwable {
        Optional<String> result = textInputDialog.showAndWait();
        if(result.isPresent()) {
            handleInputCommand(result.get());
        }
    }

    private void handleInputCommand(String command) throws Throwable {
        CliCommand cliCommand = CommandParser.parseCommand(command);
        CommandLineInterpreter.executeCommand(cliCommand);
    }
}
