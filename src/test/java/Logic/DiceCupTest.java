package Logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceCupTest {
    DiceCup diceCup=new DiceCup();

    @Test
    void rollDice() {
       diceCup.rollDice();
        Assertions.assertEquals(true,diceCup.getFaceValues().length==2);
    }

    @Test
    void getFaceValues() {
        diceCup.rollDice();
        int sum=diceCup.getFaceValues()[0]+diceCup.getFaceValues()[1];
        Assertions.assertEquals(true,sum>=2);
    }
}