package Logic;

import Board.Board;
import Interface.GUIController;
import SimpleClasses.DiceCup;
import SimpleClasses.Player;
import Utilities.Language;

public class GameController {
    private DiceCup diceCup;
    private Board board;
    private Player[] players;
    private Player currentPlayer;
    private int playersLeft;

    //changeable variables
    private final int minPlayers = 3, maxPlayers = 6;
    private int doublesRolled = 0;
    private final int START_MONEY = 30000;


    public void setupGame() {
        Language.getInstance(); // Needs an initial initialisation

        // Create related objects
        diceCup = new DiceCup();
        board = new Board();
        new GUIController(board.getALL_SQUARES());

        // Create players in GameController and GUIController
        GUIController.createPlayers(minPlayers, maxPlayers, START_MONEY);
        setupPlayers(GUIController.getPlayerNames());

        board.givePlayerToActionHandler(players); // Pass on players to board
    }

    // Creates players with GUIController aswell.
    public void setupPlayers(String[] playerNames) {
        if (playerNames.length >= minPlayers && playerNames.length <= maxPlayers) { // Ensure allowed number of players
            players = new Player[playerNames.length];

            for (int i = 0; i < playerNames.length; i++) {
                players[i] = new Player(playerNames[i], START_MONEY, 0);
                players[i].setColor(GUIController.getPlayerColors()[i]);
            }
            playersLeft = players.length;
            currentPlayer = players[0]; // Always set the starting players as the first one created.

        }else{ // Exception if for some reason a not allowed input is used.
            players = new Player[]{};
        }
    }

    // Handles the game flow.
    public void playGame() {
        boolean extraTurnTextEnabled = false;

        do { // Loops until only one player left.
            boolean allowedToDecide = true;

            // If player does not escape jail. They cannot make any other actions
            if (currentPlayer.isInJail())
                allowedToDecide = jailAttempt(); // False if not out of jail

            if (allowedToDecide) { // If player is allowed to make actions.

                // Give player choice; Throw, Buy, sell

                String actionMsg = currentPlayer.getName() + ": " + Language.get("askAction");
                if (extraTurnTextEnabled)
                    actionMsg = Language.get("haveExtraTurn") + actionMsg;

                extraTurnTextEnabled = false;

                String[] actionChoices;
                String actionAnswer;
                String action1 = Language.get("choice1"); // Throw Dice

                // If the player does not have monoply. No other choice than taking turn.
                if (board.allMonopolyColorsByPlayer(currentPlayer).length > 0) {
                    String action2 = Language.get("choice2"); // Buy properties
                    String action3 = Language.get("choice3"); // Sell properties
                    actionChoices = new String[]{action1, action2, action3};
                    actionAnswer = GUIController.givePlayerChoice(actionMsg, actionChoices);

                    // OBS! Can not use switch because of the implementation of Language  OBS!
                    // Do action depending on answer.
                    if (actionAnswer.equals(action2)) // Buy
                        manageProperty("buy");
                    else if (actionAnswer.equals(action3)) // Sell
                        manageProperty("sell");

                } else { // Only give prompt to throw dice
                    actionChoices = new String[]{action1};
                    actionAnswer = GUIController.givePlayerChoice(actionMsg, actionChoices);
                }

                // Always take turn after other actions.
                if (actionAnswer.equals(action1)) // Dont asker for another prompt if this is the first choice
                    takeTurn();
                else { // Only make prompt if throwing dice was not the first option.
                    GUIController.getPlayerAction(currentPlayer.getName(), Language.get("throwDice"));
                    takeTurn();
                }
            }

            // Check for extra turn
            if (!currentPlayer.getHasExtraTurn()){
                changeTurn();
                doublesRolled = 0;
            }else
                extraTurnTextEnabled = true;
            currentPlayer.setHasExtraTurn(false); // Make sure that extra turn is reset
            checkForBust(); // See if any player has gone bust.

        }while (!(playersLeft <= 1));
        // Game is stopped. Find the winner
        findWinner();
    }



    private void manageProperty(String action){
        boolean keepGoing = true;

        do {
            // Construct message
            String msg = "";
            if (action.equals("buy")) msg = Language.get("whatToBuy?");
            if (action.equals("sell")) msg = Language.get("whatToSell?");
            String[] choices = new String[]{Language.get("houses"), Language.get("hotels"), Language.get("stop")};
            String answer = GUIController.givePlayerChoice(msg, choices);

            // Sort after answer
            if (answer.equals(choices[0]) || answer.equals(choices[1])){ // Houses and hotels. Have same price
                // TODO ensure that the player can buy property if the have monopoly on that street color

                String[] colorsToChooseFrom = board.allMonopolyColorsByPlayer(currentPlayer);
                msg = Language.get("chooseStreetColor");

                if (colorsToChooseFrom.length <= 0){ // Does not have monopoly. Cannot buy or sell
                    GUIController.getPlayerAction(currentPlayer.getName(), Language.get("noMonopoly"));
                    break;
                }

                String colorChosen = GUIController.givePlayerChoice(msg, colorsToChooseFrom);

                int housePrice = board.getHousePrice(colorChosen); // Same as hotelprice

                String howMany = "";
                if (action.equals("buy")) howMany = Language.get("howManyToBuy?");
                if (action.equals("buy")) howMany = Language.get("howManyToSell?");


                int amount = GUIController.getPlayerInteger(howMany + housePrice);
                if (amount <= 0){ // If inputted 0 or negative, exit
                    break;
                }

                // Make the deal
                if (answer.equals(choices[0])) {// houses
                    if (action.equals("buy")) {
                        board.buyHouse(currentPlayer, colorChosen, amount);
                    }
                    else if (action.equals("sell")){
                        board.sellBuilding(currentPlayer, colorChosen, "house", amount);
                    }

                }

                if (answer.equals(choices[1])) {
                    if (action.equals("buy")) {
                        board.buyHotel(currentPlayer, colorChosen, amount);
                    }
                    else if (action.equals("sell")){
                        board.sellBuilding(currentPlayer, colorChosen, "hotel", amount);
                    }
                }
            } else  // Stop
                keepGoing = false;

        }while (keepGoing);
    }


    private void takeTurn() {
        // Roll dice and get results
        diceCup.rollDice();
        int[] faceValues = diceCup.getFaceValues();

        // Show dice on GUI
        GUIController.showDice(faceValues);

        // Rolled double gives extra turn.
        if (isDoubles(faceValues)) { // Means that player has rolled doubles
            currentPlayer.setHasExtraTurn(true);
            doublesRolled++;

            // If 3 doubles are rolled, go to jail
            if (doublesRolled >= 3){
                board.setPlayerInJail(currentPlayer);
            }
        }
        // Make board move the player
        if (!currentPlayer.isInJail())
            board.updatePlayerPosition(currentPlayer, diceSum(faceValues));
    }


    private void changeTurn(){
        int currentPlayerIndex;
        boolean isNotActive;
        do { // Run thorugh until find one active player

            // Get index of current player
            currentPlayerIndex = java.util.Arrays.asList(players).indexOf(currentPlayer);

            if (currentPlayerIndex >= (players.length-1)) // If last in array, set new currentPlayer as index 0
                currentPlayer = players[0];
            else
                currentPlayer = players[currentPlayerIndex +1]; // If not add end of array, take next index

            currentPlayerIndex = java.util.Arrays.asList(players).indexOf(currentPlayer); // Set again since we need to check after aswell
            isNotActive = !players[currentPlayerIndex].getActive();

        }while (isNotActive);
    }


    private boolean jailAttempt() {
        // Give player choices between paying or trying to throw dice
        String msg = Language.get("choose");

        //construction the different disicions the player have access to
        String[] choices;
        if (currentPlayer.getGetOutJailCards() > 0){
            choices = new String[3];
            choices[2] = Language.get("useEscapeCard"); // Make Dynamic
        }else
            choices = new String[2];

        choices[0] = Language.get("attemptEscaping");
        choices[1] = Language.get("payFee");
        String answer = GUIController.givePlayerChoice(msg, choices);

        boolean forcedToMove = false, haveToPay = false, usedChanceCard = false;
        boolean result = true;


        //OBS! Need to be if-else because of Language OBS!
        if (answer.equals(choices[0])){ // Attempt escape
            forcedToMove = true;//if player escape, they are forced to move whatever they rolled

            // Throw Dice
            diceCup.rollDice();
            int[] diceRoll = diceCup.getFaceValues();
            GUIController.showDice(diceRoll);

            // See if doubles
            if (!isDoubles(diceRoll)) {
                result = false;
                currentPlayer.addJailEscapeAttempt();
                GUIController.showMessage(Language.get("didNotEscape"));

                // If thirds try is a fail.
                if (currentPlayer.getJailEscapeAttempts() >= 3) {
                    haveToPay = true;
                    result = true;
                    GUIController.showMessage(Language.get("noMoreTries"));
                }
            }

        }else if (answer.equals(choices[1])){ // Pay fee
            haveToPay = true;
        }else if (answer.equals(choices[2])){ // Use free of jail card
            usedChanceCard = true;
        }else{
            System.out.println("Not know answer: " + answer);
        }

        if(result){
            GUIController.showMessage(Language.get("youBrokeFree"));
            board.escapeJail(currentPlayer, diceCup.getSum(), forcedToMove, haveToPay, usedChanceCard);
            currentPlayer.resetJailEscapeAttempts();
        }
        return !forcedToMove;
    }


    private void checkForBust(){ // Checks all players
        playersLeft = players.length;

        for (Player player : players){
            if (player.getBalance() <= 0 || !player.getActive()){
                player.setActive(false); // Make Inactive
                player.setHasExtraTurn(false); // Ensure that they dont have extra turn.
                board.playerGoingBankrupt(player); //removes all owned property from player
                playersLeft--;
            }
        }
    }


    // Finds the player with the highest balance
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

    public Player[] getPlayers() {
        return players;
    }
}