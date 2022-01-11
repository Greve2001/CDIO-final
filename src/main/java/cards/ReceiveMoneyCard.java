package cards;

public class ReceiveMoneyCard extends ChanceCard{

    protected int amount;

    public ReceiveMoneyCard(String description, int amount){
        super(description);
        this.amount = amount;

    }

    @Override
    public int updateBalancePositive() {
        return amount;
    }
}
