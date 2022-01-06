package Board;

import Logic.Player;

public class Square {
    private final String NAME;
    private final int POSITION;
    private boolean ownable;

    public Square (String name, int position, boolean ownable)
    {
        this.NAME = name;
        this.POSITION = position;
        this.ownable = ownable;
    }

    public String getNAME() {
        return NAME;
    }

    public int getPOSITION() {
        return POSITION;
    }

    public String getColor(){
        return "";
    }

    public Player getOwner(){
        return null;
    }

    public void setOwner(){

    }

    public boolean getOwnable(){
        return ownable;
    }
}
