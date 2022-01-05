package Board;
import java.awt.*;
import Logic.Player;

public class Brewery extends Square {
    private final String COLOR;
    private final int[]  RENT;
    private final int PRICE;
    private boolean pledge = false;
    private Player owner = null;

    public Brewery (String name, int position, int[] rent, int price, String color) {
        super(name, position);
        this.COLOR = color;
        this.RENT = rent;
        this.PRICE = price;
    }
}
