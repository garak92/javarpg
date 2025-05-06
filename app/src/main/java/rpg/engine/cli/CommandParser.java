package rpg.engine.cli;

class CommandParser {
    private CommandParser() {
    }

    public static CliCommand parseCommand(String command) {
        CliCommand res = null;
        String value = "";

        try {
            String[] wordList = command.split(" ");
            if(wordList.length == 0) {
                throw new Exception("Please, write a valid command.  Write \"list\" for a list of commands.");
            }

            String verb = wordList[0].toLowerCase().trim();

            if(wordList.length == 2) {
                value = wordList[1].trim();
            }

            CliOperation cliOperation = CliOperation.valueOfStringVerb(verb);

            if(cliOperation == null) {
                throw new Exception("Command " + verb + " does not exist");
            }

            res = new CliCommand(cliOperation, value);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return res;
    }

}
