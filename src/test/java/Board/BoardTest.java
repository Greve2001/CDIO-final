package Board;

import Interface.GUIController;
import Logic.Bank;
import Utilities.Language;
import gui_main.GUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Logic.Player;

import javax.swing.*;


import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    Player player;
    Square[] ALL_SQUARES;

    @BeforeEach
    void setUp() {
        Language.getInstance();
        board = new Board();
        new GUIController(board.getALL_SQUARES());
        player = new Player("test", 30000, 0);
        ALL_SQUARES = board.getALL_SQUARES();
        GUIController.setTesting(true);
    }

    @Test
    void updatePlayerPosition() {
        player.setPosition(35);
        board.updatePlayerPosition(player, 10);
        assertEquals(5, player.getPosition());

        player.setPosition(2);
        board.updatePlayerPosition(player, 6);
        assertEquals(8, player.getPosition());

        //with passing start
        int playerStartBalance = 10000;
        player.setBalance(playerStartBalance);
        player.setPosition(35);
        board.updatePlayerPosition(player, 12);
        assertEquals(7, player.getPosition());
        assertEquals(14000, player.getBalance());

        //checking if start bonus is payed if player move backward over start
        player.setBalance(playerStartBalance);
        player.setPosition(2);
        board.updatePlayerPosition(player, -6);
        assertEquals(36, player.getPosition());
        assertEquals(10000, player.getBalance());
    }

    @Test
    void payPlayerThatOwnStreet() {
        Player a = new Player("test", 30000, 0);
        Player b = new Player("test2", 30000, 0);

        int position = 1;

        ALL_SQUARES[position].setOwner(b);

        board.setPlayerPosition(a, position, false);

        assertEquals(30050, b.getBalance());
        assertEquals(29950, a.getBalance());
    }

    @Test
    void playerTotalValue() {
        Player a = new Player("test", 30000, 0);
        Player b = new Player("test2", 30000, 0);
        Player c = new Player("test2", 30000, 0);

        int position = 3, position2 = 6, position3 = 9;

        ALL_SQUARES[position].setOwner(a);
        ALL_SQUARES[position2].setPledge(true);
        ALL_SQUARES[position2].setOwner(b);
        ALL_SQUARES[position3].setOwner(c);

        assertEquals(31200, board.playerTotalValue(a));
        assertEquals(31000, board.playerTotalValue(b));
        assertEquals(32400, board.playerTotalValue(c));
    }

    @Test
    void playerLandsOnPrison() {
        Player a = new Player("test", 30000, 0);

        int position = 5;

        a.setPosition(position);

        board.updatePlayerPosition(a, 5);

        assertFalse(a.isInJail());
        assertEquals(10, a.getPosition());
    }

    @Test
    void playerLandsOnGoToPrison() {
        Player a = new Player("test", 30000, 0);

        int position = 25;
        a.setPosition(position);

        board.updatePlayerPosition(a, 5);

        assertTrue(a.isInJail());
        assertEquals(10, a.getPosition());
    }

    @Test
    void amountOwnedWithinTheColorTest() {
        Player a = new Player("test", 30000, 0);

        int position1 = 6, position2 = 8, position3 = 9;

        ALL_SQUARES[position1].setOwner(a);
        assertEquals(1, board.amountOwnedWithinTheColor(position1));

        ALL_SQUARES[position2].setOwner(a);
        assertEquals(2, board.amountOwnedWithinTheColor(position2));

        ALL_SQUARES[position3].setOwner(a);
        assertEquals(3, board.amountOwnedWithinTheColor(position3));
    }

    @Test
    void escapeJailTest() {
        Player a = new Player("test", 30000, 0);
        a.setInJail(true);

        boolean forcedToMove = false, haveToPay = true, usedChanceCard = false;

        board.escapeJail(a, 0, forcedToMove, haveToPay, usedChanceCard);

        assertFalse(a.isInJail());
        assertEquals(29000, a.getBalance());

        a.setInJail(true);
        forcedToMove = true;
        haveToPay = false;

        board.escapeJail(a, 0, forcedToMove, haveToPay, usedChanceCard);

        assertFalse(a.isInJail());
        assertEquals(29000, a.getBalance());

        a.setInJail(true);
        haveToPay = true;

        board.escapeJail(a, 0, forcedToMove, haveToPay, usedChanceCard);

        assertFalse(a.isInJail());
        assertEquals(28000, a.getBalance());

        //TODO test escape card use
    }

    @Test
    void testBuyHouse(){
        Player a = new Player("a",30000,0);

        int position1 = 16, position2 = 18, position3 = 19;
        String color = "grey";
        boolean happend;

        ALL_SQUARES[position1].setOwner(a);
        ALL_SQUARES[position2].setOwner(a);

        board.buyHouse(a,color,6);

        assertEquals(30000, a.getBalance());
        assertEquals(0, ALL_SQUARES[position1].getAmountOfHouses());
        assertEquals(0, ALL_SQUARES[position2].getAmountOfHouses());
        assertEquals(0, ALL_SQUARES[position3].getAmountOfHouses());

        ALL_SQUARES[position3].setOwner(a);
        a.setBalance(10000);

        board.buyHouse(a,color,6);

        assertEquals(10000, a.getBalance());
        assertEquals(0, ALL_SQUARES[position1].getAmountOfHouses());
        assertEquals(0, ALL_SQUARES[position2].getAmountOfHouses());
        assertEquals(0, ALL_SQUARES[position3].getAmountOfHouses());

        a.setBalance(30000);

        board.buyHouse(a,color,1);

        assertEquals(28000, a.getBalance());
        assertEquals(1, ALL_SQUARES[position1].getAmountOfHouses());
        //assertNotEquals(0, ALL_SQUARES[position2].getAmountOfHouses());
        //assertNotEquals(0, ALL_SQUARES[position3].getAmountOfHouses());
    }

    @Test
    void testGetCurrentCostFieldStreet(){
        int result;
        int position = 1;

        result = board.getCurrentCost(position);
        assertEquals(1200, result);

        ALL_SQUARES[position].setOwner(player);
        result = board.getCurrentCost(position);
        assertEquals(50,result);

        ALL_SQUARES[position].setAmountOfHouses(2);
        result = board.getCurrentCost(position);
        assertEquals(750, result);
    }

    @Test
    void testGetCurrentCostFieldFerry(){
        int result;
        Player a = new Player("a",30000,0);
        int position = 5;
        int position2 = 15;

        result = board.getCurrentCost(position);
        assertEquals(4000, result);

        ALL_SQUARES[position].setOwner(player);
        result = board.getCurrentCost(position);
        assertEquals(500,result);

        ALL_SQUARES[position].setAmountOfHouses(2);
        result = board.getCurrentCost(position);
        assertEquals(500, result);

        ALL_SQUARES[position2].setOwner(player);
        result = board.getCurrentCost(position);
        assertEquals(1000, result);

        ALL_SQUARES[position2].setOwner(a);
        result = board.getCurrentCost(position);
        assertEquals(500, result);
    }

    @Test
    void testGetAllStreetColors(){
        String[] arr = board.getAllStreetColors();

        assertEquals(8, arr.length);
    }
}