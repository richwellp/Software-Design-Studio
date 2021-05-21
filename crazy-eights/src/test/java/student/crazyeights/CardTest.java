package student.crazyeights;

import static student.crazyeights.Card.Rank;
import static student.crazyeights.Card.Suit;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/*
 * =======================================================
 * DO NOT PUT YOUR TESTS IN THIS FILE!
 *
 * Write your own test classes for any classes you create.
 * =======================================================
 */

public class CardTest {

    private List<Card> deck;

    @Before
    public void setUp() {
        deck = Card.getDeck();
    }

    @Test
    public void faceCardsWorthTenPoints() {
        List<Card> faceCards = Arrays.asList(
            new Card(Suit.DIAMONDS, Rank.JACK),
            new Card(Suit.HEARTS, Rank.QUEEN),
            new Card(Suit.SPADES, Rank.KING)
        );

        for (Card card : faceCards) {
            assertEquals(10, card.getPointValue());
        }
    }

    @Test
    public void eightsWorthFiftyPoints() {
        List<Card> eights = Arrays.asList(
            new Card(Suit.DIAMONDS, Rank.EIGHT),
            new Card(Suit.HEARTS, Rank.EIGHT),
            new Card(Suit.SPADES, Rank.EIGHT),
            new Card(Suit.CLUBS, Rank.EIGHT)
        );

        for (Card card : eights) {
            assertEquals(50, card.getPointValue());
        }
    }

    @Test
    public void acesWorthOnePoint() {
        List<Card> aces = Arrays.asList(
            new Card(Suit.DIAMONDS, Rank.ACE),
            new Card(Suit.HEARTS, Rank.ACE),
            new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.CLUBS, Rank.ACE)
        );

        for (Card card : aces) {
            assertEquals(1, card.getPointValue());
        }
    }

    @Test
    public void numericCardsWorthNumericPoints() {
        assertEquals(2, new Card(Suit.DIAMONDS, Rank.TWO).getPointValue());
        assertEquals(3, new Card(Suit.HEARTS, Rank.THREE).getPointValue());
        assertEquals(4, new Card(Suit.SPADES, Rank.FOUR).getPointValue());
        assertEquals(5, new Card(Suit.CLUBS, Rank.FIVE).getPointValue());
        assertEquals(6, new Card(Suit.DIAMONDS, Rank.SIX).getPointValue());
        assertEquals(7, new Card(Suit.HEARTS, Rank.SEVEN).getPointValue());
        assertEquals(9, new Card(Suit.SPADES, Rank.NINE).getPointValue());
        assertEquals(10, new Card(Suit.CLUBS, Rank.TEN).getPointValue());
    }

    @Test
    public void deckHasFiftyTwoCards() {
        assertEquals(52, deck.size());
    }

    @Test
    public void deckHasThirteenOfEachSuit() {
        int numDiamonds = 0;
        int numHearts = 0;
        int numSpades = 0;
        int numClubs = 0;

        for (Card card : deck) {
            switch (card.getSuit()) {
                case DIAMONDS:
                    numDiamonds++;
                    break;
                case HEARTS:
                    numHearts++;
                    break;
                case SPADES:
                    numSpades++;
                    break;
                case CLUBS:
                    numClubs++;
                    break;
                default:
                    break;
            }
        }

        assertEquals(13, numDiamonds);
        assertEquals(13, numHearts);
        assertEquals(13, numSpades);
        assertEquals(13, numClubs);
    }
}
