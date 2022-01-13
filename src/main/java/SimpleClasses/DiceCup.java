package SimpleClasses;

public class DiceCup {
    private Die[] dice;

    /**
     * Creates a standard DiceCup with 2 dice having 6 sides.
     */
    public DiceCup(){
        int nrOfDies = 2;
        int nrOfSides = 6;

        dice=new Die[nrOfDies];

        for (int i = 0; i < nrOfDies; i++){
            dice[i] = new Die(nrOfSides);
        }
    }

    /**
     * @param nrOfDies - the number of dice in the cup
     * @param nrOfSides - the number of sides of each die
     */
    public DiceCup(int nrOfDies, int nrOfSides) {
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
