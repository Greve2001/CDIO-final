package Logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {
    Die die=new Die(6);

    @Test
    void roll() {
        die.roll();
        assertTrue(die.getFaceValue() <= 6);
    }

    @Test
    void getFaceValue() {
        die.roll();
        assertTrue(die.getFaceValue() !=0);

    }
}