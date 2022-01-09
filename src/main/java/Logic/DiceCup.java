package Logic;

public class DiceCup {
    private Die[] dice;

    public DiceCup(){
        int nrOfDies = 2;
        int nrOfSides = 6;

        dice=new Die[nrOfDies];

        for (int i = 0; i < nrOfDies; i++){
            dice[i] = new Die(nrOfSides);
        }
    }

    public void rollDice(){
        for (Die die: dice){
            die.roll();
        }
    }

    public int[] getFaceValues(){
        int[] values = new int[dice.length];

        for (int i = 0; i < values.length; i++) {
            values[i] = dice[i].getFaceValue();
        }

        return values;
    }

    public int getSum(){
        int result = 0;
        for (Die d: dice){
            result += d.getFaceValue();
        }
        return result;
    }

}
