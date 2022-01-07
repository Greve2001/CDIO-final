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

    // Extra turn varaibles
    private boolean hasExtraTurn = false;
    private int doublesRolled = 0;

    //final variable
    private final int START_MONEY = 30000;

    //other variables
    private int playersLeft;


    // TODO REMOVE THIS
    Scanner input = new Scanner(System.in);

    public void setupGame() {
        diceCup = new DiceCup();
        board = new Board();

        GUIController gui = new GUIController(board.getALL_SQUARES());

        GUIController.createPlayers(START_MONEY);
        setupPlayers(GUIController.getPlayerNames());

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
            String msg = currentPlayer.getName() + ": Choose action";
            String[] choices = new String[]{"throwDice", "buyProperty"};
            String answer = GUIController.givePlayerChoice(msg, choices);

            switch (answer){
                case "throwDice" :
                    takeTurn(); // Only if player wants to throw dice
                    break;

                case "buyProperty" :
                    // Make more indepth logic to buy houses, hotels, sell them, place them
                    // Temp text for GUI. REMOVE
                    GUIController.getPlayerAction(currentPlayer, "Buy properties");

                    // When done buying, take turn.
                    takeTurn();
                    break;
            }

            if (!hasExtraTurn){
                changeTurn();
                doublesRolled = 0;
            }

            hasExtraTurn = false; // Make sure that extra turn is reset


        }while (playersLeft != 1);
        // Stop game. Find winner
    }

    private void takeTurn() {
        // Roll dice and get results
        diceCup.rollDice();
        int[] faceValues = diceCup.getFaceValues();
        int sum = faceValues[0] + faceValues[1];

        // Show dice on GUI
        GUIController.showDice(faceValues);

        // Rolled double gives extra turn.
        if (faceValues[0] == faceValues[1]) { // Means that player has rolled doubles
            hasExtraTurn = true;
            doublesRolled++;

            // If 3 doubles are rolled, go to jail
            board.setPlayerInJail(currentPlayer);
        }

        // TODO make sure that the player positions then get update in the Board class
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
