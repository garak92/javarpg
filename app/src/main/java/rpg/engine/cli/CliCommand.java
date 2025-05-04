package rpg.engine.cli;

public class CliCommand {
    private final CliOperation cliOperation;
    private final String value;

    public CliCommand(CliOperation cliOperation, String value) {
        this.cliOperation = cliOperation;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public CliOperation getCliOperation() {
        return cliOperation;
    }
}
