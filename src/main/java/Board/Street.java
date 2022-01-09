package Board;

import Logic.Player;

public class Street extends Ownable {
    private final int HOUSEPRICE;
    private int amountOfHouses = 0;

    public Street(String name, int position, String type, String color, int[] rent, int price, int housePrice) {
        super(name, position, color, rent, price);
        this.HOUSEPRICE = housePrice;
    }

    public int getHousePrice() {
        return this.HOUSEPRICE;
    }

    public void setAmountOfHouses(int amount){
        this.amountOfHouses = amount;
    }

    public int getAmountOfHouses(){
        return this.amountOfHouses;
    }

    public boolean isBuildAble(){
        return true;
    }
}