package Logic;

public class Cup {
    private Die[] dice;

    public Cup(){
        dice=new Die[2];
        dice[0]=new Die();
        dice[1]=new Die();
    }

    public void rollDice(){
        dice[0].roll();
        dice[1].roll();
    }

    public int[] getFaceValues(){
        int[] values= new int[2];
        values[0]= dice[0].getFaceValue();
        values[1]= dice[1].getFaceValue();

        return values;
    }
}
