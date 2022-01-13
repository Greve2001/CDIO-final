package cards;

import Utilities.Language;

public class Deck {

    final static int DECK_SIZE = 41;
    private ChanceCard[] chanceCardDeck;
    private int drawCardCount = 0;


    /**
     * Constructor for the Matador-game.
     * Initializes the Chance Cards used in the game.
     * Then calls the shuffleCards-method.
     */
    public Deck() {
        this.chanceCardDeck = new ChanceCard[DECK_SIZE];

        chanceCardDeck[0] = new PayMoneyToBankCard(Language.get("ccFuldtStop"), 1000);
        chanceCardDeck[1] = new PayMoneyToBankCard(Language.get("ccVognvaskOgSmøring"), 300);
        chanceCardDeck[2] = new PayMoneyToBankCard(Language.get("ccLeveringAfToKasserØl"), 200);
        chanceCardDeck[3] = new PayMoneyToBankCard(Language.get("ccReparationAfVogn"), 3000);
        chanceCardDeck[4] = new PayMoneyToBankCard(Language.get("ccReparationAfVogn"), 3000);
        chanceCardDeck[5] = new PayMoneyToBankCard(Language.get("ccNyeDæk"), 1000);
        chanceCardDeck[6] = new PayMoneyToBankCard(Language.get("ccParkeringsbøde"), 200);
        chanceCardDeck[7] = new PayMoneyToBankCard(Language.get("ccBilforsikring"), 1000);
        chanceCardDeck[8] = new PayMoneyToBankCard(Language.get("ccTold"), 200);
        chanceCardDeck[9] = new PayMoneyToBankCard(Language.get("ccTandlægeregning"), 2000);

        chanceCardDeck[10] = new ReceiveMoneyFromBankCard(Language.get("ccVundetIKlasselotteriet"), 500);
        chanceCardDeck[11] = new ReceiveMoneyFromBankCard(Language.get("ccVundetIKlasselotteriet"), 500);
        chanceCardDeck[12] = new ReceiveMoneyFromBankCard(Language.get("ccModtagAktieudbytte"), 1000);
        chanceCardDeck[13] = new ReceiveMoneyFromBankCard(Language.get("ccModtagAktieudbytte"), 1000);
        chanceCardDeck[14] = new ReceiveMoneyFromBankCard(Language.get("ccModtagAktieudbytte"), 1000);
        chanceCardDeck[15] = new ReceiveMoneyFromBankCard(Language.get("ccEftergivetSkat"), 3000 );
        chanceCardDeck[16] = new ReceiveMoneyFromBankCard(Language.get("cc11RigtigeITipning"), 1000);
        chanceCardDeck[17] = new ReceiveMoneyFromBankCard(Language.get("ccGageforhøjelse"), 1000);
        chanceCardDeck[18] = new ReceiveMoneyFromBankCard(Language.get("ccPræmieobligationUdtrukket"), 1000);
        chanceCardDeck[19] = new ReceiveMoneyFromBankCard(Language.get("ccPræmieobligationUdtrukket"), 1000);
        chanceCardDeck[20] = new ReceiveMoneyFromBankCard(Language.get("ccSalgPåAuktion"), 1000);
        chanceCardDeck[21] = new ReceiveMoneyFromBankCard(Language.get("ccVærdiAfAvlFraNyttehaven"), 200);

        chanceCardDeck[22] = new MoveNrOfFieldsCard(Language.get("ccRykTreFelterFrem"), 3);
        chanceCardDeck[23] = new MoveNrOfFieldsCard(Language.get("ccRykTreFelterTilbage"), -3);
        chanceCardDeck[24] = new MoveNrOfFieldsCard(Language.get("ccRykTreFelterTilbage"), -3);

        chanceCardDeck[25] = new ReceiveMoneyFromPlayersCard(Language.get("ccFødselsdag"), 200);
        chanceCardDeck[26] = new ReceiveMoneyFromPlayersCard(Language.get("ccSammenskudsgilde"), 500);
        chanceCardDeck[27] = new ReceiveMoneyFromPlayersCard(Language.get("ccFamiliefest"), 500);

        chanceCardDeck[28] = new MoveToSpecificFieldCard(Language.get("ccRykFremTilStart"), 0);
        chanceCardDeck[29] = new MoveToSpecificFieldCard(Language.get("ccRykFremTilStart"), 0);
        chanceCardDeck[30] = new MoveToSpecificFieldCard(Language.get("ccRykFremTilFrederiksbergAllé"), 11);
        chanceCardDeck[31] = new MoveToSpecificFieldCard(Language.get("ccTagMedMolsLinjen"), 15);
        chanceCardDeck[32] = new MoveToSpecificFieldCard(Language.get("ccRykFremTilGrønningen"), 24);
        chanceCardDeck[33] = new MoveToSpecificFieldCard(Language.get("ccRykFremTilVimmelskaftet"), 32);
        chanceCardDeck[34] = new MoveToSpecificFieldCard(Language.get("ccRykFremTilStrandvejen"), 19);
        chanceCardDeck[35] = new MoveToSpecificFieldCard(Language.get("ccTagIndTilRådhuspladsen"), 39);
        chanceCardDeck[36] = new MoveToSpecificFieldCard(Language.get("ccGåIFængsel"), 10);
        chanceCardDeck[37] = new MoveToSpecificFieldCard(Language.get("ccGåIFængsel"), 10);

        chanceCardDeck[38] = new MatadorGrantCard(Language.get("ccMatadorLegat"), 40000);

        chanceCardDeck[39] = new GetOutOfJailCard(Language.get("ccBenådningforFængsel"));
        chanceCardDeck[40] = new GetOutOfJailCard(Language.get("ccBenådningforFængsel"));

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
