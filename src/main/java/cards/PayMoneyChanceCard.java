package cards;

public class PayMoneyChanceCard extends ChanceCard{

    public PayMoneyChanceCard(double amount){
        this.amount = amount;
    }

    @Override
    public double updateBalanceNegative(double amount){
        return amount;
    }
}
