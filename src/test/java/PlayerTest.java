import SimpleClasses.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    String name;
    int balance;
    int position;
    Player p;

    @BeforeEach
    public void setup(){
        //Setup
        name = "TestPlayer";
        balance = 0;
        position = 0;
        p = new Player(name, balance, position);
    }

    @Test
    public void setAndGetBalanceTest(){
        // Action
        int playersBalanceBefore = p.getBalance();
        p.setBalance(20);
        int playersBalanceAfter = p.getBalance();

        int expectedBalanceBefore = 0;
        int expectedBalanceAfter = 20;

        // Assertions
        assertEquals(expectedBalanceBefore, playersBalanceBefore);
        assertEquals(expectedBalanceAfter, playersBalanceAfter);
    }

    @Test
    public void setAndGetPositionTest(){
        // Action
        int playersPositionBefore = p.getPosition();
        p.setPosition(3);
        int playersPositionAfter = p.getPosition();

        int expectedPositionBefore = 0;
        int expectedPositionAfter = 3;

        // Assertions
        assertEquals(expectedPositionBefore, playersPositionBefore);
        assertEquals(expectedPositionAfter, playersPositionAfter);
    }


    @Test
    public void getNameTest(){
        String playersName = p.getName();

        assertTrue(name.equals(playersName));
    }
}