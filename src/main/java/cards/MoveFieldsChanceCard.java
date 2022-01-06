package cards;

public class MoveFieldsChanceCard extends ChanceCard{

    public MoveFieldsChanceCard(int fieldsToMove){
        this.fieldsToMove = fieldsToMove;
    }

    @Override
    public int moveNumOfFields(int fieldsToMove){
        return fieldsToMove;
    }

}
