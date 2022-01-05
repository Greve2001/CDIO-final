public class Player {
    private final String name;
    private int position; // Make it through constructor or other
    private int balance;

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
}
