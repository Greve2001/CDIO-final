package Board;

import Interface.GUIController;
import Logic.ActionHandler;
import Logic.Player;
import Utilities.CSVReader;

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

    public void givePlayerToActionHandller(Player[] players){
        actionHandler.setPlayers(players);
    }

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

    public boolean hasMonopoly(int position, Player... player) {
        String color = ALL_SQUARES[position].getColor();
        Player owner = ALL_SQUARES[position].getOwner();
        boolean result = false;

        if (owner != null && !ALL_SQUARES[position].isBuildAble()) {
            result = true;
            if (player != null) //handle out of bounce
                owner = player[0];

            for (Square square : ALL_SQUARES) {
                if (square.getColor().equals(color) && !square.getOwner().equals(owner))
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
        GUIMove(player, startPos, diceValue);

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
        GUIMove(player, startPos, calculateSpaceToMove(startPos, endPos));

        actionHandler.squareAction(player, ALL_SQUARES[player.getPosition()], 0);
    }

    private int calculateSpaceToMove(int startPosition, int endPosition) {
        if (startPosition < endPosition) {
            return endPosition - startPosition;
        } else {
            return (endPosition + ALL_SQUARES.length) - startPosition;
        }
    }

    private void GUIMove(Player player, int startPos, int spaceToMove) {
        try { // Only to handle errors made from the GUI
            GUIController.movePlayer(player, startPos, spaceToMove);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void payStartBonus(Player currentPlayer) {
        actionHandler.boardPaymentsToBank(currentPlayer, -4000);
    }

    public Square[] getALL_SQUARES() {
        return ALL_SQUARES;
    } //used by the GUI

    public void setPlayerInJail(Player player) {
        setPlayerPosition(player, jailPosition, true);
    }

    public int getCurrentCost(int position) {
        int result;
        if (ALL_SQUARES[position].isBuildAble()) {
            result = ALL_SQUARES[position].getCurrentCost();
            if (ALL_SQUARES[position].getAmountOfHouses() == 0 && hasMonopoly(position))
                result = result * 2;
        } else{
            result = getCurrentCost(amountOwnedWithinTheColor(position));
        }
        return result;
    }

    public int amountOwnedWithinTheColor(int position) {
        int result = 0;
        if (ALL_SQUARES[position].getOwner() != null) {
            Player player = ALL_SQUARES[position].getOwner();
            String color = ALL_SQUARES[position].getColor();
            for (Square field : ALL_SQUARES) {
                if (field.getColor().equals(color) && player.equals(field.getOwner()))
                    result++;
            }
        }
            return result;
    }

    public void escapeJail(Player player, int dieRoll, boolean forcedToMove, boolean haveToPay, boolean usedChanceCard) {
        player.setInJail(false);
        if (forcedToMove)
            updatePlayerPosition(player, dieRoll);
        else if (haveToPay)
            actionHandler.boardPaymentsToBank(player, 1000);
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

    public void buyHouse(Player player, String color){
        int position = getFirstPropertyInAColor(color);

        if (hasMonopoly(position, player)){
            //TODO actionhandler sektion
        }


    }

    public int getFirstPropertyInAColor(String color){
        for (Square field: ALL_SQUARES){
            if (field.getColor().equals(color))
                return field.getPOSITION();
        }
        return 0;
    }
}
