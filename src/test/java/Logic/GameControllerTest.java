package Logic;
import SimpleClasses.Player;
import gui_fields.GUI_Car;
import gui_fields.GUI_Player;
import gui_main.GUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    GameController gameController;

    @BeforeEach
    void setUp(){
        gameController = new GameController();
    }

    @Test
    void testAllowedNumberOfPlayers()
    {
        int[] numOfPlayers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] expectedNumPlayers = {0, 0, 0, 3 , 4, 5, 6, 0, 0, 0};

        for (int i = 0; i < numOfPlayers.length; i++){
            String[] playerNames = new String[numOfPlayers[i]];
            for (int j = 0; j < numOfPlayers[i]; j++){
                playerNames[j] = Integer.toString(j);
            }
            gameController.setupPlayers(playerNames);

            int actualNumOfPlayers = gameController.getPlayers().length;

            assertEquals(expectedNumPlayers[i], actualNumOfPlayers);
        }
    }


    @Test
    void TestEveryPlayerStartBalance(){
        gameController.setupPlayers(new String[]{"1", "2", "3", "4"});
        Player[] players = gameController.getPlayers();

        int expectedStartBalance = 30000;

        for (Player player : players){
            assertEquals(expectedStartBalance, player.getBalance());
        }
    }

    @Test
    void TestEveryPlayerHasATokenOnTheBoard(){
        GUI gui = new GUI();

        gameController.setupPlayers(new String[]{"1", "2", "3", "4"});
        Player[] players = gameController.getPlayers();

        for (Player player : players){
            GUI_Player GUIplayer = new GUI_Player(player.getName());
            gui.addPlayer(GUIplayer);

            GUI_Car car = GUIplayer.getCar();
            assertNotNull(car);
        }
    }

    @Test
    void ensureInactivePlayersCannotPlay(){
        gameController.setupPlayers(new String[]{"1", "2", "3", "4"});
        Player[] players = gameController.getPlayers();

        Player currentPlayer = players[0];

        // Again what to do? Cannot access the right methods
        //TODO when figuring out how to make innerclass
    }

    @Test
    void TestFreeFromJailCard(){
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
}
