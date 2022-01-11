package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionHandlerTest {
    Board board = new Board();
    ActionHandler actionHandler = new ActionHandler(board);
    Square[] squares = new Square[10];
    Player[] players = new Player[3];


    @BeforeEach
    void setUp() {
        System.setProperty("user.language", "da");
        Language.getInstance();
        new GUIController(board);
        GUIController.setTesting(true);

        int[] rents = {1000, 2000, 3000, 4000, 5000, 6000};

        squares[0] = new Start("Start", 1, 4000);
        squares[1] = new Street("Street", 2, "Street", "Yellow", rents, 2000, 2000);
        squares[2] = new Chance("Chance", 3);
        squares[3] = new GoToPrison("GoToPrison", 4);
        squares[4] = new Ferry("Ferry", 5, "Street", "Yellow", rents, 2000);
        squares[5] = new Brewery("Brewery", 6, "Street", "Yellow", rents, 2000);
        squares[6] = new Tax("Tax", 7, 2000);
        squares[7] = new IncomeTax("IncomeTax", 8, 4000, 10);
        squares[8] = new Refugee("Refuge", 9);
        squares[9] = new Prison("Prison", 10);

        players[0] = new Player("Player 1", 30000, 1);
        players[1] = new Player("Player 2", 30000, 1);
        players[2] = new Player("Player 3", 30000, 1);
    }

    @Test
    void streetActionBuyingDeed() {
        actionHandler.squareAction(players[0], squares[1], 10);

        int expectedBalance = 28000;
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

        int expectedBalance = 29000;
        int actualBalance = players[1].getBalance();
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void holdAuction() {
    }

    @Test
    void roundToNearest50() {
    }
}