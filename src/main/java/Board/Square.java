package Board;

import Logic.Player;

public class Square {
    private final String NAME;
    private final int POSITION;

    private final boolean ownable;
    private final String type;

    public Square (String name, int position, boolean ownable, String type)
    {
        this.NAME = name;
        this.POSITION = position;
        this.ownable = ownable;
        this.type = type;
    }

    public String getName(){
        return NAME;
    }

    public int getPOSITION() {
        return POSITION;
    }

    public boolean getOwnable(){
        return ownable;
    }

    public String getType(){
        return type;
    }

    //methods for late binding only
    //used by Street, Ferry and Brewery

    public String getColor(){
        return "";
    }

    public int getPrice(){
        return 0;
    }

    public int[] getRent(){
        return null;
    }

    public Player getOwner(){
        return null;
    }

    public void setOwner(){

    }

    public boolean getPledge(){
        return false;
    }

    public void setPledge(){

    }



}
