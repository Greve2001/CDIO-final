package Board;

import org.junit.jupiter.api.Test;
import SimpleClasses.Player;

import static org.junit.jupiter.api.Assertions.*;

public class BoardCSVTest {
    Board board;
    Player player;
    Square[] ALL_SQUARES;

    @Test
    void exceptionCSV(){
        System.setProperty("user.language","en");
        assertEquals("en", System.getProperty("user.language"));
        board = new Board();
    }



}
