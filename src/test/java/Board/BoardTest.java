package Board;

import Interface.GUIController;
import Utilities.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Logic.Player;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    Player player;
    Square[] ALL_SQUARES;

    @BeforeEach
    void setUp() {
        Language.getInstance();
        board = new Board();
        player = new Player("test", 30000, 0);
        ALL_SQUARES = board.getALL_SQUARES();
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
        board.updatePlayerPosition(player,12);
        assertEquals(7,player.getPosition());
        assertEquals(14000, player.getBalance());

        //checking if start bonus is payed if player move backward over start
        player.setBalance(playerStartBalance);
        player.setPosition(2);
        board.updatePlayerPosition(player,-6);
        assertEquals(36,player.getPosition());
        assertEquals(10000, player.getBalance());
    }

    @Test
    void payPlayerThatOwnStreet(){
        Player a = new Player("test", 30000, 0);
        Player b = new Player("test2", 30000, 0);

        int position = 1;

        ALL_SQUARES[position].setOwner(b);

        board.setPlayerPosition(a,position,false);

        assertEquals(30050, b.getBalance());
        assertEquals(29950, a.getBalance());
    }
    @Test
    void playerTotalValue()
    {
       Player a = new Player("test",30000,0);

       int position = 3;

       ALL_SQUARES[position].setOwner(a);

       assertEquals(30600,board.playerTotalValue(a));
    }
}