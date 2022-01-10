package Board;

import Interface.GUIController;
import Utilities.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Logic.Player;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    Player player;

    @BeforeEach
    void setUp() {
        Language.getInstance();
        board = new Board();
        player = new Player("test", 30000, 0);
        GUIController.setTesting(true);
    }

    @Test
    void TestConstructor(){
        for (int i = 0; i<40; i++) {
            assertFalse(board.hasMonopoly(i));
        }
    }

    @Test
    void updatePlayerPosition(){
        player.setPosition(35);
        board.updatePlayerPosition(player,10);
        assertEquals(5,player.getPosition());

        player.setPosition(2);
        board.updatePlayerPosition(player,6);
        assertEquals(8,player.getPosition());

        //with passing start
        int playerStartBalance = 10000;
        player.setBalance(playerStartBalance);
        player.setPosition(35);
        board.updatePlayerPosition(player,10);
        assertEquals(5,player.getPosition());
        assertEquals(14000, player.getBalance());

        //checking if start bonus is payed if player move backward over start
        player.setPosition(2);
        board.updatePlayerPosition(player,-3);
        assertEquals(5,player.getPosition());
    }
}