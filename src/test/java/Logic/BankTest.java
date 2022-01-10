package Logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankTest {
    Player player;
    Bank bank;

    @BeforeEach
    void setup() {
        player = new Player("Goli", 30000, 0);
        bank = new Bank();
    }

    @Test
    void payToBank() {
      bank.payToBank(player,200);
        Assertions.assertEquals(29800,player.getBalance());

        bank.payToBank(player,35000);
        Assertions.assertEquals(0,player.getBalance());
    }

    @Test
    void PlayersPayToPlayer() {
        Player[] players=new Player[]{
                new Player("fishy",25000,0),
                new Player("jack",30000,0),
                new Player("Hidi",35000,0)
        };

        bank.PlayersPayToPlayer(player,30000,players);
        Assertions.assertEquals(115000,player.getBalance());
        Assertions.assertEquals(0,players[0].getBalance());
        Assertions.assertEquals(0,players[1].getBalance());
        Assertions.assertEquals(5000,players[2].getBalance());
    }

    @Test
    void BankpaytoPlayer() {
        bank.BankpaytoPlayer(player,250);
        Assertions.assertEquals(30250,player.getBalance());
    }

    @Test
    void buyHouses() {
        bank.buyHouses(player,101,2);
        Assertions.assertEquals(100,bank.getHousesAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        bank.buyHouses(player,2,20000);
        Assertions.assertEquals(100,bank.getHousesAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        bank.buyHouses(player,10,3000);
        Assertions.assertEquals(90,bank.getHousesAvailable());
        Assertions.assertEquals(0,player.getBalance());
    }

    @Test
    void buyHotels() {
        bank.buyHotels(player,21,2);
        Assertions.assertEquals(20,bank.getHotelsAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        bank.buyHotels(player,2,20000);
        Assertions.assertEquals(20,bank.getHotelsAvailable());
        Assertions.assertEquals(30000,player.getBalance());

        bank.buyHotels(player,5,2500);
        Assertions.assertEquals(15,bank.getHotelsAvailable());
        Assertions.assertEquals(17500,player.getBalance());
    }
}