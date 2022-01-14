package Interface;

import Board.*;
import Utilities.*;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;
import java.util.Objects;

public class GUIController {
    private static boolean testing = false; // Used to allow easier testing

    private static GUI gui;
    private static Square[] fields;
    private static GUI_Field[] GUIFields;
    private static String[] playerNames;
    private static GUI_Player[] guiPlayers;

    private static final Color[] colorsToChooseFrom = new Color[]{Color.blue, Color.green, Color.yellow, Color.red, Color.magenta, Color.orange};
    // Changeable variables
    private static final int moveTime = 50; // Determines the time it take the car to move to the next square.


    // Allows easier testing
    public static void setTesting(boolean condition){
        testing = condition;
    }

    public GUIController(Square[] inputSquares){
        if (testing) return;

        GUI_Field[] GUIFields = createBoard(inputSquares);
        gui = new GUI(GUIFields);
    }


    public static GUI_Field[] createBoard(Square[] inputSquares){
        GUIFields = new GUI_Field[inputSquares.length]; // Allocate new array
        fields = inputSquares; // Save the squares for later uses

        for (int i = 0; i < GUIFields.length; i++) {
            switch (inputSquares[i].getClass().getSimpleName()) {
                case "Street" -> {

                    GUIFields[i] = new GUI_Street();
                    GUIFields[i].setBackGroundColor(convertColor(inputSquares[i].getColor()));
                    GUIFields[i].setDescription( // Rents
                            Language.get("baseRent") + ": " + inputSquares[i].getRent()[0] + "<br>" +
                                    Language.get("1house") + ": " + inputSquares[i].getRent()[1] + "<br>" +
                                    Language.get("2house") + ": " + inputSquares[i].getRent()[2] + "<br>" +
                                    Language.get("3house") + ": " + inputSquares[i].getRent()[3] + "<br>" +
                                    Language.get("4house") + ": " + inputSquares[i].getRent()[4] + "<br>" +
                                    Language.get("hotel") + ": " + inputSquares[i].getRent()[5] + "<br>"
                    );
                    ((GUI_Ownable) GUIFields[i]).setRent("");
                    GUIFields[i].setSubText(Language.get("price") + ": " + fields[i].getPrice());
                }
                case "Ferry" -> {
                    GUIFields[i] = new GUI_Shipping();
                    GUIFields[i].setBackGroundColor(Color.white);
                    GUIFields[i].setDescription( // Rents
                            Language.get("baseRent") + ": " + inputSquares[i].getRent()[0] + "<br>" +
                                    Language.get("2ferries") + ": " + inputSquares[i].getRent()[1] + "<br>" +
                                    Language.get("3ferries") + ": " + inputSquares[i].getRent()[2] + "<br>" +
                                    Language.get("4ferries") + ": " + inputSquares[i].getRent()[3] + "<br>"
                    );
                    GUIFields[i].setSubText(Language.get("price") + ": " + fields[i].getPrice());
                }
                case "Brewery" -> {
                    GUIFields[i] = new GUI_Brewery();
                    GUIFields[i].setBackGroundColor(Color.pink);
                    GUIFields[i].setDescription(Language.get("1brewery") +
                            "<br><br>" +
                            Language.get("2brewery"));
                    GUIFields[i].setSubText(Language.get("price") + ": " + fields[i].getPrice());
                }
                case "Chance" -> {
                    GUIFields[i] = new GUI_Chance();
                    GUIFields[i].setBackGroundColor(Color.pink);
                    GUIFields[i].setDescription(Language.get("pullChancecard"));
                    GUIFields[i].setSubText("");
                }
                case "Prison" -> {
                    GUIFields[i] = new GUI_Jail();
                    GUIFields[i].setSubText(Language.get("prison"));
                }
                case "GoToPrison" -> {
                    GUIFields[i] = new GUI_Street(); // Hopefully change
                    GUIFields[i].setSubText("");
                }
                case "IncomeTax" -> {
                    GUIFields[i] = new GUI_Tax();
                    GUIFields[i].setBackGroundColor(Color.red);
                    GUIFields[i].setDescription(Language.get("incomeTax"));
                    GUIFields[i].setSubText("");
                }
                case "Tax" -> {
                    GUIFields[i] = new GUI_Tax();
                    GUIFields[i].setBackGroundColor(Color.red);
                    GUIFields[i].setDescription(Language.get("extraTax"));
                    GUIFields[i].setSubText("");
                }
                case "Refugee" -> { // Parkering
                    GUIFields[i] = new GUI_Refuge();
                    GUIFields[i].setBackGroundColor(Color.white);
                    GUIFields[i].setSubText(Language.get("parking"));
                }
                case "Start" -> { // Parkering
                    GUIFields[i] = new GUI_Start();
                    GUIFields[i].setBackGroundColor(Color.red);
                    GUIFields[i].setDescription(Language.get("startHere"));
                    GUIFields[i].setSubText(Language.get("startHere"));
                }
                default -> GUIFields[i] = new GUI_Street(); // Change
            }

            GUIFields[i].setTitle(inputSquares[i].getName());
        }
        return GUIFields;
    }


    public static void createPlayers(int minPlayers, int maxPlayers, int startBalance){
        if (testing) return;

        // Needs an input from players
        String msg = Language.get("selectPlayers");
        String[] choices = new String[maxPlayers-minPlayers + 1];

        for (int i = 0; i < choices.length; i++) { // Fill up choices with differen number of player choices
            choices[i] = String.valueOf(minPlayers + i);
        }

        int numberOfPlayers = Integer.parseInt(gui.getUserSelection(msg, choices)); // Ask players

        // Prepare arrays
        playerNames = new String[numberOfPlayers];
        guiPlayers = new GUI_Player[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            // Get players name and car color
            playerNames[i] = gui.getUserString(Language.get("enterName"));

            // Add the player
            guiPlayers[i] = new GUI_Player(playerNames[i], startBalance); // To our array
            gui.addPlayer(guiPlayers[i]); // To the GUI itself

            // Add players car to square
            GUI_Field field = gui.getFields()[0];
            guiPlayers[i].getCar().setPrimaryColor(colorsToChooseFrom[i]);
            guiPlayers[i].getCar().setPosition(field);
        }
    }

    // Moves players on the GUI board
    public static void movePlayer(String playerName, int startPosition, int spacesToMove){
        if (testing) return;

        // Determines the way the player is moving
        int direction = 1;
        if (spacesToMove < 0)
            direction = -1;

        try {
            for (int i = 1; i <= Math.abs(spacesToMove); i++) {
                // Calculate new position
                int newPosition = (i * direction + startPosition) % fields.length;
                if (newPosition < 0) newPosition = fields.length + i * direction + startPosition;

                GUI_Field toField = gui.getFields()[newPosition]; // Get the GUI field
                int guiPlayerIndex = getGuiPlayerIndex(playerName); // Now get the players index, since the GUI does not track it

                // Set the car of the player
                guiPlayers[guiPlayerIndex].getCar().setPosition(toField);

                // Sleep, so the car stops breifly for the player to see movement better
                Thread.sleep(moveTime);
            }
        } catch (InterruptedException exception){ // Have to ensure that the Thread.sleeps does not cause errors
            System.out.println("This move did not work");
        }
    }
    // updates GUI toshow the players balance
    public static void setPlayerBalance(String playerName, int value){
        if (testing) return;

        int index = getGuiPlayerIndex(playerName);
        guiPlayers[index].setBalance(value);
    }


    // Gives access to logic to get the inputted names from the GUI.
    public static String[] getPlayerNames(){
        if (testing) return new String[]{"1", "2", "3"}; // Simple return for testing

        return playerNames;
    }

    // Players are given predetermined colors.
    public static Color[] getPlayerColors(){
        return colorsToChooseFrom;
    }


    // Show dice roll
    public static void showDice(int[] faceValues){
        if (testing) return;

        gui.setDice(faceValues[0], faceValues[1]);
    }

    // Sets houses on GUI Field
    public static void setHouses(int position, int amount){
        if (testing) return;

        Objects.requireNonNull(posToStreet(position)).setHouses(amount);
    }

    // Same as houses
    public static void setHotel(int position, boolean bool){
        if (testing) return;

        Objects.requireNonNull(posToStreet(position)).setHotel(bool);
    }

    // Put a name on the ownable of the player. This also displays the players color on the border for easier visualisation
    public static void setOwner(String playerName, Color playerColor, int position){
        if (testing) return;

        boolean ownable = fields[position].getOwnable(); // Fetch ownable

        if (ownable){
            if (!playerName.equals("")){
                ((GUI_Ownable) GUIFields[position]).setOwnerName(playerName);
                ((GUI_Ownable) GUIFields[position]).setBorder(playerColor);
                //TODO set the current rent or make another function for more uses
            }else{ // Used for reseting the field, if it's no longer owned
                ((GUI_Ownable) GUIFields[position]).setOwnerName(null);
                ((GUI_Ownable) GUIFields[position]).setBorder(null);
                // FOR READERS:
                // The GUI does not have functionally to remove a already existing border. So when setting it to null it will be black.
            }
        }else{
            System.out.println(Language.get("notAStreet"));
        }
    }

    // Updates the subtext of the Field to show the current rent/price etc. the player has to pay by landing there.
    public static void updateRent(int position, int currentCost){
        if (testing) return;

        GUIFields[position].setSubText(Language.get("rent") + ": " + currentCost);
    }


    // Gives players choice using a dropdown.
    public static String givePlayerChoice(String msg, String[] choices){
        if (testing) return choices[0];

        return gui.getUserSelection(msg, choices);
    }

    // Ask for players acceptence.
    public static boolean askPlayerAccept(String msg){
        if (testing) return true;

        String answer = gui.getUserSelection(msg, Language.get("yes"), Language.get("no"));
        return answer.equals(Language.get("yes"));
    }

    // Stops game-flow until button is pressed. Used for UX.
    public static void getPlayerAction(String playerName, String msg){
        if (testing) return;

        gui.showMessage(playerName + "; " + msg);
    }

    // Gets player to input integer
    public static int getPlayerInteger(String msg){
        if (testing) return 1;

        return gui.getUserInteger(msg);
    }

    // Display text in the middle of the GUI board.
    public static void showCenterMessage(String msg){
        if (!testing)
            gui.displayChanceCard(msg);
    }

    // Displays text with no button.
    public static void showMessage(String msg){
        if (!testing)
            gui.showMessage(msg);
    }




    //Used for getting player index since thats the only way we can find the corresponding GUI_Player
    private static int getGuiPlayerIndex(String playerName) {
        for (int i = 0; i < guiPlayers.length; i++) {
            if (guiPlayers[i].getName().equals(playerName)) {
                return i;
            }
        }
        return -1;
    }

    // Helps getting the field, when only the position is known.
    private static GUI_Street posToStreet(int position){
        GUI_Field field = gui.getFields()[position];
        if (field instanceof GUI_Street){
            return (GUI_Street) field;
        }else{
            System.out.println(Language.get("notAStreetAt" + position));
            return null;
        }
    }

    // Converts string colors to Color objects
    private static Color convertColor(String colorStr){
        return switch (colorStr.toLowerCase()) {
            case "red" -> Color.red;
            case "green" -> Color.green;
            case "blue" -> Color.cyan;
            case "yellow" -> Color.yellow;
            case "purple" -> Color.magenta;
            case "orange" -> Color.orange;
            case "grey" -> Color.gray; // Make grey
            case "white" -> Color.white;
            default -> Color.pink;
        };
    }
}
