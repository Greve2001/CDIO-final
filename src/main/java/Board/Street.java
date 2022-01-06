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
        super(name, position, true, "Street");
        this.COLOR = color;
        this.PRICE = price;
        this.HOUSEPRICE = housePrice;
        this.RENT = rent;

    }

    public String getColor(){
        return COLOR;
    }

    public Player getOwner(){
        return owner;
    }

    public void setOwner(Player p){
        owner = p;
    }
}
