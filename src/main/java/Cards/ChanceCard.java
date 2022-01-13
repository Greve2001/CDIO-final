package Cards;

/**
 * The ChanceCard-class is made abstract, since it shouldn't be instantiated.
 * The child-classes will override the method, specific to its feature.
 */
public abstract class ChanceCard {

    protected String description;

    public ChanceCard(String description){
        this.description = description;

    }

    /**
     * This method will return 0, unless the ChanceCard dictates, that the player who drew the card
     *      should receive money from the bank.
     * @return the amount stated on the drawn ChanceCard to be RECEIVED FROM the bank.
     */
    public int updateBalancePositive(){
        return 0;
    }

    /**
     * This method will return 0, unless the ChanceCard dictates, that the player who drew the card
     *      should pay money to the bank.
     * @return the amount stated on the drawn ChanceCard to be PAYED TO the bank.
     */
    public int updateBalanceNegative(){
        return 0;
    }

    /**
     * This method will return 0, unless the ChanceCard dictates, that the player who drew the card
     *      should move a number of fields, either forward og backwards on the board.
     * @return the number of fields the player should move (-/+).
     */
    public int moveNumOfFields(){
        return 0;
    }

    public String getDescription(){
        return description;

    }

}
