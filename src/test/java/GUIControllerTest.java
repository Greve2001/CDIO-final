import Board.Board;
import Interface.GUIController;
import Logic.DiceCup;
import Logic.Player;
import Utilities.Language;
import org.junit.jupiter.api.BeforeEach;

class GUIControllerTest {
    public static void main(String[] args) throws InterruptedException {
        Language.getInstance();

        Board board = new Board();
        Player test = new Player("Test", 0, 0);

        GUIController controller = new GUIController(board);

    }
}