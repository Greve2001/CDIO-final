package cards;


public abstract class ChanceCard {

    protected String description;
    protected ChanceCardType type;
    protected int value;


    public ChanceCard(ChanceCardType type, String description, int value){
        this.description = description;
        this.type = type;
        this.value = value;

    }
    public ChanceCardType getType(){
        return type;
    }

    public int getValue(){
        return value;
    }

    public String getDescription(){
        return description;
    }
}
