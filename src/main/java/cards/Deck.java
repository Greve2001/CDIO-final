package cards;

public class Deck {

    private ChanceCard[] chanceCardDeck;
    private int drawCardCount = 0;


    /**
     * Constructor for the Matador-game.
     * Initializes the Chance Cards used in the game.
     */

    // Three types of chanceCard so far. Rest OTW.

    public Deck() {
        this.chanceCardDeck = new ChanceCard[25];

        chanceCardDeck[0] = new PayMoneyCard("De har kørt frem for \"fuldt stop.\" Betal 1000 kr. i bøde.", 1000);
        chanceCardDeck[1] = new PayMoneyCard("Betal for vognvask og smøring, kr. 300.", 300);
        chanceCardDeck[2] = new PayMoneyCard("Betal kr. 200 for levering af to kasser øl.", 200);
        chanceCardDeck[3] = new PayMoneyCard("Betal 3000 for reperation af deres vogn.", 3000);
        chanceCardDeck[4] = new PayMoneyCard("Betal 3000 for reperation af deres vogn.", 3000);
        chanceCardDeck[5] = new PayMoneyCard("De har købt 4 nye dæk til deres vogn. Betal kr. 1000.", 1000);
        chanceCardDeck[6] = new PayMoneyCard("De har fået en parkeringsbøde. Betal kr. 200 i bøde.", 200);
        chanceCardDeck[7] = new PayMoneyCard("Betal deres bilforsikring, kr. 1000.", 1000);
        chanceCardDeck[8] = new PayMoneyCard("De har været udenlands og har købt for mange smøger med hjem. Betal kr. 200 i told.", 200);
        chanceCardDeck[9] = new PayMoneyCard("Tandlægeregning, betal kr. 2000.", 2000);

        chanceCardDeck[10] = new ReceiveMoneyCard("De har vundet i klasselotteriet. Modtag 500 kr.", 500);
        chanceCardDeck[11] = new ReceiveMoneyCard("De har vundet i klasselotteriet. Modtag 500 kr.", 500);
        chanceCardDeck[12] = new ReceiveMoneyCard("De modtager deres aktieudbytte. Modtag kr. 1000 af banken", 1000);
        chanceCardDeck[13] = new ReceiveMoneyCard("De modtager deres aktieudbytte. Modtag kr. 1000 af banken", 1000);
        chanceCardDeck[14] = new ReceiveMoneyCard("De modtager deres aktieudbytte. Modtag kr. 1000 af banken", 1000);
        chanceCardDeck[15] = new ReceiveMoneyCard("Kommunen har eftergivet et kvartals skat. Hæv i banken kr. 3000.", 3000 );
        chanceCardDeck[16] = new ReceiveMoneyCard("De havde en række med elleve rigtige i tipning. Modtag kr. 1000.", 1000);
        chanceCardDeck[17] = new ReceiveMoneyCard("Grundet dyrtiden har De fået gageforhøjelse.", 1000);
        chanceCardDeck[18] = new ReceiveMoneyCard("Deres præmieobligation er udtrukket. De modtager kr. 1000 af banken.", 1000);
        chanceCardDeck[19] = new ReceiveMoneyCard("Deres præmieobligation er udtrukket. De modtager kr. 1000 af banken.", 1000);
        chanceCardDeck[20] = new ReceiveMoneyCard("De har solgt nogle gamle møbler på auktion. Modtag kr. 1000 af banken.", 1000);
        chanceCardDeck[21] = new ReceiveMoneyCard("Værdien af egen avl fra nyttehaven udgør kr. 200 som de modtager af banken.", 200);

        chanceCardDeck[22] = new MoveFieldsCard("Ryk tre felter frem.", 3);
        chanceCardDeck[23] = new MoveFieldsCard("Ryk tre felter tilbage", -3);
        chanceCardDeck[24] = new MoveFieldsCard("Ryk tre felter tilbage", -3);

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
