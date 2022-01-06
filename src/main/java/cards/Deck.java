package cards;

public class Deck {

    private ChanceCard[] chanceCardDeck;
    private int drawCardCount = 0;

    //Temporary constructor, that initiates an array of 6 ChanceCards.
    //The array holds 2 cards of each of the 3 types, that have been implemented so far.
    public Deck(){
        this.chanceCardDeck = new ChanceCard[6];

        chanceCardDeck[0] = new PayMoneyChanceCard(1000);
        chanceCardDeck[1] = new PayMoneyChanceCard(300);
        chanceCardDeck[2] = new ReceiveMoneyChanceCard(500);
        chanceCardDeck[3] = new ReceiveMoneyChanceCard(1000);
        chanceCardDeck[4] = new MoveFieldsChanceCard(3);
        chanceCardDeck[5] = new MoveFieldsChanceCard(-3);

    }

    public ChanceCard pullCard() {
        try {
            return chanceCardDeck[drawCardCount++];

        } catch (ArrayIndexOutOfBoundsException ex){
            // If an ArrayOutOfBoundsException is thrown, the deck is empty and the drawCardCount is reset
            // to 0 making sure that the next card will be drawn from the top of the deck.
            drawCardCount = 0;
            return chanceCardDeck[drawCardCount++];
        }

    }



}
