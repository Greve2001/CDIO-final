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

        GUIController controller = new GUIController(board.getALL_SQUARES());
        GUIController.createPlayers(3, 6, 10000);

        GUIController.movePlayer("1", 0, -4);
    }
}