package Utilities;

import cards.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeckTest {

    @Test
    public void pullCardShouldReturnAChanceCardFromTheDeck(){
        ChanceCard testCard = new MoveFieldsCard("", 0);
        Deck testDeck = new Deck(new ChanceCard[]{testCard});

        ChanceCard result = testDeck.pullCard();

        assertThat(result).isInstanceOf(ChanceCard.class);

    }

    @Test
    public void pullCardShouldReturnAChanceCardFromTheTopOfTheDeck(){
        ChanceCard testCard0 = new MoveFieldsCard("",0);
        ChanceCard testCard1 = new PayMoneyCard("",0);
        ChanceCard testCard2 = new ReceiveMoneyCard("",0);

        Deck testDeck = new Deck(new ChanceCard[]{testCard0, testCard1, testCard2});

        ChanceCard result = testDeck.pullCard();

        assertThat(result).isEqualTo(testCard0);

    }

    @Test
    public void pullCardShouldReturnChanceCardWithIndexCorrespondingToDrawCardCount(){
        ChanceCard testCard1 = new ReceiveMoneyCard("", 0);
        ChanceCard testCard2 = new ReceiveMoneyCard("", 0);
        ChanceCard testCard3 = new ReceiveMoneyCard("", 0);


        Deck testDeck = new Deck(new ChanceCard[]{testCard1, testCard2, testCard3});

        ChanceCard result1 = testDeck.pullCard();
        ChanceCard result2 = testDeck.pullCard();
        ChanceCard result3 = testDeck.pullCard();

        assertThat(result1).isEqualTo(testCard1);
        assertThat(result2).isEqualTo(testCard2);
        assertThat(result3).isEqualTo(testCard3);
    }

    @Test
    public void pullCardShouldReturnChanceCardFromTheTopOfTheDeckWhenTheLastCardInTheDeckHasBeenDrawn(){
        ChanceCard testCard1 = new MoveFieldsCard("", 0);
        ChanceCard testCard2 = new MoveFieldsCard("", 0);
        ChanceCard testCard3 = new MoveFieldsCard("", 0);

        Deck testDeck = new Deck(new ChanceCard[]{testCard1, testCard2, testCard3});

        ChanceCard result1 = testDeck.pullCard();
        ChanceCard result2 = testDeck.pullCard();
        ChanceCard result3 = testDeck.pullCard();
        ChanceCard result4 = testDeck.pullCard();

        assertThat(result4).isEqualTo(testCard1);
    }

    @Test

    public void shuffleCardsShouldReturnChanceCardsInDifferentOrderThanBeforeShuffle(){
        ChanceCard testCard1 = new MoveFieldsCard("", 1);
        ChanceCard testCard2 = new MoveFieldsCard("", 2);
        ChanceCard testCard3 = new PayMoneyCard("", 3);
        ChanceCard testCard4 = new PayMoneyCard("", 4);
        ChanceCard testCard5 = new ReceiveMoneyCard("", 5);
        ChanceCard testCard6 = new ReceiveMoneyCard("", 6);

        ChanceCard[] deckBeforeShuffle = new ChanceCard[]{testCard1, testCard2, testCard3, testCard4, testCard5, testCard6};

        Deck testDeck = new Deck(deckBeforeShuffle);

        testDeck.shuffleCards();

        ChanceCard result1 = testDeck.pullCard();
        ChanceCard result2 = testDeck.pullCard();
        ChanceCard result3 = testDeck.pullCard();
        ChanceCard result4 = testDeck.pullCard();
        ChanceCard result5 = testDeck.pullCard();
        ChanceCard result6 = testDeck.pullCard();

        ChanceCard[] deckAfterShuffle = new ChanceCard[]{result1, result2, result3, result4, result5, result6};

        assertThat(deckAfterShuffle).isNotNull();
        assertThat(deckAfterShuffle).containsExactlyInAnyOrder(deckBeforeShuffle);
        assertThat(deckAfterShuffle).isNotEqualTo(deckBeforeShuffle);

    }




}
