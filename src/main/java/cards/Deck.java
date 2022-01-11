package cards;

public class Deck {

    private ChanceCard[] chanceCardDeck;
    private int drawCardCount = 0;

    //Temporary constructor, that initiates an array of 6 ChanceCards.
    //The array holds 2 cards of each of the 3 types, that have been implemented so far.
    public Deck() {
        this.chanceCardDeck = new ChanceCard[6];

        chanceCardDeck[0] = new PayMoneyCard(1000);
        chanceCardDeck[1] = new PayMoneyCard(300);
        chanceCardDeck[2] = new ReceiveMoneyCard(500);
        chanceCardDeck[3] = new ReceiveMoneyCard(1000);
        chanceCardDeck[4] = new MoveFieldsCard(3);
        chanceCardDeck[5] = new MoveFieldsCard(-3);

        shuffleCards();
    }

    /**
     * One-argument constructor, that initializes the deck with the input array.
     * Primarily used for unit-testing.
     *
     * @param chanceCards
     */
    public Deck(ChanceCard[] chanceCards) {
        if (chanceCards == null) {
            throw new IllegalArgumentException("You cannot initialize an empty array.");
        } else {
            // Initializes a copy of the original array, to be used in the Unittest.
            ChanceCard[] copy = new ChanceCard[chanceCards.length];
            for(int i=0; i< chanceCards.length; i++){

                copy[i] = chanceCards[i];
            }
            this.chanceCardDeck = copy;

        }

    }

    /**
     * @returns the ChanceCard that is currently at the top of the deck.
     */
    public ChanceCard pullCard() {
        if (drawCardCount >= chanceCardDeck.length) {
            drawCardCount = 0;
        }
        return chanceCardDeck[drawCardCount++];
    }

    // Implementation of the modern Fisher-Yates shuffle.
    // Algorithm Source: https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
    public void shuffleCards() {
        for (int pos = chanceCardDeck.length - 1; pos >= 0; pos--) {
            // Generate a random number at the length of the array and shorten by one each iteration
            int randomNumber = (int) (Math.random() * (pos + 1));

            // Swap cards at the two locations
            ChanceCard cardToSwap = chanceCardDeck[pos];
            chanceCardDeck[pos] = chanceCardDeck[randomNumber];
            chanceCardDeck[randomNumber] = cardToSwap;
        }
    }

}
