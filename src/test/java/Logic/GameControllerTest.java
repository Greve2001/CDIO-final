package Logic;
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













}
