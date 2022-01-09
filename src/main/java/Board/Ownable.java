package Board;

import Logic.Player;

public abstract class Ownable extends Square{
    private final String COLOR;
    private final int[]  RENT;
    private final int PRICE;

    private Player owner = null;
    private boolean pledge = false;

    public Ownable(String name, int position, String color, int[] rent, int price){
        super(name, position);
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
        return 0;
    }

    public int getCurrentCost(){
        int result;
        if (owner != null)
            result = RENT[getAmountOfHouses()];
        else
            result = PRICE;
        return result;
    }

    public boolean getOwnable(){
        return true;
    }

    public boolean isBuildAble(){
        return false;
    }
}
