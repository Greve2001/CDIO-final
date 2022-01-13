package Cards;

public class ReceiveMoneyFromPlayersCard extends ChanceCard{

    protected int amount;

    public ReceiveMoneyFromPlayersCard(String description, int amount){
        super(description);
        this.amount = amount;
    }

    @Override
    public int updateBalancePositive(){
        return amount;
    }
}
