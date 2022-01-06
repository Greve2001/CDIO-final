package Board;

public class IncomeTax extends Square{
    private final int AMOUNT;
    private final int percentage;

    public IncomeTax(String name, int position, int price, int percentPrice){
        super(name, position, false, "incomeTax");
        this.AMOUNT = price;
        this.percentage = percentPrice;
    }
}
