package Logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Player player=new Player("Goli",30000,0);

    @Test
    void payToBank() {
      Bank.payToBank(player,200);
        Assertions.assertEquals(29800,player.getBalance());

        Bank.payToBank(player,35000);
        Assertions.assertEquals(0,player.getBalance());
    }

    @Test
    void payToPlayer() {
        Player[] players=new Player[]{
                new Player("fishy",25000,0),
                new Player("jack",30000,0),
                new Player("Hidi",35000,0)
        };

        Bank.payToPlayer(player,30000,players);
        Assertions.assertEquals(115000,player.getBalance());
        Assertions.assertEquals(0,players[0].getBalance());
        Assertions.assertEquals(0,players[1].getBalance());
        Assertions.assertEquals(5000,players[2].getBalance());
    }

    @Test
    void payPlayer() {
        Bank.payPlayer(player,250);
        Assertions.assertEquals(30250,player.getBalance());
    }

    @Test
    void buyHouses() {
        Bank.buyHouses(player,101,2);
        Assertions.assertEquals(100,Bank.getHousesAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        Bank.buyHouses(player,2,20000);
        Assertions.assertEquals(100,Bank.getHousesAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        Bank.buyHouses(player,10,3000);
        Assertions.assertEquals(90,Bank.getHousesAvailable());
        Assertions.assertEquals(0,player.getBalance());
    }

    @Test
    void buyHotels() {
        Bank.buyHotels(player,21,2);
        Assertions.assertEquals(20,Bank.getHotelsAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        Bank.buyHotels(player,2,20000);
        Assertions.assertEquals(20,Bank.getHotelsAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        Bank.buyHotels(player,5,2500);
        Assertions.assertEquals(15,Bank.getHotelsAvailable());
        Assertions.assertEquals(17500,player.getBalance());
    }
}