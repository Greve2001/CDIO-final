package Board;

import Interface.GUIController;
import Logic.ActionHandler;
import Logic.Player;
import Utilities.CSVReader;
import Utilities.Language;

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

    //CSV only contain Strings, this allow us to change those to array
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

    public void updatePlayerPosition(Player player, int diceValue) {
        int startPos = player.getPosition();
        int sum = startPos + diceValue; //calculate the normerical end position
        int endPosition;
        int boardSize = ALL_SQUARES.length;

        if (diceValue == Math.abs(diceValue) && sum >= boardSize) {
            endPosition = sum - boardSize;
            payStartBonus(player);
        } else if (sum < 0)
            endPosition = sum + boardSize;
        else
            endPosition = sum;

        //GUI update
        player.setPosition(endPosition);
        GUIController.movePlayer(player, startPos, diceValue);

        //send relevant information to the actionhandler to execute field action.
        actionHandler.squareAction(player, ALL_SQUARES[player.getPosition()], diceValue);
    }

    public void setPlayerPosition(Player player, int endPos, boolean goingToJail) {
        if (endPos < player.getPosition() && !goingToJail)
            payStartBonus(player);
        else
            player.setInJail(true);

        int startPos = player.getPosition();
        player.setPosition(endPos);
        GUIController.movePlayer(player, startPos, calculateSpaceToMove(startPos, endPos));

        actionHandler.squareAction(player, ALL_SQUARES[player.getPosition()], 0);
    }

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

    public Square[] getALL_SQUARES() {
        return ALL_SQUARES;
    } //used by the GUI

    public void setPlayerInJail(Player player) {
        GUIController.showMessage(Language.get("goToPrison"));
        setPlayerPosition(player, jailPosition, true);
        player.setHasExtraTurn(false);
    }

    public int getCurrentCost(int position) {
        int result = 0;
        if (ALL_SQUARES[position].getOwnable()) {
            if (ALL_SQUARES[position].isBuildAble()) {
                result = ALL_SQUARES[position].getCurrentCost();
                if (ALL_SQUARES[position].getAmountOfHouses() == 0 && hasMonopoly(position))
                    result = result * 2;
            } else {
                if (ALL_SQUARES[position].getOwner() != null)
                    result = ALL_SQUARES[position].getRent()[amountOwnedWithinTheColor(position) - 1];
                else
                    result = ALL_SQUARES[position].getPrice();
            }
        }
        return result;
    }

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

    public void escapeJail(Player player, int dieRoll, boolean forcedToMove, boolean haveToPay, boolean usedChanceCard) {
        player.setInJail(false);
        if (haveToPay)
            actionHandler.boardPaymentsToBank(player, 1000);
        if (forcedToMove)
            updatePlayerPosition(player, dieRoll);
        else if (usedChanceCard) ;
        //TODO return card logic
    }

    public int playerTotalValue(Player player){
        int result = player.getBalance();
        for (Square field: ALL_SQUARES){
            if (player.equals(field.getOwner())){
                if (!field.getPledge())
                    result += field.getPrice();
                else
                    result += field.getPrice()/2;
                if (field.isBuildAble()){
                    result += (field.getAmountOfHouses() * field.getHousePrice());
                }
            }
        }
        return result;
    }

    public String[] getAllStreetColors(){
        int count = 0;
        String color = "";
        for (Square field: ALL_SQUARES){
            if (field.isBuildAble() && !color.equals(field.getColor())){
                color = field.getColor();
                count++;
            }
        }
        String[] result = new String[count];

        for (Square field: ALL_SQUARES) {
            if (field.isBuildAble()) {
                for (int i = 0; i < result.length; i++) {
                    if (field.getColor().equals(result[i])) {
                        result[i + 1] = field.getColor();
                    }
                }
            }
        }
        return result;
    }

    public int getHousePrice(String color){
        for (Square field: ALL_SQUARES){
            if (color.equals(field.getColor()))
                return field.getHousePrice();
        }
        return 0;
    }

    public void buyHouse(Player player, String color, int amountOfHouses){
        int position = getFirstPropertyInAColor(color);
        String whereToPlaceHouse;

        if (hasMonopoly(position, player)){
            int price = ALL_SQUARES[position].getHousePrice();
            if (actionHandler.buyHouse(player, price, amountOfHouses)){
                String[] choice = new String[amountOwnedWithinTheColor(position)];
                for (int i = 0, j = 0; i < ALL_SQUARES.length; i++){
                    if (color.equals(ALL_SQUARES[i].getColor())){
                        choice[j] = ALL_SQUARES[i].getName();
                        j++;
                    }
                }
                do {
                    whereToPlaceHouse = GUIController.givePlayerChoice("Place a house", choice);
                    for (Square field: ALL_SQUARES){
                        if(whereToPlaceHouse.equals(field.getName())) {
                            field.setAmountOfHouses(field.getAmountOfHouses() + 1);
                            amountOfHouses--;
                        }
                    }
                }while(amountOfHouses > 0);
            }
        }
        else
            GUIController.showMessage("You do not own all the properties in the color");
    }

    private int getFirstPropertyInAColor(String color){
        for (Square field: ALL_SQUARES){
            if (field.getColor().equals(color))
                return field.getPOSITION();
        }
        return 0;
    }
}
