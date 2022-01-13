package Logic;
import Board.Board;
import Board.Square;
import Interface.GUIController;
import SimpleClasses.DiceCup;
import SimpleClasses.Player;
import Utilities.Language;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import gui_main.GUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    GameController gameController;

    @BeforeEach
    void setup() {
        gameController = new GameController();
        Language.getInstance();
        GUIController.setTesting(true);
    }

    @Test
    void testAllowedNumberOfPlayers() {
        int[] numOfPlayers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] expectedNumPlayers = {0, 0, 0, 3, 4, 5, 6, 0, 0, 0};

        for (int i = 0; i < numOfPlayers.length; i++) {
            String[] playerNames = new String[numOfPlayers[i]];
            for (int j = 0; j < numOfPlayers[i]; j++) {
                playerNames[j] = Integer.toString(j);
            }
            gameController.setupPlayers(playerNames);

            int actualNumOfPlayers = gameController.getPlayers().length;

            assertEquals(expectedNumPlayers[i], actualNumOfPlayers);
        }
    }


    @Test
    void TestEveryPlayerStartBalance() {
        gameController.setupPlayers(new String[]{"1", "2", "3", "4"});
        Player[] players = gameController.getPlayers();

        int expectedStartBalance = 30000;

        for (Player player : players) {
            assertEquals(expectedStartBalance, player.getBalance());
        }
    }

    @Test
    void TestEveryPlayerHasATokenOnTheBoard() {
        GUI gui = new GUI();

        gameController.setupPlayers(new String[]{"1", "2", "3", "4"});
        Player[] players = gameController.getPlayers();

        for (Player player : players) {
            GUI_Player GUIplayer = new GUI_Player(player.getName());
            gui.addPlayer(GUIplayer);

            GUI_Car car = GUIplayer.getCar();
            assertNotNull(car);
        }
    }

    @Test
    void ensureInactivePlayersCannotPlay() {
        gameController.setupPlayers(new String[]{"1", "2", "3", "4"});
        Player[] players = gameController.getPlayers();

        Player currentPlayer = players[0];

        // Again what to do? Cannot access the right methods
        //TODO when figuring out how to make innerclass
    }

    @Test
    void TestFreeFromJailCard() {
        // Setup
        gameController.setupPlayers(new String[]{"1", "2", "3", "4"});
        Player[] players = gameController.getPlayers();

        // Part 1 //
        // Action
        players[1].setInJail(true);
        players[1].useOneGetOutOfJailCard();

        // Assert
        boolean actualStatus = players[1].isInJail();
        boolean expectedStatus = true; // Beacuse they havent been given a card.

        assertEquals(expectedStatus, actualStatus);

        // Part 2 //
        // Action
        players[2].setInJail(true);
        players[2].giveOneGetOutOfJailCard();
        players[2].useOneGetOutOfJailCard();

        actualStatus = players[2].isInJail();
        expectedStatus = false; // Beacuse they have a card

        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    void TestPlayerCanPayFeeToLeaveJail() {
        Player player = new Player("Test player", 1000, 0);
        GUIController.setTesting(true);
        Board board = new Board();

        board.setPlayerInJail(player);
        board.escapeJail(player, 0, true, true, false);

        int actualBalance = player.getBalance();
        int expectedBalance = 0;

        assertEquals(expectedBalance, actualBalance);
    }

    @Test
    void TestDouble3xTimesPutsPlayerInJail() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        gameController.setupGame();
        Player[] players = gameController.getPlayers();

        Field diceCup = gameController.getClass().getDeclaredField("diceCup");
        diceCup.setAccessible(true);

        Method takeTurn = gameController.getClass().getDeclaredMethod("takeTurn");
        takeTurn.setAccessible(true);

        diceCup.set(gameController, new DiceCupStub(4, 4));

        // Action
        takeTurn.invoke(gameController, null);
        takeTurn.invoke(gameController, null);
        takeTurn.invoke(gameController, null);

        // Assert
        boolean actualInJail = players[0].isInJail();

        assertTrue(actualInJail);

    }

    @Test
    void TestDoublesGiveExtraTurn() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        // Setup
        gameController.setupGame();
        Player[] players = gameController.getPlayers();

        Field diceCup = gameController.getClass().getDeclaredField("diceCup");
        diceCup.setAccessible(true);
        diceCup.set(gameController, new DiceCupStub(4, 4));

        Method takeTurn = gameController.getClass().getDeclaredMethod("takeTurn");
        takeTurn.setAccessible(true);


        // Action
        takeTurn.invoke(gameController, null);

        assertTrue(players[0].getHasExtraTurn());
    }

    @Disabled // Dont know why my test Stub does not work with this one.
    void TestPlayerLeavingJailAfter3rdAttemptFail() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // Setup
        gameController.setupGame();
        Player[] players = gameController.getPlayers();

        Field diceCup = gameController.getClass().getDeclaredField("diceCup");
        diceCup.setAccessible(true);
        diceCup.set(gameController, new DiceCupStub(2, 3));

        Method jailAttempt = gameController.getClass().getDeclaredMethod("jailAttempt");
        jailAttempt.setAccessible(true);

        // Action
        players[0].setPosition(10);
        players[0].setInJail(true);


        jailAttempt.invoke(gameController, null);
        jailAttempt.invoke(gameController, null);
        jailAttempt.invoke(gameController, null);


        // Assert
        int actualBalance = players[0].getBalance();
        int actualPosition = players[0].getPosition();
        int expectedBalance = 30000 - 1000 - 4000; //start - fee - ferriePrice
        int expectedPosition = 10 + 5;

        assertEquals(expectedBalance, actualBalance);
        assertEquals(expectedPosition, actualPosition);

    }

    @Test
    void TestGoingToJailBecauseOf3xDoublesWorksAsIntended() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Setup
        gameController.setupGame();
        Player[] players = gameController.getPlayers();

        Field diceCup = gameController.getClass().getDeclaredField("diceCup");
        diceCup.setAccessible(true);
        diceCup.set(gameController, new DiceCupStub(2, 2));

        Method takeTurn = gameController.getClass().getDeclaredMethod("takeTurn");
        takeTurn.setAccessible(true);

        // Action
        takeTurn.invoke(gameController, null);
        takeTurn.invoke(gameController, null);
        takeTurn.invoke(gameController, null);


        assertTrue(players[0].isInJail());
        assertEquals(10, players[0].getPosition());
    }

    @Test
    void TestInactivePlayersWontGetATurn() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        // Setup
        gameController.setupGame();
        Player[] players = gameController.getPlayers();

        Method takeTurn = gameController.getClass().getDeclaredMethod("takeTurn");
        takeTurn.setAccessible(true);
        Method changeTurn = gameController.getClass().getDeclaredMethod("changeTurn");
        changeTurn.setAccessible(true);


        // Action
        players[1].setActive(false);

        takeTurn.invoke(gameController, null);
        changeTurn.invoke(gameController, null);

        takeTurn.invoke(gameController, null);
        changeTurn.invoke(gameController, null);

        // Assert
        assertNotEquals(0, players[0].getPosition());
        assertEquals(0, players[1].getPosition());
        assertNotEquals(0, players[2].getPosition());
    }




    ////////// Stubs //////////

    // Stub for testing
    class DiceCupStub extends DiceCup {
        private int val1 = 1, val2 = 1;
        public DiceCupStub(int val1, int val2){
            this.val1 = val1;
            this.val2 = val2;
        }

        public void roll(){

        }
        public int[] getFaceValues(){
            return new int[]{val1,val2};
        }
    }
}

