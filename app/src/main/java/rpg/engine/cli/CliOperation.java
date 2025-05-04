package rpg.engine.cli;

public enum CliOperation {
    SPAWN_ENTITY("spawn") {
        public String apply(String value) {
            return "VALUE: " + value;
        };
    },
    RESURRECT("resurrect") {
        public String apply(String value) {
            return "VALUE: " + value;
        };
    },
    LOAD_LEVEL("map") {
        public String apply(String value) {
            return "VALUE: " + value;
        };
    };

    private final String symbol;

    CliOperation(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
       return symbol;
    }

    public abstract String apply(String value);

    public static CliOperation valueOfStringVerb(String verb) {
        for(CliOperation cliOperation: CliOperation.values()) {
            if(cliOperation.symbol.equals(verb)) {
                return cliOperation;
            }
        }

        return null;
    }

}
