package Logic;

public class Die {
    private int faceValue;
    private int NROFSIDES;

    public Die(int NROFSIDES){
        this.NROFSIDES=NROFSIDES;
    }
    public void roll(){

        faceValue=(int)(Math.random() * NROFSIDES ) + 1;
    }

    public int getFaceValue(){
        return faceValue;
    }
}
