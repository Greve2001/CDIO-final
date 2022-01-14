package Logic;

import Board.*;
import Cards.*;
import Interface.GUIController;
import SimpleClasses.Player;
import Utilities.Language;
import org.junit.jupiter.api.*;

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
    void pay10PctTax() {
        actionHandler.squareAction(players[0], squares[4], 10);

        int expected = 27000;
        int actual = players[0].getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void moveToNearest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method moveToNearest = actionHandler.getClass().getDeclaredMethod("moveToNearest",
                Player.class, String.class);
        moveToNearest.setAccessible(true);

        players[0].setPosition(2);
        Object[] args = {players[0], "Ferry"};
        moveToNearest.invoke(actionHandler, args);

        int expectedPos = 5;
        int acutalPos = players[0].getPosition();
        assertEquals(expectedPos, acutalPos);
        players[0].setPosition(7);
        moveToNearest.invoke(actionHandler, args);

        expectedPos = 15;
        acutalPos = players[0].getPosition();
        assertEquals(expectedPos, acutalPos);
        players[0].setPosition(17);
        moveToNearest.invoke(actionHandler, args);

        expectedPos = 25;
        acutalPos = players[0].getPosition();
        assertEquals(expectedPos, acutalPos);
        players[0].setPosition(22);
        moveToNearest.invoke(actionHandler, args);

        expectedPos = 25;
        acutalPos = players[0].getPosition();
        assertEquals(expectedPos, acutalPos);
        players[0].setPosition(33);
        moveToNearest.invoke(actionHandler, args);
        expectedPos = 35;
        acutalPos = players[0].getPosition();
        assertEquals(expectedPos, acutalPos);

        players[0].setPosition(36);
        moveToNearest.invoke(actionHandler, args);
        expectedPos = 5;
        acutalPos = players[0].getPosition();
        assertEquals(expectedPos, acutalPos);
    }

    @Test
    void declareAuctionWinner() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method declareAuctionWinner = actionHandler.getClass().getDeclaredMethod("declareAuctionWinner",
                boolean[].class, Square.class, int.class);
        declareAuctionWinner.setAccessible(true);
        actionHandler.setPlayers(players);

        boolean[] participants = {false, false, true};
        Object[] args = {participants, squares[1], 2000};
        declareAuctionWinner.invoke(actionHandler, args);

        Player expected = players[2];
        Player actual = squares[1].getOwner();

        assertEquals(expected, actual);
    }

    @Test
    void holdAuction() {
    }

    @Nested
    class ChanceCardTest{

        ChanceCard[] chanceCard;

        @BeforeEach
        void beforeEach(){
            chanceCard = new ChanceCard[7];

            chanceCard[0] = new PayMoneyToBankCard(Language.get("ccFuldtStop"), 1000);
            chanceCard[1] = new ReceiveMoneyFromBankCard(Language.get("ccVundetIKlasselotteriet"), 500);
            chanceCard[2] = new MoveNrOfFieldsCard(Language.get("ccRykTreFelterFrem"), 3);
            chanceCard[3] = new ReceiveMoneyFromPlayersCard(Language.get("ccFødselsdag"), 200);
            chanceCard[4] = new MoveToSpecificFieldCard(Language.get("ccRykFremTilStart"), 0);
            chanceCard[5] = new MatadorGrantCard(Language.get("ccMatadorLegat"), 40000);
            chanceCard[6] = new GetOutOfJailCard(Language.get("ccBenådningforFængsel"));

            players[0].setBalance(30000);
            players[0].setPosition(0);
        }

        @Test
        void payMoneyToBankTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method handlePayMoneyToBank = actionHandler.getClass().getDeclaredMethod("handlePayMoneyToBank", ChanceCard.class, Player.class);
            handlePayMoneyToBank.setAccessible(true);

            Object[] args = {chanceCard[0], players[0]};
            handlePayMoneyToBank.invoke(actionHandler,args);

            assertEquals(29000, players[0].getBalance());
        }

        @Test
        void ReceiveMoneyFromBankCardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method handleReceiveMoneyFromBank = actionHandler.getClass().getDeclaredMethod("handleReceiveMoneyFromBank", ChanceCard.class, Player.class);
            handleReceiveMoneyFromBank.setAccessible(true);

            Object[] args = {chanceCard[1], players[0]};
            handleReceiveMoneyFromBank.invoke(actionHandler,args);

            assertEquals(30500, players[0].getBalance());
        }

        @Test
        void MoveNrOfFieldsCardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method handleMoveNrOfFields = actionHandler.getClass().getDeclaredMethod("handleMoveNrOfFields", ChanceCard.class, Player.class);
            handleMoveNrOfFields.setAccessible(true);

            Object[] args = {chanceCard[2], players[0]};
            players[0].setPosition(17);
            handleMoveNrOfFields.invoke(actionHandler,args);

            assertEquals(30000, players[0].getBalance());
            assertEquals(20,players[0].getPosition());
        }

        @Test
        void ReceiveMoneyFromPlayersCardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method handleReceiveMoneyFromPlayers = actionHandler.getClass().getDeclaredMethod("handleReceiveMoneyFromPlayers", ChanceCard.class, Player.class, Player[].class);
            handleReceiveMoneyFromPlayers.setAccessible(true);

            Object[] args = {chanceCard[3], players[0], players};
            handleReceiveMoneyFromPlayers.invoke(actionHandler,args);

            assertEquals(30400, players[0].getBalance());
            assertEquals(29800, players[1].getBalance());
            assertEquals(29800, players[2].getBalance());
        }

        @Test
        void MoveToSpecificFieldCardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method handleMoveToSpecificField = actionHandler.getClass().getDeclaredMethod("handleMoveToSpecificField", ChanceCard.class, Player.class);
            handleMoveToSpecificField.setAccessible(true);

            players[0].setPosition(20);

            Object[] args = {chanceCard[4], players[0]};
            handleMoveToSpecificField.invoke(actionHandler,args);

            assertEquals(34000, players[0].getBalance());
            assertEquals(0, players[0].getPosition());
        }

        @Test
        void MatadorGrantCardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method handleMatadorGrantCard = actionHandler.getClass().getDeclaredMethod("handleMatadorGrantCard", ChanceCard.class, Player.class);
            handleMatadorGrantCard.setAccessible(true);

            Object[] args = {chanceCard[5], players[0]};
            handleMatadorGrantCard.invoke(actionHandler,args);

            assertEquals(30000, players[0].getBalance());

            players[0].setBalance(15000);
            handleMatadorGrantCard.invoke(actionHandler,args);

            assertEquals(55000, players[0].getBalance());
        }

        @Test
        void GetOutOfJailCardTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method handleGetOutOfJailCard = actionHandler.getClass().getDeclaredMethod("handleGetOutOfJailCard", Player.class);
            handleGetOutOfJailCard.setAccessible(true);

            Object[] args = {players[0]};
            handleGetOutOfJailCard.invoke(actionHandler,args);

            assertEquals(30000, players[0].getBalance());
            assertEquals(1, players[0].getGetOutJailCards());
        }
    }
}