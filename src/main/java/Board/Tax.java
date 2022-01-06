package Board;

public class Tax extends Square{
    private final int AMOUNT;

    public Tax(String name, int position, int amount){
        super(name, position, false, "tax");
        this.AMOUNT = amount;
    }

    public int getAmount(){
        return AMOUNT;
    }
}
