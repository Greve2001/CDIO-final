package Board;

import Logic.Player;

abstract class Square {
    private final String NAME;
    private final int POSITION;

    private final String type;

    public Square (String name, int position)
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
        return false;
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

    public void setOwner(Player player){

    }

    public boolean getPledge(){
        return false;
    }

    public void setPledge(boolean nun) {
        return;
    }

    public int getAmountOfHouses(){
        return 0;
    }

    public int getCurrentCost(){
        return 0;
    }
}
