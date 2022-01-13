package SimpleClasses;

public class Die {
    private int faceValue;
    private int NROFSIDES;

    public Die(int nrOfSides){
        this.NROFSIDES=nrOfSides;
    }

    public void roll(){

        faceValue=(int)(Math.random() * NROFSIDES ) + 1;
    }

    public int getFaceValue(){
        return faceValue;
    }
}
