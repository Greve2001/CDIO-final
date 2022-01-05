package Board;

public class Street extends Square{
    private final String COLOR;
    private final int PRICE;
    private final int[] RENT;

    private Player owner = null;
    private boolean pledge = false;

    public Street(String name, int position, int price, int[] rent){
        super(name, position);
        this.PRICE = price;
        this.RENT = rent;
    }
}
