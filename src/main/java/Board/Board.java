package Board;

import Logic.ActionHandler;
import Logic.Player;
import Utilities.CSVReader;
import org.jetbrains.annotations.Debug;

public class Board {
    private final Square[] ALL_SQUARES;
    private final ActionHandler actionHandler;


    public Board(){
        actionHandler = new ActionHandler();
        CSVReader reader;
        try {
            reader = new CSVReader(System.getProperty("user.language") + "_board.csv", ",", true);
        } catch (Exception e) {
            reader = new CSVReader("da_board.csv", ",", true);
        }
        ALL_SQUARES = new Square[reader.getFile().length];

        int name = 0, position = 1, type = 2, color = 3, price = 4, percentagePrice = 5, housePrice = 6;

        for(String[] data: reader.getFile()){
            switch (data[type]){
                case "street":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Street(
                            data[name],
                            Integer.parseInt(data[position]),
                            data[color],
                            Integer.parseInt(data[price]),
                            Integer.parseInt(data[housePrice]),
                            stringArrayToIntArray(data, 6)
                    );
                    break;
                case "ferry":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Ferry(
                            data[name],
                            Integer.parseInt(data[position]),
                            stringArrayToIntArray(data, 4),
                            Integer.parseInt(data[price]),
                            data[color]
                    );
                    break;
                case "brewery":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Brewery(
                            data[name],
                            Integer.parseInt(data[position]),
                            stringArrayToIntArray(data, 2),
                            Integer.parseInt(data[price]),
                            data[color]
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
                default:
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Square(
                            data[name],
                            Integer.parseInt(data[position]),
                            false
                    );
                    break;
            }
        }
        reader.close();
    }

    private int[] stringArrayToIntArray(String[] arr, int size) {
        int offset = 7;
        int[] result = new int[size];
        for (int i = 0; i < result.length; i++){
            result[i] = Integer.parseInt(arr[offset + i]);
        }
        return result;
    }

    public boolean hasMonopoly(int position, Player... player) {
        String color = ALL_SQUARES[position].getColor();
        Player owner = ALL_SQUARES[position].getOwner();
        boolean result = false;

        if (owner != null && !ALL_SQUARES[position].getOwnable()) {
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

    public void updatePlayerPosition(Player player, int diceValue){
        int currentPosition = player.getPosition();
        int sum = currentPosition + diceValue;
        int endPosition;
        int boardSize = ALL_SQUARES.length;

        if (diceValue == Math.abs(diceValue) && sum >= boardSize){
            endPosition = sum - boardSize;
            payStartBonus(player, false);
            }
        else if (sum < 0)
            endPosition = sum + boardSize;
        else
            endPosition = sum;

        player.setPosition(endPosition);
    }
    
    public void setPlayerPosition(Player player, int endPos){
        player.setPosition(endPos);
    }

    public void payStartBonus(Player currentPlayer, boolean goingToJail) {
        if (!goingToJail)
            actionHandler.bank().payToBank(currentPlayer, 4000);
    }
    public Square[] getALL_SQUARES() {
        return ALL_SQUARES;
    }

}
