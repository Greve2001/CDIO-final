package Board;

import Interface.GUIController;
import Logic.ActionHandler;
import Logic.Player;
import Utilities.CSVReader;
import Utilities.Language;
import gui_main.GUI;

public class Board {
    private final Square[] ALL_SQUARES;
    private final ActionHandler actionHandler;
    private int jailPosition;

    public Board() {
        actionHandler = new ActionHandler(this);

        int passStartAmount = 4000;

        CSVReader reader;
        try {
            reader = new CSVReader(System.getProperty("user.language") + "_board.csv", ",", true);
        } catch (Exception e) {
            reader = new CSVReader("da_board.csv", ",", true);
        }
        ALL_SQUARES = new Square[reader.getFile().length];

        int name = 0, position = 1, type = 2, color = 3, price = 4, percentagePrice = 5, housePrice = 6;

        //Runs through the CSV file, for each row and created a Square with a specific supclass depending on the data found
        for (String[] data : reader.getFile()) {
            switch (data[type]) {
                case "street":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Street(
                            data[name],
                            Integer.parseInt(data[position]),
                            data[type],
                            data[color],
                            stringArrayToIntArray(data, data[type]),
                            Integer.parseInt(data[price]),
                            Integer.parseInt(data[housePrice])
                    );
                    break;
                case "ferry":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Ferry(
                            data[name],
                            Integer.parseInt(data[position]),
                            data[type],
                            data[color],
                            stringArrayToIntArray(data, data[type]),
                            Integer.parseInt(data[price])
                    );
                    break;
                case "brewery":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Brewery(
                            data[name],
                            Integer.parseInt(data[position]),
                            data[type],
                            data[color],
                            stringArrayToIntArray(data, data[type]),
                            Integer.parseInt(data[price])
                    );
                    break;
                case "tax":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Tax(
                            data[name],
                            Integer.parseInt(data[position]),
                            Integer.parseInt(data[price])
                    );
                    break;
                case "incomeTax":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new IncomeTax(
                            data[name],
                            Integer.parseInt(data[position]),
                            Integer.parseInt(data[price]),
                            Integer.parseInt(data[percentagePrice])
                    );
                    break;
                case "start":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Start(
                            data[name],
                            Integer.parseInt(data[position]),
                            passStartAmount
                    );
                    break;
                case "chance":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Chance(
                            data[name],
                            Integer.parseInt(data[position])
                    );
                    break;
                case "prison":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Prison(
                            data[name],
                            Integer.parseInt(data[position])
                    );
                    jailPosition = Integer.parseInt(data[position]);
                    break;
                case "goToPrison":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new GoToPrison(
                            data[name],
                            Integer.parseInt(data[position])
                    );
                    break;
                case "refugee":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Refugee(
                            data[name],
                            Integer.parseInt(data[position])
                    );
                    break;
                default:
                    //TODO error handling
                    break;
            }
        }
        reader.close();
    }

    public void givePlayerToActionHandler(Player[] players){
        actionHandler.setPlayers(players);
    }

    //CSV only contain Strings, this allow us to change those to an int array for the rent
    private int[] stringArrayToIntArray(String[] arr, String type) {
        int offset = 7;
        int[] result;

        //set the size of the array based on with type
        if (type.equals("street"))
            result = new int[6];
        else if (type.equals("ferry"))
            result = new int[4];
        else
            result = new int[2];

        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(arr[offset + i]);
        }
        return result;
    }

    //checks whether or not all
    private boolean hasMonopoly(int position, Player... player) {
        String color = ALL_SQUARES[position].getColor();
        Player owner = ALL_SQUARES[position].getOwner();
        boolean result = false;

        if (owner != null && ALL_SQUARES[position].isBuildAble()) {
            result = true;
            if (player.length != 0) //handle out of bounce
                owner = player[0];

            for (Square square : ALL_SQUARES) {
                if (color.equals(square.getColor()) && !owner.equals(square.getOwner()))
                    result = false;
            }
        }
        return result;
    }

    /**
     * This method updates the player position
     * @param player get the current player
     * @param diceValue get the sum of the dice values
     */
    public void updatePlayerPosition(Player player, int diceValue) {
        int startPos = player.getPosition();
        int sum = startPos + diceValue; //calculate the normerical end position
        int endPosition;
        int boardSize = ALL_SQUARES.length;

        if (diceValue == Math.abs(diceValue) && sum >= boardSize) { //ensure that the number are positive
            endPosition = sum - boardSize;
            payStartBonus(player);
        } else if (sum < 0) //handle backward movement that comes from chancecard
            endPosition = sum + boardSize;
        else
            endPosition = sum;

        //GUI update
        player.setPosition(endPosition);
        GUIController.movePlayer(player.getName(), startPos, diceValue);

        //send relevant information to the actionhandler to execute field action.
        actionHandler.squareAction(player, ALL_SQUARES[player.getPosition()], diceValue);
    }

    /**
     * Set the players position, and maybe put them in jail
     * @param player the player that we need to move
     * @param endPos where the player is suppost to go on the board.
     * @param goingToJail whether or not the player is going inside the jail (will be false if they are going to visit)
     */
    public void setPlayerPosition(Player player, int endPos, boolean goingToJail) {
        if (endPos < player.getPosition() && !goingToJail) //if player passes start, and are not going to jaul
            payStartBonus(player);
        else
            player.setInJail(true); //if player pass start and ain't suppost to recieve money, they must be going to jail.

        int startPos = player.getPosition();
        player.setPosition(endPos); //updates the player position

        //GUI update
        GUIController.movePlayer(player.getName(), startPos, calculateSpaceToMove(startPos, endPos));

        //if the player ain't going to jail, they must complete the action of the square they land on.
        if(!goingToJail)
            actionHandler.squareAction(player, ALL_SQUARES[player.getPosition()], 0);
    }

    /**
     * this method is used by the GUI, as the GUI need to know how many spaces the player need to move.
     * @param startPosition where the player start
     * @param endPosition where the player end
     * @return the amount of Squares the player moved.
     */
    private int calculateSpaceToMove(int startPosition, int endPosition) {
        if (startPosition < endPosition) {
            return endPosition - startPosition;
        } else {
            return (endPosition + ALL_SQUARES.length) - startPosition;
        }
    }

    private void payStartBonus(Player currentPlayer) {
        actionHandler.boardPaymentsToBank(currentPlayer, -4000);
    }

    /**
     * used by the GUI to display the Board.
     * @return all squares
     */
    public Square[] getALL_SQUARES() {
        return ALL_SQUARES;
    }

    /**
     * this ensure that all attributes related to a player getting jailed is set.
     * all guards to ensure the player have to go to jail, is made prior to this method call
     * @param player the player that need to go to jail
     */
    public void setPlayerInJail(Player player) {
        GUIController.showMessage(Language.get("goToPrison"));
        setPlayerPosition(player, jailPosition, true);
        player.setHasExtraTurn(false);
    }

    /**
     * this method is used to simplify the amount of data the different controller classes need.
     * It gives what ever a play can / must pay if they land on a square, depending on the ownership and propperty on the square.
     * @param position the square that we need to look through.
     * @return the price of landing on the square
     */
    public int getCurrentCost(int position) {
        int result = 0;
        if (ALL_SQUARES[position].getOwnable()) { //if it's a child of Ownable, else the rest would go out of bounce.
            if (ALL_SQUARES[position].isBuildAble()) { //if it's of the type Street
                result = ALL_SQUARES[position].getCurrentCost();
                if (ALL_SQUARES[position].getAmountOfHouses() == 0 && hasMonopoly(position)) //if there is no hauses, and the play owns all streets within the color.
                    result = result * 2;
            } else { //not a street
                if (ALL_SQUARES[position].getOwner() != null) // if there is an owner
                    result = ALL_SQUARES[position].getRent()[amountOwnedWithinTheColor(position) - 1];
                else
                    result = ALL_SQUARES[position].getPrice();
            }
        }
        return result;
    }

    /**
     * This is used for Ferry and Brewery, as there Rent is based on how many the player own.
     * @param position used to find the color and the owner that we want to count for
     * @return total amount of Streets found with the same color and the same owner
     */
    public int amountOwnedWithinTheColor(int position) {
        int result = 0;
        if (ALL_SQUARES[position].getOwner() != null) {
            Player player = ALL_SQUARES[position].getOwner();
            String color = ALL_SQUARES[position].getColor();
            for (Square field : ALL_SQUARES) {
                if (color.equals(field.getColor()) && player.equals(field.getOwner()))
                    result++;
            }
        }
            return result;
    }

    /**
     * based on the method the player use to escape jail, different thing will happend.
     * @param player trying to escape
     * @param dieRoll is used when the player try to escape and either manage or are force to pay.
     * @param forcedToMove if the player must move if they get out of jail
     * @param haveToPay if the player decides or are forced to pay to get out
     * @param usedChanceCard if they used the get out of jail chnace card.
     */
    public void escapeJail(Player player, int dieRoll, boolean forcedToMove, boolean haveToPay, boolean usedChanceCard) {
        player.setInJail(false);
        if (haveToPay)
            actionHandler.boardPaymentsToBank(player, 1000);
        if (forcedToMove)
            updatePlayerPosition(player, dieRoll);
        else if (usedChanceCard) ;
        //TODO return card logic
    }

    /**
     * the IncomeTax need to know the players total value of everything owned.
     * @param player is the one we want to check ownership of Square + properties for
     * @return the value of money in hand + Squares owned + properties owned.
     */
    public int playerTotalValue(Player player){
        int result = player.getBalance();
        for (Square field: ALL_SQUARES){ //runs through all Squares
            if (player.equals(field.getOwner())){ //if the player own the Square
                if (!field.getPledge()) //if the square is pledged
                    result += field.getPrice();
                else
                    result += field.getPrice()/2;
                if (field.isBuildAble()){ //if the Square is a Street
                    result += (field.getAmountOfHouses() * field.getHousePrice());
                }
            }
        }
        return result;
    }

    /**
     * this is used by the GameController so that player can take actions based on any of the color of squares.
     * @return all the colors of squares of the type Street.
     */
    public String[] getAllStreetColors(){
        int count = 0;
        String[] arr = new String[100];
        String color = "";
        for (Square field: ALL_SQUARES){ //runs through all Squares
            if (field.isBuildAble() && !color.equals(field.getColor())){ //if it's a street and we haven't already added the color to the array
                color = field.getColor();
                arr[count] = field.getColor();
                count++;
            }
        }
        return reduceStringArraySize(arr, count);
    }

    /**
     * used by gamecontroller to display the price for a house on the selected color.
     * @param color that the square should match
     * @return the price of buying a house or hotel on the given color
     */
    public int getHousePrice(String color){
        for (Square field: ALL_SQUARES){
            if (color.equals(field.getColor())) //if the square color matches the given color.
                return field.getHousePrice();
        }
        return 0;
    }

    /**
     * ensures that all parameters for buying houses is correct, and ensure correct placement
     * @param player that is buying the houses
     * @param color that the player want to build on
     * @param amountOfHouses that the player want
     */
    public void buyHouse(Player player, String color, int amountOfHouses){
        int position = getFirstPropertyInAColor(color);
        String whereToPlaceHouse;

        if (hasMonopoly(position, player)){ //if the player own all the Streets within the color

            //this section prepares all the data we need to test before we can actualy buy the house
            int price = ALL_SQUARES[position].getHousePrice();
            int streetWithinColor = amountOwnedWithinTheColor(position);
            int amountOfHousesBefore = amountOfHousesOnColor(color);

            //this section handle control of setup and the purchase of the houses.
            if (player.getBalance() >= price * amountOfHouses && //if the player have enough money
                actionHandler.getHousesAvailable() >= amountOfHouses && //if the bank has enough houses
                amountOfHousesBefore + amountOfHouses <= streetWithinColor * 4){ //if the total amount of houses doesn't surpass 4 per Street

                actionHandler.buyHouse(player, price, amountOfHouses);

                //this prepare the streets name, so that we can easily pass them to the player.
                String[] choice = getNameOfAllStreetsWithinAColor(color);

                boolean placementOkay;
                int amountOfHousesOnStreet;
                do {
                    //this section handle the GUI and player interaction for where to place the houses.
                    whereToPlaceHouse = GUIController.givePlayerChoice(Language.get("placeHouse"), choice);
                    position = getPositionFromName(whereToPlaceHouse);

                    //this ensure that the houses is evenly distributed on the different streets within the color.
                    amountOfHousesOnStreet = ALL_SQUARES[position].getAmountOfHouses();
                    placementOkay = ensureEvenDistribution(position, true);

                    //this places the houses
                    if (placementOkay) {
                        ALL_SQUARES[position].setAmountOfHouses(amountOfHousesOnStreet + 1);
                        amountOfHouses--;
                        GUIController.setHouses(position, ALL_SQUARES[position].getAmountOfHouses());
                        GUIController.updateRent(position, getCurrentCost(position));
                    }
                }while(amountOfHouses > 0);
            }

            //this gives the correct failure message to the player
            else if(player.getBalance() >= price * amountOfHouses) //if the player have enough money
                GUIController.getPlayerAction(player.getName(),Language.get("insuficientMoney"));
            else if(actionHandler.getHousesAvailable() >= amountOfHouses) //if the bank has enough houses
                GUIController.getPlayerAction(player.getName(), Language.get("bankOutOfHouses"));
            else if(amountOfHousesBefore + amountOfHouses <= streetWithinColor * 4)//if the total amount of houses doesn't surpass 4 per Street
                GUIController.getPlayerAction(player.getName(), Language.get("MaxAmountOfHouses"));
            else
                GUIController.getPlayerAction(player.getName(),Language.get("boardUnknowError"));
        }
        else
            GUIController.getPlayerAction(player.getName(),Language.get("missingMonopoly"));
    }

    private boolean ensureEvenDistribution(int position, boolean buy) {
        int amountOfHousesOnStreet = ALL_SQUARES[position].getAmountOfHouses();
        for (Square field: ALL_SQUARES){
            if(ALL_SQUARES[position].getColor().equals(field.getColor())){
                if (!(amountOfHousesOnStreet == field.getAmountOfHouses() ||
                    (amountOfHousesOnStreet == field.getAmountOfHouses()+1 && buy) ||
                    (amountOfHousesOnStreet +1 == field.getAmountOfHouses() && !buy)))
                        return false;
            }
        }
        return true;
    }

    private int getPositionFromName(String name) {
        for (Square field: ALL_SQUARES){
            if(name.equals(field.getName())) {
                return field.getPOSITION();
            }
        }
        return 0;
    }

    public void buyHotel(Player player, String color, int amountOfHotels){
        int position = getFirstPropertyInAColor(color);
        String whereToPlaceHotel;

        if (hasMonopoly(position, player)){ //if the player own all the Streets within the color

            //this section prepares all the data we need to test before we can actualy buy the house
            int price = ALL_SQUARES[position].getHousePrice();
            int streetWithinColor = amountOwnedWithinTheColor(position);
            int amountOfHousesOnAllProperties = amountOfHousesOnColor(color);

            //this section handle control of setup and the purchase of the houses.
            if (player.getBalance() >= price * amountOfHotels && //if the player have enough money
                    actionHandler.getHotelsAvailable() >= amountOfHotels && //if the bank has enough houses
                    amountOfHousesOnAllProperties == streetWithinColor * 4) { //if the total amount of houses is equal to 4 on each square

                actionHandler.buyHotels(player, price, amountOfHotels);

                //this prepare the streets name, so that we can easily pass them to the player.
                String[] choice = getNameOfAllStreetsWithinAColor(color);

                if (amountOfHotels == streetWithinColor) { //if a hotel is bought for each property we can just place it without asking
                    for (Square field : ALL_SQUARES) {
                        if (ALL_SQUARES[position].getColor().equals(field.getColor())){
                            field.setAmountOfHouses(5);
                            //TODO actionhandler
                        }
                    }
                } else {
                    do {
                        //this section handle the GUI and player interaction for where to place the houses.
                        whereToPlaceHotel = GUIController.givePlayerChoice(Language.get("placeHotel"), choice);
                        position = getPositionFromName(whereToPlaceHotel);

                        //this places the Hotels
                        ALL_SQUARES[position].setAmountOfHouses(5);
                        amountOfHotels--;
                        GUIController.setHotel(position, true);

                    } while (amountOfHotels > 0);
                }
            }

            //this gives the correct failure message to the player
            else if(player.getBalance() >= price * amountOfHotels) //if the player have enough money
                GUIController.getPlayerAction(player.getName(),Language.get("insuficientMoney"));
            else if(actionHandler.getHousesAvailable() >= amountOfHotels) //if the bank has enough houses
                GUIController.getPlayerAction(player.getName(), Language.get("bankOutOfHotels"));
            else if(amountOfHousesOnAllProperties != streetWithinColor * 4)//if the total amount of houses isn't exacly 4
                GUIController.getPlayerAction(player.getName(), Language.get("youNeedMoreHouses"));
            else
                GUIController.getPlayerAction(player.getName(),Language.get("boardUnknowError"));
        }
        else
            GUIController.getPlayerAction(player.getName(),Language.get("missingMonopoly"));
    }

    /**
     * since all the methods uses the position of the square, but the player will pick based on color, this allow for an easyer compability between them
     * @param color the player have chosen
     * @return the position of the first square that matches the color.
     */
    private int getFirstPropertyInAColor(String color){
        for (Square field: ALL_SQUARES){
            if (field.getColor().equals(color))
                return field.getPOSITION();
        }
        return 0;
    }

    public void sellProperty(Player player, String color, String type, int amount) {
        int position = getFirstPropertyInAColor(color);
        if (type.equals("hotel")) {
            if (amountOwnedWithinTheColor(position) == amount){
                //TODO actionhandler.sellhotel
                for (Square field: ALL_SQUARES){
                    if (color.equals(field.getColor())){
                        field.setAmountOfHouses(0);
                        GUIController.setHotel(field.getPOSITION(), false);
                    }
                }
            }
        } else {
            if (amount == amountOfHousesOnColor(color)) {
                //TODO actionhandler.sellHouse
                for (Square field: ALL_SQUARES){
                    field.setAmountOfHouses(0);
                    GUIController.setHouses(field.getPOSITION(),0);
                }
            } else {
                //remove 1 house at a time
                String[] choices = getNameOfAllStreetsWithinAColor(color);
                boolean removeOkay;
                do {
                    removeOkay = false;
                    String disicion = GUIController.givePlayerChoice(Language.get("whatToSell"),choices);
                    position = getPositionFromName(disicion);
                    removeOkay = ensureEvenDistribution(position, false);
                    if (removeOkay){
                        ALL_SQUARES[position].setAmountOfHouses(ALL_SQUARES[position].getAmountOfHouses()-1);
                        GUIController.setHouses(position, ALL_SQUARES[position].getAmountOfHouses());
                        amount--;
                    } else
                        GUIController.showMessage(Language.get("ensureEvenDistribution"));

                }while (amount != 0);
            }
        }
    }

    /**
     * used for selling
     * @param player that want to sell
     * @return all the squares that the given player own
     */
    public String[] allSquaresOwnedByPlayer(Player player){
        String[] temp = new String[ALL_SQUARES.length];
        int count = 0;
        for (Square field: ALL_SQUARES){
            if (player.equals(field.getOwner())){
                temp[count] = field.getName();
                count++;
            }
        }
        return reduceStringArraySize(temp,count);
    }

    /**
     * used for selling houses
     * @param player that want to sell
     * @return all the squares that the given player own and have a house on
     */
    public String[] allSquaresWherePlayerHaveHouses(Player player){
        String[] temp = new String[ALL_SQUARES.length];
        int count = 0;
        for (Square field: ALL_SQUARES){
            if (player.equals(field.getOwner())){
                if (field.getAmountOfHouses() > 0){
                    temp[count] = field.getName();
                    count++;
                }
            }
        }
        return reduceStringArraySize(temp, count);
    }

    /**
     * used by gamecontroller to only allow player to select the color that they have monopoly on, for building.
     * @param player that want to build
     * @return the colors that the play have monopoly on.
     */
    public String[] allMonopolyColorsByPlayer(Player player){
        String[] temp = new String[ALL_SQUARES.length];
        int count = 0;
        String[] color = getAllStreetColors();
        int position;
        for (String arr: color){
            position = getFirstPropertyInAColor(arr);
            if (hasMonopoly(position,player)){
                temp[count] = ALL_SQUARES[position].getColor();
                count++;
            }
        }
        return reduceStringArraySize(temp, count);
    }

    /**
     * used multiple places to ensure the arr size is reduces to only the size of wich there is data to fill.
     * this ensures that we don't get outofbounce issues.
     * @param arr the array that is bigger then needed
     * @param size what size we want it reduced down to
     * @return the data from arr that was within the size.
     */
    private String[] reduceStringArraySize(String[] arr, int size){
        String[] result = new String[size];
        for (int i = 0; i < size; i++){
            result [i] = arr[i];
        }
        return result;
    }

    private String[] getNameOfAllStreetsWithinAColor(String color){
        String[] result = new String[ALL_SQUARES.length];
        int count = 0;
        for (Square field: ALL_SQUARES) {
            if (color.equals(field.getColor())) {//if it's the same color
                result[count] = field.getName();
                count++;
            }
        }
        result = reduceStringArraySize(result, count);
        return result;
    }

    public int amountOfHousesOnColor(String color){
        int result = 0;
        for (Square field: ALL_SQUARES){
            if (color.equals(field.getColor()))
                result = field.getAmountOfHouses();
        }
        return result;
    }
}
