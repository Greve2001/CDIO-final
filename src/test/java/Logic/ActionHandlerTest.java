package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionHandlerTest {
    Board board;
    ActionHandler actionHandler;
    Player[] players = new Player[3];
    Square[] squares;


    @BeforeEach
    void setUp() {
        System.setProperty("user.language", "da");
        Language.getInstance();
        board = new Board();
        squares = board.getALL_SQUARES();
        actionHandler = new ActionHandler(board);
        new GUIController(board.getALL_SQUARES());
        GUIController.setTesting(true);

        players[0] = new Player("Player 1", 30000, 1);
        players[1] = new Player("Player 2", 30000, 1);
        players[2] = new Player("Player 3", 30000, 1);
    }

    @Test
    void streetActionBuyingDeed() {
        actionHandler.squareAction(players[0], squares[1], 10);

        int expectedBalance = 28800;
        int actualBalance = players[0].getBalance();
        assertEquals(expectedBalance, actualBalance);

        Player expectedOwner = players[0];
        Player actualOwner = squares[1].getOwner();
        assertEquals(expectedOwner, actualOwner);
    }

    @Test
    void streetActionPayRent() {
        squares[1].setOwner(players[0]);
        actionHandler.squareAction(players[1], squares[1], 10);

        // Player landing on the street.
        int expectedBalance = 29950;
        int actualBalance = players[1].getBalance();
        assertEquals(expectedBalance, actualBalance);

        // Owner of the street.
        expectedBalance = 30050;
        actualBalance = players[0].getBalance();
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void payTax() {
        actionHandler.squareAction(players[0], squares[38], 10);

        int expectedBalance = 28000;
        int actualBalance = players[0].getBalance();
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void holdAuction() {
    }

    @Test
    void roundToNearest50() {
    }
}