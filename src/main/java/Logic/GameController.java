package Logic;

import Board.Board;
import Interface.GUIController;

import java.util.Scanner;

public class GameController {
    //object to be created
    private DiceCup diceCup;
    private Board board;
    private Player[] players;

    //other object references
    private Player currentPlayer;
    private boolean hasExtraTurn = false;

    //final variable
    private final int START_MONEY = 30000;

    //other variables
    private int playersLeft;


    // TODO REMOVE THIS
    Scanner input = new Scanner(System.in);

    public void setupGame() {
        diceCup = new DiceCup();
        board = new Board();

        //GUIController gui = new GUIController(board.getALL_SQUARES());
        setupPlayers(new String[]{"1", "2", "3", "4"});



    }

    private void setupPlayers(String[] playerNames) {
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++){
            players[i] = new Player(playerNames[i], START_MONEY, 0);
        }
        playersLeft = players.length;
        currentPlayer = players[0];
    }

    public void playGame() {
        do {
            // Ask if want to buy houses etc.

            takeTurn(); // Only if player wants to throw dice

            if (!hasExtraTurn)
                changeTurn();

            hasExtraTurn = false; // Make sure that extra turn is reset


        }while (playersLeft != 1);
        // Stop game. Find winner
    }

    private void takeTurn() {
        input.nextLine();

        // Roll dice and get results
        diceCup.rollDice();
        int[] faceValues = diceCup.getFaceValues();
        int sum = faceValues[0] + faceValues[1];

        // Rolled double gives extra turn.
        // TODO if double 3times -> jail. (Could have)
        if (faceValues[0] == faceValues[1]) { // Means that player has rolled doubles
            hasExtraTurn = true;
        }
        
        board.updatePlayerPosition(currentPlayer, sum);

    }

    private void changeTurn(){
        // Get index of current player
        int currentPlayerIndex = java.util.Arrays.asList(players).indexOf(currentPlayer);

        if (currentPlayerIndex >= (players.length-1)){
            currentPlayer = players[0];
        }else{
            currentPlayer = players[currentPlayerIndex +1];
        }
    }

    //private void updateRemainingPlayerCount() {
    //}
}
