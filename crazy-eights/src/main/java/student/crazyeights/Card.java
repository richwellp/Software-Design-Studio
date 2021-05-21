package student.crazyeights;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

/*
 * ========================================
 * You should not need to modify this file.
 * ========================================
 */

/**
 * Represents a standard playing card from a 52 card deck.
 */
public class Card {

    public enum Suit {DIAMONDS, HEARTS, SPADES, CLUBS}

    public enum Rank {
        ACE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT,
        NINE,
        TEN,
        JACK,
        QUEEN,
        KING
    }

    private static final int FACE_CARD_VALUE = 10;
    private static final int EIGHT_CARD_VALUE = 50;

    private static final int DECK_SIZE = Suit.values().length * Rank.values().length;

    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    /**
     * Returns the Crazy8s point value for this card.
     *
     * @return An integer representing this card's point value
     */
    public int getPointValue() {
        if (rank.ordinal() >= Rank.JACK.ordinal()) {
            return FACE_CARD_VALUE;
        }

        if (rank == Rank.EIGHT) {
            return EIGHT_CARD_VALUE;
        }

        // Otherwise, return numeric value of card
        return rank.ordinal() + 1;
    }

    /**
     * Creates a new, unshuffled deck of standard playing cards.
     *
     * @return A list representing an unshuffled deck of cards
     */
    public static List<Card> getDeck() {
        List<Card> cardDeck = new ArrayList<>(DECK_SIZE);

        for (Suit suit : EnumSet.allOf(Suit.class)) {
            for (Rank rank : EnumSet.allOf(Rank.class)) {
                cardDeck.add(new Card(suit, rank));
            }
        }

        return cardDeck;
    }

    // Convenience methods; you might or might not need these.

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return suit == card.suit &&
            rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
