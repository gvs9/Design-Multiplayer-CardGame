import java.util.*;
/**
* represent the class
 */
class Player {
    private final String name;
    private final List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand.
      card== the card to add
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Removes a card from the player's hand.
     *  cardIndex ==the index of the card to remove
     * return the card that was removed
     */
    public Card removeCard(int cardIndex) {
        return hand.remove(cardIndex);
    }

    /**
     * Draws a card from the draw pile and adds it to the player's hand.
     * drawPile ==the draw pile
     */
    public void drawCard(List<Card> drawPile) {
        Card card = drawPile.remove(0);
        hand.add(card);
        System.out.println(name + " draws a " + card);
    }
}