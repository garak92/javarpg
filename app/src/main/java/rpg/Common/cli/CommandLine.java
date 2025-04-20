package rpg.Common.cli;

import rpg.Game;

public enum CommandLine {
    INSTANCE;

    private CommandLineUI commandLineUI;
    private CommandLineController commandLineController;

    CommandLine() {
       commandLineUI = new CommandLineUI();
    }

    public void activate() throws Throwable {
       commandLineController = new CommandLineController(commandLineUI.getTextInputDialog());
       commandLineController.handleResults();
    }
}
