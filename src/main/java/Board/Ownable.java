package Board;

import Logic.Player;

public class Ownable extends Square{
    private final String COLOR;
    private final int[]  RENT;
    private final int PRICE;

    private Player owner = null;
    private boolean pledge = false;

    private int amountOfHouses = 0;

    public Ownable(String name, int position, String type, String color, int[] rent, int price){
        super(name, position, true, type);
        this.COLOR = color;
        this.RENT = rent;
        this.PRICE = price;
    }

    public String getColor() {
        return COLOR;
    }

    public int[] getRent(){
        return RENT;
    }

    public int getPrice(){
        return PRICE;
    }

    public boolean getPledge(){
        return pledge;
    }

    public void setPledge(boolean condition){
        this.pledge = condition;
    }

    public Player getOwner(){
        return owner;
    }

    public void setOwner (Player player){
        this.owner = player;
    }

    public int getAmountOfHouses(){
        return amountOfHouses;
    }
}
