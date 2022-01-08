package Logic;

import Interface.GUIController;

import java.lang.reflect.Field;

public class Bank {
    private static int housesAvailable = 100;
    private static int hotelsAvailable = 20;

    public static void payToBank(Player player, int amount){
        if(player.getBalance()>=amount){
            player.setBalance(player.getBalance()-amount);
        }
        else{
            player.setBalance(0);
        }
        GUIController.setPlayerBalance(player, player.getBalance());
    }

    /**
     * this method is responsible to handle 2 situation
     *- when a chanceCard states that all players pay to a player
     *- when a player has to pay to another player(pay rent)
    **/
    public static void payToPlayer(Player player,int amount,Player... players){
       int balance;
        for(int i=0; i<players.length; i++){
            if(players[i].getBalance()>=amount){
                player.setBalance(player.getBalance()+amount);

                players[i].setBalance(players[i].getBalance()-amount);
            }
            else{
                player.setBalance(player.getBalance()+players[i].getBalance());

                players[i].setBalance(0);
            }
            GUIController.setPlayerBalance(player, player.getBalance());
            GUIController.setPlayerBalance(players[i], players[i].getBalance());
        }
    }

    //this method is responsible for bank to pay to a player
    public static void payPlayer(Player player,int amount){
        int balance=player.getBalance()+amount;
        player.setBalance(balance);
        GUIController.setPlayerBalance(player, player.getBalance());
    }

    public static void buyHouses(Player player,int nr, int price){

        if(housesAvailable>=nr){
            if(player.getBalance()>= (price*nr)) {
                housesAvailable -= nr;
                int balance=player.getBalance()-(price*nr);
                player.setBalance(balance);
            }

        }

    }

    public static void buyHotels(Player player,int nr,int price){
        if(hotelsAvailable>=nr){
            if(player.getBalance()>=(nr*price)){
                hotelsAvailable-=nr;
                int balance=player.getBalance()-(nr*price);
                player.setBalance(balance);
            }
        }
    }

    public static int getHousesAvailable(){
      return housesAvailable;
    }

    public static int getHotelsAvailable(){
       return hotelsAvailable;
    }

}
