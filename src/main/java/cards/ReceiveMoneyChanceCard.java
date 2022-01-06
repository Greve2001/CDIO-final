package cards;

public class ReceiveMoneyChanceCard extends ChanceCard{

    public ReceiveMoneyChanceCard(double amount){
        this.amount = amount;

    }

    @Override
    public double updateBalancePositive(double amount) {
        return amount;
    }
}
