package SimpleClasses;

public class DiceCup {
    private final Die[] DICE;

    /**
     * Creates a standard DiceCup with 2 dice having 6 sides.
     */
    public DiceCup(){
        int nrOfDies = 2;
        int nrOfSides = 6;

        DICE =new Die[nrOfDies];

        for (int i = 0; i < nrOfDies; i++){
            DICE[i] = new Die(nrOfSides);
        }
    }

    /**
     * @param nrOfDies - the number of dice in the cup
     * @param nrOfSides - the number of sides of each die
     */
    public DiceCup(int nrOfDies, int nrOfSides) {
        DICE =new Die[nrOfDies];

        for (int i = 0; i < nrOfDies; i++){
            DICE[i] = new Die(nrOfSides);
        }
    }

    public void rollDice(){
        for (Die die: DICE){
            die.roll();
        }
    }

    public int[] getFaceValues(){
        int[] values = new int[DICE.length];

        for (int i = 0; i < values.length; i++) {
            values[i] = DICE[i].getFaceValue();
        }

        return values;
    }

    public int getSum(){
        int result = 0;
        for (Die d: DICE){
            result += d.getFaceValue();
        }
        return result;
    }

}
