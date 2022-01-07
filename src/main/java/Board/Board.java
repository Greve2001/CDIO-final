package Board;

import Logic.ActionHandler;
import Logic.Player;
import Utilities.CSVReader;

public class Board {
    private final Square[] ALL_SQUARES;
    private final ActionHandler actionHandler;
    private int jailPosition;


    //hej
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

        //Runs through the CSV file, for each row and created a Square with a specific supclass depending on the data found
        for(String[] data: reader.getFile()){
            switch (data[type]){
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
                case "brewery":
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Ownable(
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
                default:
                    ALL_SQUARES[Integer.parseInt(data[position])] = new Square(
                            data[name],
                            Integer.parseInt(data[position]),
                            false,
                            data[type]
                    );
                    if(data[type].equals("prison")) {//check if the type is prison, and save it for later uses
                        jailPosition = Integer.parseInt(data[position]);
                    }
                    break;
            }
        }
        reader.close();
    }

    private int[] stringArrayToIntArray(String[] arr, String type) {
        int offset = 7;
        int[] result;

        //set the size of the array based on with type
        if (type.equals("street"))
            result = new int [6];
        else if(type.equals("ferry"))
            result = new int[4];
        else
            result = new int[2];

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
            payStartBonus(player);
            }
        else if (sum < 0)
            endPosition = sum + boardSize;
        else
            endPosition = sum;

        player.setPosition(endPosition);
    }
    
    public void setPlayerPosition(Player player, int endPos, boolean goingToJail){
        if (endPos < player.getPosition() && !goingToJail)
            payStartBonus(player);
        else
            player.setInJail(true);
        player.setPosition(endPos);
    }

    public void payStartBonus(Player currentPlayer) {
        actionHandler.getBank().payPlayer(currentPlayer, 4000);
    }

    public Square[] getALL_SQUARES(){
        return ALL_SQUARES;
    }

    public void setPlayerInJail(Player player){
        setPlayerPosition(player, jailPosition,true);
    }

    public int getCurrentCost(int position){
        int result;
        if (ALL_SQUARES[position].getOwner() != null)
            result = ALL_SQUARES[position].getRent()[ALL_SQUARES[position].getAmountOfHouses()];
        else
            result = ALL_SQUARES[position].getPrice();
        return result;

    }
}
