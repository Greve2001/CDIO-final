package Board;

import Logic.DiceCup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Logic.Player;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board;
    Player player;

    @BeforeEach
    void setUp() {
        board = new Board(new DiceCup());
        player = new Player("test", 30000, 0);
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
    }
}