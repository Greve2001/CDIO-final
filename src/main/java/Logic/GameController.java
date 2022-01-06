package Logic;

import Board.Board;
import Interface.GUIController;

public class GameController {
    //object to be created
    private DiceCup diceCup();
    private Board board;
    private Player[] players;

    //other object references
    private Player currentPlayer;

    //final variable
    private final int START_MONEY = 30000;

    //other variables
    private int playersLeft;

    public void setupGame() {
        diceCup() = new Dicecup();
        board = new Board();

        GUIController gui = new GUIController();
        setupPlayers(gui.getPlayerNames());


    }

    private void setupPlayers(String[] playerNames) {
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++){
            players[i] = new Player(playerNames[i], START_MONEY, 0);
        }
        playersLeft = players.length;
    }

    public void playGame() {
        do {
            takeTurn();
            updateRemainingPlayerCount();
        }while (playersLeft != 1);
    }

    private void takeTurn() {
        //do stuff
    }

    private void updateRemainingPlayerCount() {
    }
}
