package Board;

import java.awt.*;

public class ShippingCompany extends Square{
    private Color color;
    private final int[]  RENT;
    private final int PRICE;
    private boolean pledge = false;
    private Player owner = null;

    public ShippingCompany(String name, int position, int[] rent, int price)
    {
        super(name, position);
        this.RENT = rent;
        this.PRICE = price;
    }
}
