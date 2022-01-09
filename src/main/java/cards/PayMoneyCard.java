package cards;

public class PayMoneyCard extends ChanceCard{

    protected int amount;

    public PayMoneyCard(int amount){
        this.amount = amount;
    }

    @Override
    public int updateBalanceNegative(){
        return amount;
    }
}
