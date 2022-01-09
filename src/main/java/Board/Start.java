package Board;

public class Start extends Square {

    private int amount;

    public Start(String name, int position, int amount) {
        super(name, position);
        this.amount = amount;
    }

    public int getAmount(){
        return amount;
    }
}
