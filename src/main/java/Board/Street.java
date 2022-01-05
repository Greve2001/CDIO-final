package Board;

public class Street extends Square{
    private final String COLOR;
    private final int[] PRICE;
    private Player owner = null;

    public Street(){
        super(NAME, POSITION);

    }
}
