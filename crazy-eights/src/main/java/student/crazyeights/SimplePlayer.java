package student.crazyeights;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimplePlayer implements PlayerStrategy {
    /** Current player's id. */
    private int playerId;

    /** List containing other player's id. */
    private List<Integer> opponentIds;

    /** List of cards the player is holding. */
    private List<Card> currentCards = new ArrayList<>();

    /** the current discarded card. */
    private Card topPileCard;

    /** Null if the topPileCard is not eight. Represents the declared suit. */
    private Card.Suit changedSuit;

    /**
     * Gives the player their assigned id, as well as a list of the opponents' assigned ids.
     * <p>
     * This method will be called by the game engine once at the very beginning (before any games
     * are started), to allow the player to set up any initial state.
     *
     * @param playerId    The id for this player, assigned by the game engine
     * @param opponentIds A list of ids for this player's opponents
     */
    @Override
    public void init(int playerId, List<Integer> opponentIds) {
        this.playerId = playerId;
        this.opponentIds = opponentIds;
    }

    /**
     * Called once at the beginning of o game to deal the player their initial cards.
     *
     * @param cards The initial list of cards dealt to this player
     */
    @Override
    public void receiveInitialCards(List<Card> cards) {
        for (Card card: cards) {
            currentCards.add(card);
        }
    }

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
    @Override
    public boolean shouldDrawCard(Card topPileCard, Card.Suit changedSuit) {
        this.topPileCard = topPileCard;
        this.changedSuit = changedSuit;
        for (Card card: currentCards) {
            if (changedSuit != null) {
                if (card.getSuit().equals(changedSuit)) {
                    return true;
                }
            } else {
                if (topPileCard.getPointValue() == card.getPointValue()
                        || topPileCard.getSuit() == card.getSuit()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Called when this player has chosen to draw a card from the deck.
     *
     * @param drawnCard The card that this player has drawn
     */
    @Override
    public void receiveCard(Card drawnCard) {
        currentCards.add(drawnCard);
    }

    /**
     * Called when this player is ready to play a card (will not be called if this player drew on
     * their turn).
     * <p>
     * This will end this player's turn.
     *
     * @return The card this player wishes to put on top of the pile
     */
    @Override
    public Card playCard() {
        for (Card card: currentCards) {
            if (changedSuit != null) {
                if (card.getSuit().equals(changedSuit)) {
                    changedSuit = null;
                    currentCards.remove(card);
                    return card;
                }
            } else {
                if (topPileCard.getPointValue() == card.getPointValue()) {
                    currentCards.remove(card);
                    return card;
                }
            }
        }
        return null;
    }

    /**
     * Called if this player decided to play a "8" card to ask the player what suit they would like
     * to declare.
     * <p>
     * This player should then return the Card.Suit enum that it wishes to set for the discard
     * pile.
     */
    @Override
    public Card.Suit declareSuit() {
        Random random = new Random();
        int suit = random.nextInt(4);
        if (suit == 0) {
            return Card.Suit.DIAMONDS;
        } else if (suit == 1) {
            return Card.Suit.HEARTS;
        } else if (suit == 2) {
            return Card.Suit.SPADES;
        } else {
            return Card.Suit.CLUBS;
        }
    }

    /**
     * Called at the very beginning of this player's turn to give it context of what its opponents
     * chose to do on each of their turns.
     *
     * @param opponentActions A list of what the opponents did on each of their turns
     */
    @Override
    public void processOpponentActions(List<PlayerTurn> opponentActions) {

    }

    /**
     * Called before a game begins, to allow for resetting any state between games.
     */
    @Override
    public void reset() {
        currentCards.clear();
    }
}
