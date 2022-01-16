package Logic;

import Interface.GUIController;
import SimpleClasses.Player;
import Utilities.Language;

public class Bank {
    private int housesAvailable = 32;
    private int hotelsAvailable = 12;

    public void payToBank(Player player, int amount) {
        if (player.getBalance() >= amount) {
            player.setBalance(player.getBalance() - amount);
        } else {
            player.setBalance(0);
        }
        GUIController.showCenterMessage(player.getName() + Language.get("paid") + amount + Language.get("paidToBank1"));
        GUIController.setPlayerBalance(player.getName(), player.getBalance());
    }

    /**
     * this method is responsible to handle 2 situation
     * - when a chanceCard states that all players pay to a player
     * - when a player has to pay to another player(pay rent)
     **/
    public void playersPayToPlayer(Player player, int amount, Player... players) {
        for (Player value : players) {
            if (value.getBalance() >= amount) {
                player.setBalance(player.getBalance() + amount);

                value.setBalance(value.getBalance() - amount);
            } else {
                player.setBalance(player.getBalance() + value.getBalance());

                value.setBalance(0);
            }
            GUIController.showCenterMessage(value.getName() + Language.get("paid") + amount + Language.get("paidToPlayer") + player.getName());
            GUIController.setPlayerBalance(player.getName(), player.getBalance());
            GUIController.setPlayerBalance(value.getName(), value.getBalance());
        }
    }

    //this method is responsible for bank to pay to a player
    public void bankPayToPlayer(Player player, int amount) {
        int balance = player.getBalance() + amount;
        player.setBalance(balance);
        GUIController.showCenterMessage(Language.get("bankPaid1") + amount + Language.get("bankPaid2") + player.getName());
        GUIController.setPlayerBalance(player.getName(), player.getBalance());
    }

    public void buyHouses(Player player, int nr, int price) {
        if (housesAvailable >= nr && player.getBalance() >= (price * nr)) {
            housesAvailable -= nr;
            int balance = player.getBalance() - (price * nr);
            player.setBalance(balance);
            GUIController.setPlayerBalance(player.getName(), player.getBalance());
        }
    }

    public void buyHotels(Player player, int nr, int price) {
        if (hotelsAvailable >= nr && player.getBalance() >= (nr * price)) {
            hotelsAvailable -= nr;
            int balance = player.getBalance() - (nr * price);
            player.setBalance(balance);
            GUIController.setPlayerBalance(player.getName(), player.getBalance());
        }
    }

    public void setHousesAvailable(int housesAvailable) {
        this.housesAvailable = housesAvailable;
    }

    public int getHousesAvailable() {
        return housesAvailable;
    }

    public void setHotelsAvailable(int hotelsAvailable) {
        this.hotelsAvailable = hotelsAvailable;
    }

    public int getHotelsAvailable() {
        return hotelsAvailable;
    }

}
