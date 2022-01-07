package Board;

import Logic.Player;

public class Street extends Ownable {
    private final int HOUSEPRICE;

    public Street(String name, int position, String type, String color, int[] rent, int price, int housePrice) {
        super(name, position, type, color, rent, price);
        this.HOUSEPRICE = housePrice;
    }

    public int getHousePrice() {
        return HOUSEPRICE;
    }
}