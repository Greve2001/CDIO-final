package Interface;

import Board.*;
import Logic.*;
import Utilities.*;
import gui_fields.*;
import gui_main.GUI;

import java.awt.*;

public class GUIController {

    private static GUI gui;
    private static Color[] colorsToChooseFrom = new Color[]{Color.blue, Color.green, Color.yellow, Color.red, Color.magenta, Color.orange};
    private static String[] playerNames;
    private static GUI_Player[] guiPlayers;
    private static int[] playerPositions;

    
    private static int moveTime = 20;

    public GUIController(Square[] inputSquares){
        GUI_Field[] GUIFields = createBoard(inputSquares);
        gui = new GUI(GUIFields);
    }

    public GUI_Field[] createBoard(Square[] inputSquares){
        GUI_Field[] GUIFields = new GUI_Field[inputSquares.length]; // Create empty array

        for (int i = 0; i < GUIFields.length; i++) {
            switch (inputSquares[i].getType()){
                case "street" :
                    //GUIFields[i] = new GUI_Street();
                    GUIFields[i] = new GUI_Street();
                    GUIFields[i].setBackGroundColor(convertColor(inputSquares[i].getColor()));
                    GUIFields[i].setDescription(
                            "Skal indholde renten"
                    );
                    break;

                case "ferry" :
                    GUIFields[i] = new GUI_Shipping();
                    GUIFields[i].setBackGroundColor(Color.white);
                    break;

                case "brewery" :
                    GUIFields[i] = new GUI_Brewery();
                    GUIFields[i].setBackGroundColor(Color.pink);
                    break;

                case "chance" :
                    GUIFields[i] = new GUI_Chance();
                    GUIFields[i].setBackGroundColor(Color.pink);
                    break;

                case "prison" :
                    GUIFields[i] = new GUI_Jail();
                    break;

                case "goToPrison" :
                    GUIFields[i] = new GUI_Street(); // Hopefully change
                    break;

                case "incomeTax", "tax" :
                    GUIFields[i] = new GUI_Tax();
                    GUIFields[i].setBackGroundColor(Color.red);
                    break;

                case "refugee" : // Parkering
                    GUIFields[i] = new GUI_Refuge();
                    GUIFields[i].setBackGroundColor(Color.white);
                    break;

                case "start" : // Parkering
                    GUIFields[i] = new GUI_Start();
                    GUIFields[i].setBackGroundColor(Color.red);
                    break;

                default:
                    GUIFields[i] = new GUI_Street(); // Change
                    break;
            }

            GUIFields[i].setTitle(inputSquares[i].getNAME());
            //TODO not setting subtext cause we need some dynamic handling to show what the price and then rent is.


            // Not using setDescription because it's not giving from csv files.
        }

        return GUIFields;
    }

    public void createPlayers(int startBalance){
        int numberOfPlayers = Integer.parseInt(gui.getUserSelection("Select number of players", "3", "4", "5", "6")); //TODO Use CSVReader

        playerNames = new String[numberOfPlayers]; // Empty name array
        guiPlayers = new GUI_Player[numberOfPlayers];
        playerPositions = new int[numberOfPlayers];

        for (int i = 0; i < numberOfPlayers; i++) {
            // Get players name and car color
            playerNames[i] = gui.getUserString("Enter name"); //TODO Use CSV Reader

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
    public void movePlayer(Player player, int destination) throws InterruptedException { // Make sure it needs the Player or name of Player
        // Calculate distance to move
        int playerStartPos = getPlayerPosition(player);
        int deltaSquares = destination - playerStartPos;
        int time = moveTime * deltaSquares; // Number of frames

        for (int i = 1; i <= deltaSquares; i++) {
            // Get informations
            GUI_Field toField = gui.getFields()[i];
            int guiPlayerIndex = getGuiPlayerIndex(player);

            // Set the car
            guiPlayers[guiPlayerIndex].getCar().setPosition(toField);

            // Sleep, so the car stops breifly for the player to see movement
            Thread.sleep(time);

        }
    }

    public void setPlayerBalance(Player player, int value){
        int index = getGuiPlayerIndex(player);
        guiPlayers[index].setBalance(value);
    }

    // Gives access to logic to get the inputted names from the GUI.
    public String[] getPlayerNames(){
        return playerNames;
    }
    //TODO implement solution to let player select colors
    public Color[] getPlayerColors(){
        return colorsToChooseFrom;
    }


/// Action Section ///
    // Show rolled dice
    public void showDice(int[] faceValues){
        gui.setDice(faceValues[0], faceValues[1]);
    }

    // Stops game-flow until button is pressed. Used for UX.
    public void getPlayerAction(Player player, String msg){
        gui.showMessage(player.getName() + "; " + msg);
    }

    //TODO when Pi has it made

    // Used for more than displayChanceCard. Also just for normal messages.
    //public void displayChanceCard(ChanceCard card) { // Use CSV Reader
    //    gui.displayChanceCard("ChanceCard" + "\n" + card.toString());
    //}

    public void setHouses(int position, int amount){
        if (posToStreet(position) != null)
            posToStreet(position).setHouses(amount);
    }

    public void setHotel(int position, boolean bool){
        if (posToStreet(position) != null)
            posToStreet(position).setHotel(bool);

    }

    public void setOwner(Player player, int position){
        if (posToStreet(position) != null){
            if (player != null){
                posToStreet(position).setOwnerName(player.getName());
                ((GUI_Ownable) posToStreet(position)).setBorder(player.getColor());
            }else{
                posToStreet(position).setOwnerName(null);
                ((GUI_Ownable) posToStreet(position)).setBorder(null);
                // FOR READERS:
                // The GUI does not have functionally to remove a already existing border. So when setting it to null i will be black.
            }

        }else{
            System.out.println("Not a street. Cant own it");
        }
    }

    private GUI_Street posToStreet(int position){
        GUI_Field field = gui.getFields()[position];
        if (field instanceof GUI_Street){
            return (GUI_Street) field;
        }else{
            System.out.println("Error, this square at position: " + position + " is not a street: ");
            return null;
        }
    }


    /////////////////////////// UTILITY SECTION /////////////////////////////

    //Used for getting player index since thats the only way we can find the corresponding GUI_Player
    private int getGuiPlayerIndex(Player player) {
        for (int i = 0; i < guiPlayers.length; i++) {
            if (guiPlayers[i].getName().equals(player.getName())) {
                return i;
            }
        }
        return -1;
    }

    // Used for finding the players position, since the GUI does not hold the position as a numerical value.
    private int getPlayerPosition(Player player){
        for (int i = 0; i < gui.getFields().length; i++) {
            GUI_Player guiPlayer = guiPlayers[getGuiPlayerIndex(player)];

            if (gui.getFields()[i].equals(guiPlayer.getCar().getPosition())){
                return i;
            }
        }
        return -1;
    }

    private Color convertColor(String colorStr){
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
}
