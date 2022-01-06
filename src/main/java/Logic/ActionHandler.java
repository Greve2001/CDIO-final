package Logic;

public class ActionHandler {
    private final Bank bank;


    public ActionHandler(){
        bank = new Bank();
    }

    public Bank bank(){
        return bank;
    }

}
