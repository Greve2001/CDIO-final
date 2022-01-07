package Logic;

import java.awt.Color;

public class Player {
    private final String name;
    private int position; // Make it through constructor or other
    private Color color = Color.white;
    private int balance;
    private boolean active = true; // Used to determinate when a player gone broken.
    private boolean inJail = false;

    public Player(String name, int startBalance, int startPosition){
        this.name = name;
        this.balance = startBalance;
        this.position = startPosition;
    }

    // Prints
    @Override
    public String toString() {
        return name + " at position:" + position + " with " + balance + "kr";
    }

    //Balance
    public int getBalance(){
        return balance;
    }
    public void setBalance(int newBalance){
        balance = newBalance;
    }

    // Name
    public String getName(){
        return name;
    }

    // Position
    public int getPosition(){
        return position;
    }
    public void setPosition(int newPosition){
        position = newPosition;
    }

    //Active
    public boolean getActive(){
        return active;
    }
    public void setActive(boolean active){
        this.active = active;
    }

    // Color for GUI
    public Color getColor(){
        return color;
    }
    public void setColor(Color color){
        this.color = color;
    }

    // Jail
    public boolean isInJail(){
        return inJail;
    }
    public void setInJail(boolean bool){
        inJail = bool;
    }
}
