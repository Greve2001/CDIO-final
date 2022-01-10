package Logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {
    Die die=new Die(6);

    @RepeatedTest(100)
    void when_roll_then_valueMustBeLessThanOrEqualToSix() {
        // Act
        die.roll();

        // Assert
        assertTrue(die.getFaceValue() <= 6);
    }

    @RepeatedTest(100)
    void when_roll_then_valueMustNotBeZero() {
        // Act
        die.roll();

        // Assert
        assertTrue(die.getFaceValue() !=0);

    }
}