package Board;

import Logic.Player;

public abstract class Square {
    private final String NAME;
    private final int POSITION;

    public Square (String name, int position)
    {
        this.NAME = name;
        this.POSITION = position;
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

    public int getCurrentCost(int... amountOwned){
        return 0;
    }

    public boolean isBuildAble(){
        return false;
    }
}
