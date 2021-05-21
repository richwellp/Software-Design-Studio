package mineopoly_two.action;

import mineopoly_two.game.GameBoard;
import mineopoly_two.game.MinePlayer;

public abstract class Action {
    /**
     * Performs the action on the GameBoard specified by the specific Action subclass
     *
     * @param board The board that may be modified as a result of this action
     * @param player The MinePlayer performing the action
     * @param action The TurnAction enum representing the Action being performed
     */
    public abstract void performAction(GameBoard board, MinePlayer player, TurnAction action);
}
