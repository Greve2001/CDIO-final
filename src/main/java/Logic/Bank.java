package Logic;

import java.lang.reflect.Field;

public class Bank {
    private int housesAvailable = 100;
    private int hotelsAvailable = 20;

    public static void payToBank(Player player, int amount){
       int balance=player.getBalance()-amount;
       player.setBalance(balance);
    }

    /**
     * this method is responsible to handle 2 situation
     *- when a chanceCard states that all players pay to a player
     *- when a player has to pay to another player(pay rent)
    **/
    public static void payToPlayer(Player player,int amount,Player... players){
       int balance;
        for(int i=0; i<players.length; i++){
            balance=players[i].getBalance()-amount;
            players[i].setBalance(balance);

            balance=player.getBalance()+amount;
            player.setBalance(balance);
        }
    }

    //this method is responsible for bank to pay to a player
    public static void payPlayer(Player player,int amount){
       int balance=player.getBalance()+amount;
       player.setBalance(balance);
    }

    public void buyHouses(Player player,int nr, int price){

        if(housesAvailable>=nr){
                if(player.getBalance()>= (price*nr)) {
                    housesAvailable -= nr;
                    int balance=player.getBalance()-(price*nr);
                    player.setBalance(balance);
                }

        }


    }

    public void buyHotels(Player player,int nr,int price){
        if(hotelsAvailable>=nr){
            if(player.getBalance()>=(nr*price)){
                hotelsAvailable-=nr;
                int balance=player.getBalance()-(nr*price);
                player.setBalance(balance);
            }
        }
    }

    public int getHousesAvailable(){
      return housesAvailable;
    }

    public int getHotelsAvailable(){
       return hotelsAvailable;
    }

}
