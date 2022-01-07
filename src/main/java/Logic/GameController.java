package Logic;

import Board.Board;
import Interface.GUIController;
import Utilities.Language;

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
    private final int BRIBE = 1000;

    //other variables
    private int playersLeft;


    // TODO REMOVE THIS
    Scanner input = new Scanner(System.in);

    public void setupGame() {
        Language.getInstance();
        diceCup = new DiceCup();
        board = new Board(diceCup);

        GUIController gui = new GUIController(board);

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

    public void playGame() throws InterruptedException {
        do {
            // Ask if want to buy houses etc.
            String msg = currentPlayer.getName() + ": " + Language.get("askAction");
            String[] choices = new String[]{Language.get("choice1"), Language.get("choice2")};
            String answer = GUIController.givePlayerChoice(msg, choices);

            String case1 = Language.get("choice1");
            String case2 = Language.get("choice2");


            if (currentPlayer.isInJail()){ // In jail
                jailAttempt();

            } else if (answer.equals(case1)){ // Throw Dice
                takeTurn();

            } else if(answer.equals(case2)){ // Buy Property
                // Make more indepth logic to buy houses, hotels, sell them, place them
                // Temp text for GUI. REMOVE
                GUIController.getPlayerAction(currentPlayer, Language.get("choice2"));

                // When done buying, take turn.
                takeTurn();
            }

            if (!hasExtraTurn){
                changeTurn();
                doublesRolled = 0;
            }

            hasExtraTurn = false; // Make sure that extra turn is reset


        }while (playersLeft != 1);
        // Stop game. Find winner
    }

    private void takeTurn() throws InterruptedException {
        // Roll dice and get results
        diceCup.rollDice();
        int[] faceValues = diceCup.getFaceValues();


        // Show dice on GUI
        GUIController.showDice(faceValues);

        // Rolled double gives extra turn.
        if (isDoubles(faceValues)) { // Means that player has rolled doubles
            hasExtraTurn = true;
            doublesRolled++;

            // If 3 doubles are rolled, go to jail
            board.setPlayerInJail(currentPlayer);
        }

        // TODO make sure that the player positions then get update in the Board class
        board.updatePlayerPosition(currentPlayer, diceSum(faceValues));
        

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

    private void jailAttempt(){
        // Give player choices between paying or trying to throw dice
        String msg = "Choose";
        String[] choices = {"Attempt Escaping", "Pay 1000"}; // Make Dynamic
        String answer = GUIController.givePlayerChoice(msg, choices);

        if (answer.equals(choices[0])){ // Attempt escaping
            // Throw Dice
            diceCup.rollDice();
            int[] diceRoll = diceCup.getFaceValues();
            GUIController.showDice(diceRoll);

            // See if doubles
            if (isDoubles(diceRoll)){
                escapeJail(diceSum(diceRoll));

            } else {
                currentPlayer.addJailEscapeAttempt();

                if (currentPlayer.getJailEscapeAttempts() >= 3){
                    Bank.payToBank(currentPlayer, BRIBE);
                }
                escapeJail(diceSum(diceRoll));
            }

        } else { // Pay the bribe
            Bank.payToBank(currentPlayer, BRIBE);

        }
    }
    private void escapeJail(int diceSum){
        currentPlayer.setInJail(false); // Break out of jail
        currentPlayer.resetJailEscapeAttempts();
        board.updatePlayerPosition(currentPlayer, diceSum);
    }

    private boolean isDoubles(int[] faceValues){
        return faceValues[0] == faceValues[1];
    }
    private int diceSum(int[] faceValues){
        return faceValues[0] + faceValues[1];
    }
}
