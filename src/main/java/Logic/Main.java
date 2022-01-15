package Logic;

import Utilities.Language;

public class Main {
    public static void main(String[] args) {
        Language.getInstance();
        GameController matador = new GameController();
        matador.setupGame();

        // FOR TESTING BUG REMOVE
        Player[] players = matador.getPlayers();
        players[0].setBalance(500000);
        players[1].setBalance(500000);
        players[2].setBalance(500000);

        Board board = matador.getBoard();
        Square[] squares = board.getALL_SQUARES();

        squares[1].setOwner(players[0]);
        squares[3].setOwner(players[0]);
        // END OF TESTING CONDITIONS

        matador.playGame();
    }
}
