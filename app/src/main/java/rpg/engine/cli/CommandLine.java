package rpg.engine.cli;

public enum CommandLine {
    INSTANCE;

    private final CommandLineUI commandLineUI;
    private CommandLineController commandLineController;

    CommandLine() {
        commandLineUI = new CommandLineUI();
    }

    public void activate() throws Throwable {
        commandLineController = new CommandLineController(commandLineUI.getTextInputDialog());
        commandLineController.handleResults();
    }
}
