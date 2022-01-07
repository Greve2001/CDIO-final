package Logic;

import Utilities.Language;

public class Main {
    public static void main(String[] args) {
        Language.getInstance();
        GameController matador = new GameController();
        matador.setupGame();
        matador.playGame();
    }
}
