package Board;

public class Tax extends Square{
    private final int AMOUNT;

    public Tax(String name, int position, int amount){
        super(name, position);
        this.AMOUNT = amount;
    }
}
