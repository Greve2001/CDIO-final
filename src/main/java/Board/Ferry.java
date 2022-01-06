package Board;
import java.awt.*;
import Logic.Player;

public class Ferry extends Square{
    private final String COLOR;
    private final int[]  RENT;
    private final int PRICE;

    private boolean pledge = false;
    private Player owner = null;

    public Ferry(String name, int position, int[] rent, int price, String color) {
        super(name, position, true, "ferry");
        this.COLOR = color;
        this.RENT = rent;
        this.PRICE = price;
    }

    public String getColor() {
        return COLOR;
    }

    public int[] getRent() {
        return RENT;
    }

    public int getPrice(){
        return PRICE;
    }

    public boolean getPledge(){
        return this.pledge;
    }

    public void setPledge(boolean condition){
        this.pledge = condition;
    }

    public Player getOwner(){
        return owner;
    }

    public void setOwner(Player player){
        this.owner = player;
    }
}
