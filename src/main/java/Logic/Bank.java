package Logic;

import Interface.GUIController;

public class Bank {
    private int housesAvailable = 100;
    private int hotelsAvailable = 20;

    public void payToBank(Player player, int amount) {
        if (player.getBalance() >= amount) {
            player.setBalance(player.getBalance() - amount);
        } else {
            player.setBalance(0);
        }
        GUIController.setPlayerBalance(player, player.getBalance());
    }

    /**
     * this method is responsible to handle 2 situation
     * - when a chanceCard states that all players pay to a player
     * - when a player has to pay to another player(pay rent)
     **/
    public void playersPayToPlayer(Player player, int amount, Player... players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].getBalance() >= amount) {
                player.setBalance(player.getBalance() + amount);

                players[i].setBalance(players[i].getBalance() - amount);
            } else {
                player.setBalance(player.getBalance() + players[i].getBalance());

                players[i].setBalance(0);
            }
            GUIController.setPlayerBalance(player, player.getBalance());
            GUIController.setPlayerBalance(players[i], players[i].getBalance());
        }
    }

    //this method is responsible for bank to pay to a player
    public void bankPayToPlayer(Player player, int amount) {
        int balance = player.getBalance() + amount;
        player.setBalance(balance);
        GUIController.setPlayerBalance(player, player.getBalance());
    }

    public boolean buyHouses(Player player, int nr, int price) {
        boolean result = false;
        if (housesAvailable >= nr && player.getBalance() >= (price * nr)) {
            housesAvailable -= nr;
            int balance = player.getBalance() - (price * nr);
            player.setBalance(balance);
            result = true;
        }

        return result;
    }

    public boolean buyHotels(Player player, int nr, int price) {
        boolean result = false;
        if (hotelsAvailable >= nr && player.getBalance() >= (nr * price)) {
            hotelsAvailable -= nr;
            int balance = player.getBalance() - (nr * price);
            player.setBalance(balance);
        }

        return result;
    }

    public int getHousesAvailable() {
        return housesAvailable;
    }

    public int getHotelsAvailable() {
        return hotelsAvailable;
    }

}
