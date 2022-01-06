package Logic;

public class Die {
    private int faceValue;

    public void roll(){
        int max=6;
        int min =1;

        faceValue=(int)(Math.random() * ((max - min) + 1)) + min;
    }

    public int getFaceValue(){
        return faceValue;
    }
}
