package Board;

public class Start extends Square {

    private final int AMOUNT;

    public Start(String name, int position, int amount) {
        super(name, position);
        this.AMOUNT = amount;
    }

    public int getAmount(){
        return AMOUNT;
    }
}
