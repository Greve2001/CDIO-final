package Board;

import Logic.Player;

public class Street extends Square{
    private final String COLOR;
    private final int PRICE;
    private final int[] RENT;
    private final int HOUSEPRICE;

    private Player owner = null;
    private boolean pledge = false;

    public Street(String name, int position, String color, int price, int housePrice,int[] rent){
        super(name, position);
        this.COLOR = color;
        this.PRICE = price;
        this.HOUSEPRICE = housePrice;
        this.RENT = rent;

    }

    public String getCOLOR(){
        return COLOR;
    }

    public Player getOwner(){
        return owner;
    }
}
