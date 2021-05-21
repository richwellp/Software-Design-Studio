package mineopoly_two.action;

public enum TurnAction {
    MOVE_UP(new MoveAction(0, 1)),
    MOVE_DOWN(new MoveAction(0, -1)),
    MOVE_RIGHT(new MoveAction(1, 0)),
    MOVE_LEFT(new MoveAction(-1, 0)),
    MINE(new TileInteractAction()),
    PICK_UP(new TileInteractAction());

    private Action actionToPerform;

    TurnAction(Action actionToPerform) {
        this.actionToPerform = actionToPerform;
    }

    public Action getActionToPerform() {
        return actionToPerform;
    }
}
