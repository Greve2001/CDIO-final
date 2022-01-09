package cards;

public class MoveFieldsCard extends ChanceCard{

    protected int fieldsToMove;

    public MoveFieldsCard(int fieldsToMove){
        this.fieldsToMove = fieldsToMove;
    }

    @Override
    public int moveNumOfFields(){
        return this.fieldsToMove;
    }

}
