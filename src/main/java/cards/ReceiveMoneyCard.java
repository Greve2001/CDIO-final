package cards;

public class ReceiveMoneyCard extends ChanceCard{

    protected int amount;

    public ReceiveMoneyCard(int amount){
        this.amount = amount;

    }

    @Override
    public int updateBalancePositive() {
        return amount;
    }
}
