import java.util.*;

 //Represents the game.

class Game {

    private  final int NUM_PLAYERS = 4;
    private  final int NUM_CARDS_PER_HAND = 5;
   // private static final int DRAW_PILE_SIZE = 52 - NUM_PLAYERS * NUM_CARDS_PER_HAND;
    private static final String[] SUITS = {"hearts", "diamonds", "clubs", "spades"};
    private static final String[] RANKS = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    //private static final String[] ACTION_CARDS = {"A", "K", "Q", "J"};

    private final List<Card> drawPile;
    private final List<Card> discardPile;
    private final List<Player> players;
    private int currentPlayerIndex;
    private boolean reverseOrder;
    private boolean skipNextTurn;

    public Game() {
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        players = new ArrayList<>();
        currentPlayerIndex = 0;
        reverseOrder = false;
        skipNextTurn = false;
        Deck();
        Players();
    }

    /**
     *   deck of cards.
     */
    private void Deck() {
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                drawPile.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(drawPile);
    }

    /**
     *  players of the game
     */
    private void Players() {
        Scanner scanner = new Scanner(System.in);
        for (int i = 1; i <= NUM_PLAYERS; i++) {
            System.out.print("Enter name for Player " + i + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }
    }

    /**
     *  Cards Allocation to the players
     */
    private void allotCards() {
        for (Player player : players) {
            for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
                Card card = drawPile.remove(0);
                player.addCard(card);
            }
            System.out.println(player.getName() + " receives " + NUM_CARDS_PER_HAND + " cards.");
        }
        discardPile.add(drawPile.remove(0));
        System.out.println("The top card on the discard pile is " + discardPile.get(0));
    }

    /**
     * it returns the next player in turn
     */
    private Player getNextPlayer() {
        if (skipNextTurn) {
            skipNextTurn = false;
            int skippedPlayerIndex = getNextPlayerIndex();
            System.out.println(players.get(skippedPlayerIndex).getName() + " is skipped.");
        }
        Player nextPlayer = players.get(currentPlayerIndex);
        currentPlayerIndex = getNextPlayerIndex();
        return nextPlayer;
    }

    /**
     * Returns the index of the next player in turn.
     *
     */
    private int getNextPlayerIndex() {
        int nextPlayerIndex = currentPlayerIndex + (reverseOrder ? -1 : 1);
        if (nextPlayerIndex < 0) {
            nextPlayerIndex = players.size() - 1;
        } else if (nextPlayerIndex >= players.size()) {
            nextPlayerIndex = 0;
        }
        return nextPlayerIndex;
    }

    /**
     * Plays a card from the player's hand.
     *  player ==the player who is playing the card
     *  cardIndex ==the index of the card to play
     *   it returns true if the card was played successfully,  or else false
     */
    private boolean playCard(Player player, int cardIndex) {
        Card card = player.getHand().get(cardIndex);
        if (canPlayCard(card)) {
            player.removeCard(cardIndex);
            discardPile.add(card);
            System.out.println(player.getName() + " plays " + card);
            applyActionCard(card);
            return true;
        } else {
            System.out.println("Invalid move.");
            return false;
        }
    }

    /**
     * shows whether a card can be played.
     *  card ==the card to check
     * it returns true if the card can be played, or else false
     */
    private boolean canPlayCard(Card card) {
        Card topCard = discardPile.get(discardPile.size() - 1);
        return card.getSuit().equals(topCard.getSuit()) || card.getRank().equals(topCard.getRank());
    }

    /**
     * Applies the action of an action card.
     *  card== the action card to apply
     */
    private void applyActionCard(Card card) {
        if (card.getRank().equals("A")) {
            skipNextTurn = true;
            System.out.println("Next player is skipped.");
        } else if (card.getRank().equals("K")) {
            reverseOrder = !reverseOrder;
            System.out.println("Order of play is reversed.");
        }
        else if (card.getRank().equals("Q")) {
            Player nextPlayer = getNextPlayer();
            drawCards(nextPlayer, 2);
            System.out.println(nextPlayer.getName() + " draws 2 cards.");
        } else if (card.getRank().equals("J")) {
            Player nextPlayer = getNextPlayer();
            drawCards(nextPlayer, 4);
            System.out.println(nextPlayer.getName() + " draws 4 cards.");
        }
    }

    /**
     * Draws a specified number of cards for a player.
      player ==the player to draw cards for
      numCards ==the number of cards to draw
     */
    private void drawCards(Player player, int numCards) {
        for (int i = 0; i < numCards; i++) {
            if (drawPile.isEmpty()) {
                endGame();
                return;
            }
            Card card = drawPile.remove(0);
            player.addCard(card);
            System.out.println(player.getName() + " draws " + card);
        }
    }

    /**
     * Ends the game and shows  the winner.
     */
    private void endGame() {
        int minCards = Integer.MAX_VALUE;
        Player winner = null;
        for (Player player : players) {
            int numCards = player.getHand().size();
            if (numCards < minCards) {
                minCards = numCards;
                winner = player;
            }
        }
        System.out.println("Game over! " + winner.getName() + " wins with " + minCards + " cards.");
        System.exit(0);
    }

    /**
     * Runs the game.
     */
    public void run() {
        Deck();
        Players();
        allotCards();
        System.out.println(players.get(currentPlayerIndex).getName() + " goes first.");
        while (true) {
            Scanner scanner = new Scanner(System.in);
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println(currentPlayer.getName() + "'s turn.");
            System.out.println("Your hand: " + currentPlayer.getHand());
            System.out.println("Top card on discard pile: " + discardPile.get(discardPile.size() - 1));
            System.out.print("Enter the index of the card you want to play (or -1 to draw a card): ");
            int cardIndex = scanner.nextInt();
            if (cardIndex == -1) {
                if (drawPile.isEmpty()) {
                    endGame();
                }
                Card drawnCard = drawPile.remove(0);
                currentPlayer.addCard(drawnCard);
                System.out.println(currentPlayer.getName() + " draws " + drawnCard);
            } else if (playCard(currentPlayer, cardIndex)) {
                if (currentPlayer.getHand().isEmpty()) {
                    endGame();
                }
            }
            System.out.println();
        }
    }
}



