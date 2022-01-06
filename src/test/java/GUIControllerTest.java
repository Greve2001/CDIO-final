import Board.Board;
import Interface.GUIController;
import Logic.Player;
import org.junit.jupiter.api.BeforeEach;

class GUIControllerTest {
    public static void main(String[] args) {
        Board board = new Board();

        GUIController controller = new GUIController(board.getALL_SQUARES());

        controller.setHouses(3, 4);

        controller.setHotel(3, true);

        controller.setOwner(new Player("Test", 10, 0), 3);
    }
}