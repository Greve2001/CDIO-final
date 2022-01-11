package Logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceCupTest {
    DiceCup diceCup=new DiceCup();

    @Test
    void cupMustHaveTwoDice() {
        // Assert
        Assertions.assertEquals(2, diceCup.getFaceValues().length);
    }

    @Test
    void when_rollDice_andThen_getFaceValues_sumMustBeGreaterThanOrEqualToTwo() {
        // Act
        diceCup.rollDice();
        int sum=diceCup.getFaceValues()[0]+diceCup.getFaceValues()[1];

        // Assert
        assertTrue(sum >= 2);
    }
}