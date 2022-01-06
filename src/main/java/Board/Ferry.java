package Board;
import java.awt.*;
import Logic.Player;

public class Ferry extends Square{
    private final String color;
    private final int[]  RENT;
    private final int PRICE;
    private boolean pledge = false;
    private Player owner = null;


    public Ferry(String name, int position, int[] rent, int price, String color) {
        super(name, position);
        this.color = color;
        this.RENT = rent;
        this.PRICE = price;
    }
}