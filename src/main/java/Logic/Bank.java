package Logic;

import java.lang.reflect.Field;

public class Bank {
    private int housesAvailable;
    private int hotelsAvailable;
    private Field[] streets;
    private Field[] shipping;

    public Bank(int housesAvailable,int hotelsAvailable,Field[] streets,Field[] shipping){
       this.hotelsAvailable=hotelsAvailable;
       this.housesAvailable=housesAvailable;
       this.streets=streets;
       this.shipping=shipping;
    }

    public void payToBank(Player player, int amount){
       int balance=player.getBalance()-amount;
       player.setBalance(balance);
    }

    public void payToPlayer(Player player1,Player player2,int amount){
       int balance;
       balance=player1.getBalance()-amount;
       player1.setBalance(balance);

       balance=player2.getBalance()+amount;
       player2.setBalance(balance);
    }

    public void payToPlayer(Player player,int amount){
       int balance=player.getBalance()+amount;
       player.setBalance(balance);
    }

    public int getHousesAvailable(){
      return housesAvailable;
    }

    public int getHotelsAvailable(){
       return hotelsAvailable;
    }

    public Field[] getStreets(){
       return streets;
    }

    public Field[] getShipping(){
       return shipping;
    }

}
