package rpg.engine.ai;

public class StateTransition {
    private final EnumEnemyStates fromState;
    private final EnumEvents event;
    private final EnumEnemyStates toState;

    public StateTransition(EnumEnemyStates fromState, EnumEvents event, EnumEnemyStates toState) {
        this.fromState = fromState;
        this.event = event;
        this.toState = toState;
    }

    public EnumEnemyStates getToState() {
        return toState;
    }

    public EnumEvents getEvent() {
        return event;
    }

    public EnumEnemyStates getFromState() {
        return fromState;
    }
}
