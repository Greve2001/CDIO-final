package cards;

abstract class ChanceCard {

    protected double amount;
    protected int fieldsToMove;

    public double updateBalancePositive(double amount){
        return 0.0;
    }

    public double updateBalanceNegative(double amount){
        return 0.0;
    }

    public int moveNumOfFields(int fieldsToMove){
        return 0;
    }

}
