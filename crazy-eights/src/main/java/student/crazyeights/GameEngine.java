package student.crazyeights;

import student.crazyeights.*;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class GameEngine {

    /** Current players of the game. */
    private List<PlayerStrategy> players;

    /** Represents the deck of cards being played. Game finishes when it is empty. */
    private List<Card> gameDeck;

    /** The latest card discarded. */
    private Card topPileCard;

    /** Used to add for the PlayerTurn list. */
    private PlayerTurn playerRecord;

    /** The number of cards a player is holding. The index correspond to their assigned playerId. */
    private List<Integer> numberOfPlayerCards;

    /** Used to keep track of each player's scores. */
    private List<Integer> playerScores;

    /** Mainly used for testing, works like System.out. */
    private static PrintStream logger;

    /** Represents the supposed cards of the players. */
    private HashMap<Integer, List<Card>> playerCards = new HashMap<>();

    /**
     * Initializes private variables, shuffles the deck and give initial cards.
     * @param playerStrategies represent the players of the game
     * @param outputStream is mainly used for testing
     */
    public void setUpGame(List<PlayerStrategy> playerStrategies, final OutputStream outputStream) {
        this.players = playerStrategies;
        logger = new PrintStream(outputStream);
        playerRecord = new PlayerTurn();
        numberOfPlayerCards = new ArrayList<>();
        playerScores = new ArrayList<>();
        int numberOfPlayers = players.size();
        // Initialize the players
        for (PlayerStrategy player: players) {
            int playerId = 0;
            List<Integer> opponentsIds = new ArrayList<>();
            for (int i = 0; i <= numberOfPlayers; i++) {
                if (i != playerId) {
                    opponentsIds.add(i);
                }
            }
            player.reset(); // assures it is new player and for reusability.
            player.init(playerId, opponentsIds); // or player.init(opponentsIds);
            playerId++;
        }
        // Shuffle the deck
        gameDeck = Card.getDeck();
        Collections.shuffle(gameDeck);
        // Players must deal five cards
        for (PlayerStrategy player : players) {
            int playerId = 0;
            int currentDeckSize = gameDeck.size();
            List<Card> toDeal = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                toDeal.add(gameDeck.get(0));
                gameDeck.remove(0);
            }
            player.receiveInitialCards(toDeal);
            numberOfPlayerCards.add(toDeal.size());
            playerCards.put(playerId, toDeal);
            playerId++;
        }
        // Discard the top card to the discard pile
        while (gameDeck.get(0).equals(Card.Rank.EIGHT)) {
            Collections.shuffle(gameDeck);
        }
        topPileCard = gameDeck.get(0);
        gameDeck.remove(0);
    }

    public List<Card> getGameDeck() {
        return gameDeck;
    }

    public List<Integer> getNumberOfPlayerCards() {
        return numberOfPlayerCards;
    }

    public List<PlayerStrategy> getPlayers() {
        return players;
    }

    /**
     * Plays a series of games until a player reaches a score of 200 or above
     */
    public void playTournament() {
        boolean foundWinner = false;
        while (!foundWinner) {
            setUpGame(players, logger);
            playGame();
            for (Integer score : playerScores) {
                if (score >= 200) {
                    foundWinner = true;
                }
            }
        }
        logger.println("Tournament ended.");
    }

    public void playGame() {
        while (!gameDeck.isEmpty() || !numberOfPlayerCards.contains(0)) {
            playRound();
        }
        logger.println("Game ended.");
        // update scores of each players
        if (gameDeck.isEmpty()) {
            for (int playerId = 0; playerId < players.size(); playerId++) {
                int score = 0;
                // code below derived from:
                // https://www.geeksforgeeks.org/iterate-map-java/
                for (Map.Entry<Integer, List<Card>> playerCard : playerCards.entrySet()) {
                    if (playerCard.getKey() == playerId) {
                        continue;
                    }
                    List<Card> otherCards = playerCard.getValue();
                    for (Card card : otherCards) {
                        score += card.getPointValue();
                    }
                }
                playerScores.set(playerId, score);
            }
        } else {
            // add the score only to the winner
            int playerId = 0;
            for (int i = 0; i < numberOfPlayerCards.size(); i++) {
                if (numberOfPlayerCards.get(i) == 0) {
                    playerId = i;
                }
            }
            int score = 0;
            for (Map.Entry<Integer, List<Card>> playerCard : playerCards.entrySet()) {
                if (playerCard.getKey() == playerId) {
                    continue;
                }
                List<Card> otherCards = playerCard.getValue();
                for (Card card : otherCards) {
                    score += card.getPointValue();
                }
            }
            playerScores.set(playerId, score);
        }
    }

    /*
    Gives each player a turn to play if possible.
     */
    public void playRound() {
        List<PlayerTurn> roundRecord = new ArrayList<>();
        for (int playerId = 0; playerId < players.size(); playerId++) {
            PlayerStrategy currentPlayer = players.get(playerId);
            currentPlayer.processOpponentActions(roundRecord);
            playerRecord.playerId = playerId;
            playerRecord.drewACard = currentPlayer.shouldDrawCard(topPileCard, topPileCard.getSuit());
            playerRecord.playedCard = currentPlayer.playCard();
            List<Card> currentCards;
            currentCards = playerCards.get(playerId);
            if (playerRecord.playedCard != null) {
                // check if player made an illegal move
                if (playerRecord.playedCard.equals(Card.Rank.EIGHT)) {
                    Card.Suit suit = currentPlayer.declareSuit();
                    Card eight = new Card(suit, Card.Rank.EIGHT);
                    topPileCard = eight;
                } else if (topPileCard.getSuit().equals(playerRecord.playedCard.getSuit())
                        || topPileCard.getPointValue() == playerRecord.playedCard.getPointValue()) {
                    topPileCard = playerRecord.playedCard;
                } else {
                    logger.println("Cheater in a game here:" + playerId);
                    System.exit(1);
                }
                // update the cards the player has
                int noCards = numberOfPlayerCards.get(playerId) - 1;
                numberOfPlayerCards.set(playerId, noCards);
                try {
                    currentCards.remove(playerRecord.playedCard);
                } catch (Exception e) {
                    logger.println(e);
                }
                playerCards.put(playerId, currentCards);
                // check if winner wins from removing a card
                if (noCards == 0) {
                    // currentPlayer wins.
                    break;
                }
            } else {
                playerRecord.drewACard = currentPlayer.shouldDrawCard(topPileCard, topPileCard.getSuit());
                if (playerRecord.drewACard) {
                    Card toDraw = gameDeck.get(0);
                    gameDeck.remove(0);
                    // update the number of cards the player has
                    int noCards = numberOfPlayerCards.get(playerId) + 1;
                    currentCards.add(toDraw);
                    playerCards.put(playerId, currentCards);
                    numberOfPlayerCards.set(playerId, noCards);
                    currentPlayer.receiveCard(toDraw);
                    // check if draw resulted in empty deck
                    if (gameDeck.isEmpty()) {
                        break;
                    }
                } else {
                    // player did not make a turn
                    logger.println("Cheater in a game here! " + playerId);
                    System.exit(1);
                }
            }
            roundRecord.add(playerRecord);
        }
    }
}