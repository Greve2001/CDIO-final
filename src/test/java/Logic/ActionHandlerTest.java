package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    void breweryActionBuyingDeed() {
        actionHandler.squareAction(players[0], squares[12], 10);

        int expectedBalance = 27000;
        int actualBalance = players[0].getBalance();
        assertEquals(expectedBalance, actualBalance);

        Player expectedOwner = players[0];
        Player actualOwner = squares[12].getOwner();
        assertEquals(expectedOwner, actualOwner);
    }

    @Test
    void breweryActionPayRent() {
        squares[12].setOwner(players[0]);
        actionHandler.squareAction(players[1], squares[12], 10);

        // Player landing on the street.
        int expectedBalance = 29000;
        int actualBalance = players[1].getBalance();
        assertEquals(expectedBalance, actualBalance);

        // Owner of the street.
        expectedBalance = 31000;
        actualBalance = players[0].getBalance();
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void breweryMonopolyPayRent() {
        squares[12].setOwner(players[0]);
        squares[28].setOwner(players[0]);
        actionHandler.squareAction(players[1], squares[12], 10);

        // Player landing on the street.
        int expectedBalance = 28000;
        int actualBalance = players[1].getBalance();
        assertEquals(expectedBalance, actualBalance);

        // Owner of the street.
        expectedBalance = 32000;
        actualBalance = players[0].getBalance();
        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void ferryActionBuyingDeed() {
        actionHandler.squareAction(players[0], squares[5], 10);

        int expectedBalance = 26000;
        int actualBalance = players[0].getBalance();
        assertEquals(expectedBalance, actualBalance);

        Player expectedOwner = players[0];
        Player actualOwner = squares[5].getOwner();
        assertEquals(expectedOwner, actualOwner);
    }

    @Test
    void ferryActionPayRent() {
        squares[5].setOwner(players[0]);
        actionHandler.squareAction(players[1], squares[5], 10);

        // Player landing on the street.
        int expectedBalance = 29500;
        int actualBalance = players[1].getBalance();
        assertEquals(expectedBalance, actualBalance);

        // Owner of the street.
        expectedBalance = 30500;
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
    void roundToNearest50RoundUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method roundToNearest50 = actionHandler.getClass().getDeclaredMethod("roundToNearest50", int.class);
        roundToNearest50.setAccessible(true);
        int actualCase1 = (int) roundToNearest50.invoke(actionHandler, 25);
        int actualCase2 = (int) roundToNearest50.invoke(actionHandler, 26);
        int actualCase3 = (int) roundToNearest50.invoke(actionHandler, 49);

        int expected = 50;

        assertEquals(expected, actualCase1);
        assertEquals(expected, actualCase2);
        assertEquals(expected, actualCase3);

        int actualCase4 = (int) roundToNearest50.invoke(actionHandler, 75);
        int actualCase5 = (int) roundToNearest50.invoke(actionHandler, 76);
        int actualCase6 = (int) roundToNearest50.invoke(actionHandler, 99);

        expected = 100;

        assertEquals(expected, actualCase4);
        assertEquals(expected, actualCase5);
        assertEquals(expected, actualCase6);

    }

    @Test
    void roundToNearest50RoundDown() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method roundToNearest50 = actionHandler.getClass().getDeclaredMethod("roundToNearest50", int.class);
        roundToNearest50.setAccessible(true);
        int actualCase1 = (int) roundToNearest50.invoke(actionHandler, 24);
        int actualCase2 = (int) roundToNearest50.invoke(actionHandler, 23);
        int actualCase3 = (int) roundToNearest50.invoke(actionHandler, 1);

        int expected = 0;

        assertEquals(expected, actualCase1);
        assertEquals(expected, actualCase2);
        assertEquals(expected, actualCase3);

        int actualCase4 = (int) roundToNearest50.invoke(actionHandler, 74);
        int actualCase5 = (int) roundToNearest50.invoke(actionHandler, 73);
        int actualCase6 = (int) roundToNearest50.invoke(actionHandler, 51);

        expected = 50;

        assertEquals(expected, actualCase4);
        assertEquals(expected, actualCase5);
        assertEquals(expected, actualCase6);

    }

    @Test
    void holdAuction() {
    }

    @Test
    void roundToNearest50() {
    }
}