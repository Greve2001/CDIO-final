package Interface;

import Board.*;
import Logic.*;
import Utilities.*;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;

public class GUIController {

    private static boolean testing = false; // Used to allow easier testing

    private static GUI gui;
    private static Color[] colorsToChooseFrom = new Color[]{Color.blue, Color.green, Color.yellow, Color.red, Color.magenta, Color.orange};
    private static String[] playerNames;
    private static GUI_Player[] guiPlayers;
    private static int[] playerPositions;
    private static GUI_Field[] GUIFields;
    private static Square[] fields;

    // Changeable variables
    private static int moveTime = 20;


    public GUIController(Square[] inputSquares){
        if (testing) return;

        GUI_Field[] GUIFields = createBoard(inputSquares);
        gui = new GUI(GUIFields);
    }


    public static GUI_Field[] createBoard(Square[] inputSquares){
        GUIFields = new GUI_Field[inputSquares.length]; // Create empty array7
        fields = inputSquares;

        for (int i = 0; i < GUIFields.length; i++) {
            switch (inputSquares[i].getClass().getSimpleName()){
                case "Street" :
                    //GUIFields[i] = new GUI_Street();
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
                    GUIFields[i].setSubText(Language.get("price") +": " + String.valueOf(fields[i].getPrice()));
                    break;


                case "Ferry" :
                    GUIFields[i] = new GUI_Shipping();
                    GUIFields[i].setBackGroundColor(Color.white);
                    GUIFields[i].setDescription( // Rents
                            Language.get("baseRent") + ": " + inputSquares[i].getRent()[0] + "<br>" +
                                    Language.get("2ferries") + ": "  + inputSquares[i].getRent()[1] + "<br>" +
                                    Language.get("3ferries") + ": "  + inputSquares[i].getRent()[2] + "<br>" +
                                    Language.get("4ferries") + ": "  + inputSquares[i].getRent()[3] + "<br>"
                    );
                    GUIFields[i].setSubText(Language.get("price") +": " + String.valueOf(fields[i].getPrice()));
                    break;

                case "Brewery" :
                    GUIFields[i] = new GUI_Brewery();
                    GUIFields[i].setBackGroundColor(Color.pink);
                    GUIFields[i].setDescription(Language.get("1brewery") +
                            "<br><br>" +
                            Language.get("2brewery"));
                    GUIFields[i].setSubText(Language.get("price") +": " + String.valueOf(fields[i].getPrice()));
                    break;

                case "Chance" :
                    GUIFields[i] = new GUI_Chance();
                    GUIFields[i].setBackGroundColor(Color.pink);
                    GUIFields[i].setDescription(Language.get("pullChancecard"));
                    GUIFields[i].setSubText("");
                    break;

                case "Prison" :
                    GUIFields[i] = new GUI_Jail();
                    GUIFields[i].setSubText(Language.get("prison"));
                    break;

                case "GoToPrison" :
                    GUIFields[i] = new GUI_Street(); // Hopefully change
                    GUIFields[i].setSubText("");
                    break;

                case "IncomeTax" :
                    GUIFields[i] = new GUI_Tax();
                    GUIFields[i].setBackGroundColor(Color.red);
                    GUIFields[i].setDescription(Language.get("incomeTax"));
                    GUIFields[i].setSubText("");
                    break;

                case "Tax" :
                    GUIFields[i] = new GUI_Tax();
                    GUIFields[i].setBackGroundColor(Color.red);
                    GUIFields[i].setDescription(Language.get("extraTax"));
                    GUIFields[i].setSubText("");
                    break;

                case "Refugee" : // Parkering
                    GUIFields[i] = new GUI_Refuge();
                    GUIFields[i].setBackGroundColor(Color.white);
                    GUIFields[i].setSubText(Language.get("parking"));
                    break;

                case "Start" : // Parkering
                    GUIFields[i] = new GUI_Start();
                    GUIFields[i].setBackGroundColor(Color.red);
                    GUIFields[i].setDescription(Language.get("startHere"));
                    GUIFields[i].setSubText(Language.get("startHere"));
                    break;

                default:
                    GUIFields[i] = new GUI_Street(); // Change
                    break;
            }

            GUIFields[i].setTitle(inputSquares[i].getName());
        }

        return GUIFields;
    }


    public static void createPlayers(int minPlayers, int maxPlayers, int startBalance){
        // Needs an input
        if (testing) return;

        String msg = Language.get("selectPlayers");
        String[] choices = new String[maxPlayers-minPlayers + 1];
        for (int i = 0; i < choices.length; i++) {
            choices[i] = String.valueOf(minPlayers + i);
        }

        int numberOfPlayers = Integer.parseInt(gui.getUserSelection(msg, choices));

        playerNames = new String[numberOfPlayers]; // Empty name array
        guiPlayers = new GUI_Player[numberOfPlayers];
        playerPositions = new int[numberOfPlayers];

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

            // Keep track of position since GUI only uses Field for reference
            playerPositions[i] = 0;
        }
    }


/// Player Section ///
    public static void movePlayer(String playerName, int startPosition, int spacesToMove){ // Make sure it needs the Player or name of Player
        if (testing) return;

        int direction = 1;
        if (spacesToMove < 0)
            direction = -1;

        try {
            for (int i = 1; i <= Math.abs(spacesToMove); i++) {
                // Get informations
                int newPosition = direction * (i + startPosition) % fields.length;
                if (newPosition < 0) newPosition = fields.length + direction * (i + startPosition);

                GUI_Field toField = gui.getFields()[newPosition];
                int guiPlayerIndex = getGuiPlayerIndex(playerName);

                // Set the car
                guiPlayers[guiPlayerIndex].getCar().setPosition(toField);

                // Sleep, so the car stops breifly for the player to see movement
                Thread.sleep(moveTime);
            }
        } catch (InterruptedException exception){ // Needs to be made beacuse of thread.sleep()
            System.out.println(exception);
        }
    }


    public static void setPlayerBalance(String playerName, int value){
        if (testing) return;

        int index = getGuiPlayerIndex(playerName);
        guiPlayers[index].setBalance(value);
    }


    // Gives access to logic to get the inputted names from the GUI.
    public static String[] getPlayerNames(){
        if (testing) return new String[]{"1", "2", "3"};

        return playerNames;
    }

    //TODO implement solution to let player select colors
    public static Color[] getPlayerColors(){
        if (testing) return new Color[]{Color.red, Color.orange, Color.blue};

        return colorsToChooseFrom;
    }


/// Action Section ///
    // Show rolled dice
    public static void showDice(int[] faceValues){
        if (testing) return;

        gui.setDice(faceValues[0], faceValues[1]);
    }

    public static void setHouses(int position, int amount){
        if (testing) return;

        if (posToStreet(position) != null)
            posToStreet(position).setHouses(amount);
    }

    public static void setHotel(int position, boolean bool){
        if (testing) return;

        if (posToStreet(position) != null)
            posToStreet(position).setHotel(bool);
    }


    public static void setOwner(String playerName, Color playerColor, int position){
        if (testing) return;

        boolean ownable = fields[position].getOwnable();
        if (ownable){
            if (!playerName.equals("")){
                ((GUI_Ownable) GUIFields[position]).setOwnerName(playerName);
                ((GUI_Ownable) GUIFields[position]).setBorder(playerColor);
                //TODO set the current rent or make another function for more uses
            }else{
                ((GUI_Ownable) GUIFields[position]).setOwnerName(null);
                ((GUI_Ownable) GUIFields[position]).setBorder(null);
                // FOR READERS:
                // The GUI does not have functionally to remove a already existing border. So when setting it to null it will be black.
            }
        }else{
            System.out.println(Language.get("notAStreet"));
        }
    }

    public static void updateRent(int position, int currentCost){
        if (testing) return;

        GUIFields[position].setSubText(Language.get("rent") + ": " + String.valueOf(currentCost));
    }



//// Player choice and inputs ////
    public static String givePlayerChoice(String msg, String[] choices){
        if (testing) return choices[0];

        return gui.getUserSelection(msg, choices);
    }

    public static boolean askPlayerAccept(String msg){
        if (testing) return true;

        String answer = gui.getUserSelection(msg, Language.get("yes"), Language.get("no"));
        if (answer.equals(Language.get("yes")))
            return true;
        else
            return false;
    }

    // Stops game-flow until button is pressed. Used for UX.
    public static void getPlayerAction(String playerName, String msg){
        if (testing) return;

        gui.showMessage(playerName + "; " + msg);
    }

    public static int getPlayerInteger(String msg){
        if (testing) return 1;

        return gui.getUserInteger(msg);
    }

    public static void showCenterMessage(String msg){
        if (!testing)
            gui.displayChanceCard(msg);
    }

    public static void showMessage(String msg){
        if (!testing)
            gui.showMessage(msg);
    }


    /////////////////////////// UTILITY SECTION /////////////////////////////

    //Used for getting player index since thats the only way we can find the corresponding GUI_Player
    private static int getGuiPlayerIndex(String playerName) {
        for (int i = 0; i < guiPlayers.length; i++) {
            if (guiPlayers[i].getName().equals(playerName)) {
                return i;
            }
        }
        return -1;
    }

    private static GUI_Street posToStreet(int position){
        GUI_Field field = gui.getFields()[position];
        if (field instanceof GUI_Street){
            return (GUI_Street) field;
        }else{
            System.out.println(Language.get("notAStreetAt" + position));
            return null;
        }
    }

    private static Color convertColor(String colorStr){
        Color result = Color.white;

        switch (colorStr.toLowerCase()) {
            case "red" :
                result = Color.red;
                break;
            case "green" :
                result = Color.green;
                break;
            case "blue" :
                result = Color.cyan;
                break;
            case "yellow" :
                result = Color.yellow;
                break;
            case "purple" :
                result = Color.magenta;
                break;
            case "orange" :
                result = Color.orange;
                break;
            case "grey" :
                result = Color.gray; // Make grey
                break;
            case "white" :
                result = Color.white;
                break;
            default:
                result = Color.pink;
                break;
        }
        return result;
    }

    public static void setTesting(boolean condition){
        testing = condition;
    }
}
