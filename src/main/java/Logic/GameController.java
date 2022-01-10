package Logic;

import Board.Board;
import Interface.GUIController;
import Utilities.Language;
import gui_main.GUI;
import org.apache.commons.codec.language.bm.Lang;

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
        board = new Board();

        GUIController gui = new GUIController(board);

        GUIController.createPlayers(START_MONEY);
        setupPlayers(GUIController.getPlayerNames());

        board.givePlayerToActionHandler(players);

    }

    private void setupPlayers(String[] playerNames) {
        players = new Player[playerNames.length];
        for (int i = 0; i < playerNames.length; i++){
            players[i] = new Player(playerNames[i], START_MONEY, 0);
            players[i].setColor(GUIController.getPlayerColors()[i]);
        }
        playersLeft = players.length;
        currentPlayer = players[0];
    }

    public void playGame() throws InterruptedException {
        boolean allowedDecision;
        do {
            allowedDecision = true;
            if (currentPlayer.isInJail()){ // In jail. No other options
                allowedDecision = jailAttempt();

            }
            if (allowedDecision){ // Not in jail or gotten out in good standing.

                // Ask if want to buy houses etc.
                String msg = currentPlayer.getName() + ": " + Language.get("askAction");
                String[] choices = new String[]{Language.get("choice1"), Language.get("choice2")};
                String answer = GUIController.givePlayerChoice(msg, choices);

                String case1 = Language.get("choice1");
                String case2 = Language.get("choice2");

                if (answer.equals(case1)){ // Throw Dice
                    takeTurn();

                } else if(answer.equals(case2)){ // Buy Property
                    // Make more indepth logic to buy houses, hotels, sell them, place them
                    // Temp text for GUI. REMOVE
                    GUIController.getPlayerAction(currentPlayer, Language.get("choice2"));

                    // When done buying, take turn.
                    takeTurn();
                }
            }

            if (!hasExtraTurn){
                changeTurn();
                doublesRolled = 0;
            }

            hasExtraTurn = false; // Make sure that extra turn is reset

            checkForBust();

        }while (playersLeft != 1);
        // Stop game. Find winner
        findWinner();
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
            if (doublesRolled >= 3){
                board.setPlayerInJail(currentPlayer);
            }

        }

        // TODO make sure that the player positions then get update in the Board class
        board.updatePlayerPosition(currentPlayer, diceSum(faceValues));

    }

    private void changeTurn(){
        int currentPlayerIndex;
        boolean isNotActive;
        do { // Run thorugh until find one active player

            // Get index of current player
            currentPlayerIndex = java.util.Arrays.asList(players).indexOf(currentPlayer);

            if (currentPlayerIndex >= (players.length-1))
                currentPlayer = players[0];
            else
                currentPlayer = players[currentPlayerIndex +1];


            currentPlayerIndex = java.util.Arrays.asList(players).indexOf(currentPlayer); // Set again since we need to check in while-loop.
            isNotActive = !players[currentPlayerIndex].getActive();
        }while (isNotActive);
    }

    private boolean jailAttempt() {
        // Give player choices between paying or trying to throw dice
        String msg = "Choose";

        //construction the different disicions the player have access to
        String[] choices;
        if (currentPlayer.getGetOutJailCards() > 0){
            choices = new String[3];
            choices[2] = Language.get("useEscapeCard"); // Make Dynamic
        }else{
            choices = new String[2];
        }
        choices[0] = Language.get("attemptEscaping");
        choices[1] = Language.get("payFee");

        String answer = GUIController.givePlayerChoice(msg, choices);

        boolean forcedToMove = false, haveToPay = false, usedChanceCard = false;
        boolean result = true;


        String case1 = Language.get("attemptEscaping");
        String case2 = Language.get("payFee");
        String case3 = Language.get("useEscapeCard");

        // Need to be if-else because of Language
        if (answer.equals(case1)){ // Attempt escape

            forcedToMove = true;//if player escape, they are forced to move what ever they rolled

            // Throw Dice
            diceCup.rollDice();
            int[] diceRoll = diceCup.getFaceValues();
            GUIController.showDice(diceRoll);

            // See if doubles
            if (!isDoubles(diceRoll)) {
                result = false;
                currentPlayer.addJailEscapeAttempt();
                GUIController.showMessage(Language.get("didNotEscape"));

                if (currentPlayer.getJailEscapeAttempts() >= 3) {
                    haveToPay = true;
                    result = true;
                    GUIController.showMessage(Language.get("noMoreTries"));
                }
            }

        }else if (answer.equals(case2)){ // Pay fee
            haveToPay = true;
        }else if (answer.equals(case3)){ // Use free of jail card
            usedChanceCard = true;
        }else{
            //TODO Exception handling
        }

        if(result){
            GUIController.showMessage(Language.get("youBrokeFree"));
            board.escapeJail(currentPlayer, diceCup.getSum(),forcedToMove, haveToPay, usedChanceCard);
        }


        return !forcedToMove;
    }

    private void checkForBust(){ // Checks all players
        playersLeft = players.length;

        for (Player player : players){
            if (player.getBalance() <= 0 || player.getActive() == false){
                player.setActive(false);
                playersLeft--;
            }
        }
    }

    private void findWinner(){
        Player winner = players[0]; // Somewhere to start
        for (Player player : players){
            if (player.getBalance() > winner.getBalance()){
                winner = player;
            }
        }

        // Winner is...
        GUIController.showMessage(Language.get("winnerIs") + winner);
    }

    private boolean isDoubles(int[] faceValues){
        return faceValues[0] == faceValues[1];
    }
    private int diceSum(int[] faceValues){
        return faceValues[0] + faceValues[1];
    }
}