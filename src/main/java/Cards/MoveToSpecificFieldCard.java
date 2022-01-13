package Cards;

public class MoveToSpecificFieldCard extends ChanceCard{


    public MoveToSpecificFieldCard(String description, int goToFieldNr){
        super(ChanceCardType.MOVE_TO_SPECIFIC_FIELD_CARD, description, goToFieldNr);
    }
}
