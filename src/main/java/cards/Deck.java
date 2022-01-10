package cards;

public class Deck {

    private ChanceCard[] chanceCardDeck;
    private int drawCardCount = 0;

    //Temporary constructor, that initiates an array of 6 ChanceCards.
    //The array holds 2 cards of each of the 3 types, that have been implemented so far.
    public Deck(){
        this.chanceCardDeck = new ChanceCard[6];

        chanceCardDeck[0] = new PayMoneyCard(1000);
        chanceCardDeck[1] = new PayMoneyCard(300);
        chanceCardDeck[2] = new ReceiveMoneyCard(500);
        chanceCardDeck[3] = new ReceiveMoneyCard(1000);
        chanceCardDeck[4] = new MoveFieldsCard(3);
        chanceCardDeck[5] = new MoveFieldsCard(-3);

    }

    /**
     * One-argument constructor, that initializes the deck with the input array.
     * Primarily used for unit-testing.
     *
     * @param chanceCards
     */
    public Deck(ChanceCard[] chanceCards){
        if (chanceCards == null){
            throw new IllegalArgumentException("You cannot initialize an empty array.");
        }else {
            this.chanceCardDeck = chanceCards;
        }

    }

    /**
     * @returns the ChanceCard that is currently at the top of the deck.
     */
    public ChanceCard pullCard() {
        if (drawCardCount >= chanceCardDeck.length){
            drawCardCount = 0;
        }
        return chanceCardDeck[drawCardCount++];
    }



}
