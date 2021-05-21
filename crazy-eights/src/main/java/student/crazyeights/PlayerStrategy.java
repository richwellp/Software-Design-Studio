package student.crazyeights;

import java.util.List;

/*
 * =======================
 * DO NOT MODIFY THIS FILE
 * =======================
 */

/**
 * A contract for how a Crazy8's player will interact with a Crazy8's game engine.
 * <p>
 * A game engine would call these methods to interact with implementors of this interface in order
 * to orchestrate a game of Crazy8's.
 */
public interface PlayerStrategy {

    /**
     * Gives the player their assigned id, as well as a list of the opponents' assigned ids.
     * <p>
     * This method will be called by the game engine once at the very beginning (before any games
     * are started), to allow the player to set up any initial state.
     *
     * @param playerId    The id for this player, assigned by the game engine
     * @param opponentIds A list of ids for this player's opponents
     */
    void init(int playerId, List<Integer> opponentIds);

    /**
     * Called once at the beginning of o game to deal the player their initial cards.
     *
     * @param cards The initial list of cards dealt to this player
     */
    void receiveInitialCards(List<Card> cards);

    /**
     * Called to ask whether the player wants to draw this turn. Gives this player the top card of
     * the discard pile at the beginning of their turn, as well as an optional suit for the pile in
     * case a "8" was played, and the suit was changed.
     * <p>
     * By having this return true, the game engine will then call receiveCard() for this player.
     * Otherwise, playCard() will be called.
     *
     * @param topPileCard The card currently at the top of the pile
     * @param changedSuit The suit that the pile was changed to as the result of an "8" being
     *                    played. Will be null if no "8" was played.
     * @return whether or not the player wants to draw
     */
    boolean shouldDrawCard(Card topPileCard, Card.Suit changedSuit);

    /**
     * Called when this player has chosen to draw a card from the deck.
     *
     * @param drawnCard The card that this player has drawn
     */
    void receiveCard(Card drawnCard);

    /**
     * Called when this player is ready to play a card (will not be called if this player drew on
     * their turn).
     * <p>
     * This will end this player's turn.
     *
     * @return The card this player wishes to put on top of the pile
     */
    Card playCard();

    /**
     * Called if this player decided to play a "8" card to ask the player what suit they would like
     * to declare.
     * <p>
     * This player should then return the Card.Suit enum that it wishes to set for the discard
     * pile.
     */
    Card.Suit declareSuit();

    /**
     * Called at the very beginning of this player's turn to give it context of what its opponents
     * chose to do on each of their turns.
     *
     * @param opponentActions A list of what the opponents did on each of their turns
     */
    void processOpponentActions(List<PlayerTurn> opponentActions);

    /**
     * Called before a game begins, to allow for resetting any state between games.
     */
    void reset();
}
