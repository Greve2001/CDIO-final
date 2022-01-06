package Logic;

import java.lang.reflect.Field;

public class Bank {
    private int housesAvailable;
    private int hotelsAvailable;
    private Field[] streets;

    public Bank(int housesAvailable,int hotelsAvailable,Field[] streets){
       this.hotelsAvailable=hotelsAvailable;
       this.housesAvailable=housesAvailable;
       this.streets=streets;
    }

    public void payToBank(Player player, int amount){
       int balance=player.getBalance()-amount;
       player.setBalance(balance);
    }

    /**
     * I implement this method to handle 2 situation
     *- when a chanceCard states that all players pay to a player
     *- when a player has to pay to another player(pay rent)
    **/
    public void payToPlayer(Player player,int amount,Player... players){
       int balance;
        for(int i=0; i<players.length; i++){
            balance=players[i].getBalance()-amount;
            players[i].setBalance(balance);

            balance=player.getBalance()+amount;
            player.setBalance(balance);
        }
    }

    //this method is responsible for bank to pay to a player
    public void payPlayer(Player player,int amount){
       int balance=player.getBalance()+amount;
       player.setBalance(balance);
    }

    public boolean buyHouses(int nr){
        if(housesAvailable>=nr){
            housesAvailable-=nr;
            return true;
        }
        return false;
    }

    public boolean buyHotel(int nr){
        if(hotelsAvailable>=nr){
            hotelsAvailable-=nr;
            return true;
        }
        return false;
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

}
