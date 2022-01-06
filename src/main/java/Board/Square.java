package Board;

import Logic.Player;

public class Square {
    private final String NAME;
    private final int POSITION;
    private boolean ownable;
    private final String type;

    public Square (String name, int position, boolean ownable, String type)
    {
        this.NAME = name;
        this.POSITION = position;
        this.ownable = ownable;
        this.type = type;
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

    public String getName(){
        return NAME;
    }

    public int toPay(){
        return 0;
    }

    public String getType(int position){
        return type;
    }
}
