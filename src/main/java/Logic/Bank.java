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

    public String buyHouses(Player player,int nr, int price){
        String msg="";
        if(housesAvailable>=nr){
                if(player.getBalance()>= (price*nr)) {
                    housesAvailable -= nr;
                    int balance=player.getBalance()-(price*nr);
                    player.setBalance(balance);
                    msg="true";
                    return msg;
                }
                else{
                    msg="You don't have sufficient money!";
                }
        }
        else {
            msg="There are not enough houses available!";
        }

        return msg ;
    }

    public String buyHotel(Player player,int nr,int price){
        String msg="";
        if(hotelsAvailable>=nr){
            if(player.getBalance()>=(nr*price)){
                hotelsAvailable-=nr;
                int balance=player.getBalance()-(nr*price);
                player.setBalance(balance);
                msg="true";
            }
            else{
                msg="You don't have sufficient money!";
            }
        }
        else{
          msg="There are not enough hotels available!";
        }

        return msg;
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
