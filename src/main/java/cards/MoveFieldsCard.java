package cards;

public class MoveFieldsCard extends ChanceCard{

    protected int fieldsToMove;

    public MoveFieldsCard(String description, int fieldsToMove){
        super(description);
        this.fieldsToMove = fieldsToMove;
    }

    @Override
    public int moveNumOfFields(){
        return this.fieldsToMove;
    }

}
