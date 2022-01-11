package cards;

public class PayMoneyCard extends ChanceCard{

    protected int amount;

    public PayMoneyCard(String description, int amount){
        super(description);
        this.amount = amount;
    }

    @Override
    public int updateBalanceNegative(){
        return amount;
    }
}
