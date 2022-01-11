package Logic;

import Interface.GUIController;
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
    void when_payToBank_then_amountMustBeDeducted(){
        // Act
        bank.payToBank(player,200);

        // Assert
        Assertions.assertEquals(29800,player.getBalance());
    }

    @Test
    void when_payToBank_withInsufficientAmount_then_amountMustNotBeDeducted() {
        // Act
        bank.payToBank(player,35000);

        // Assert
        Assertions.assertEquals(0,player.getBalance());
    }

    @Test
    void when_playersPayToPlayer_playerAmountShouldBeIncreased() {
        // Arrange
        var fishy = new Player("fishy",5,0);
        var jack = new Player("jack",10,0);
        var hidi = new Player("Hidi",15,0);

        // Act
        GUIController.setTesting(true);
        bank.playersPayToPlayer(player,10, fishy,jack,hidi);

        // Assert
        Assertions.assertEquals(30025, player.getBalance());
    }

    @Test
    void when_playersPayToPlayer_payingPlayersAmountShouldBeDecreased() {
        // Arrange
        var fishy = new Player("fishy",5,0);
        var jack = new Player("jack",10,0);
        var hidi = new Player("Hidi",15,0);

        // Act
        GUIController.setTesting(true);
        bank.playersPayToPlayer(player,10, fishy,jack,hidi);

        // Assert
        Assertions.assertEquals(0, fishy.getBalance());
        Assertions.assertEquals(0, jack.getBalance());
        Assertions.assertEquals(5, hidi.getBalance());
    }

    @Test
    void when_bankPayToPlayer_then_playerAmountMustBeIncreased() {
        // Act
        GUIController.setTesting(true);
        bank.bankPayToPlayer(player,250);

        // Assert
        Assertions.assertEquals(30250,player.getBalance());
    }

    @Test
    void when_buyHouses_and_numHousesInBankIsTooLow_noHousesAreBought(){
        // Act
        bank.buyHouses(player,101,2);

        // Assert
        Assertions.assertEquals(100,bank.getHousesAvailable());
        Assertions.assertEquals(30000,player.getBalance());
    }

    @Test
    void when_buyHouses_and_playerAmountIsTooLow_noHousesAreBought() {
        // Act
        bank.buyHouses(player,2,20000);

        // Assert
        Assertions.assertEquals(100,bank.getHousesAvailable());
        Assertions.assertEquals(30000,player.getBalance());
    }

    @Test
    void when_buyHouses_housesInBankShouldBeDeducted(){
        // Act
        bank.buyHouses(player,10,3000);

        // Assert
        Assertions.assertEquals(90,bank.getHousesAvailable());
    }

    @Test
    void when_buyHouses_playerAmountShouldBeDeducted(){
        // Act
        bank.buyHouses(player,10,3000);

        // Assert
        Assertions.assertEquals(0,player.getBalance());
    }

    @Test
    void when_buyHotels_and_bankHasTooFewHotels_noHotelsShouldBeBought(){
        // Act
        bank.buyHotels(player,21,2);

        // Assert
        Assertions.assertEquals(20,bank.getHotelsAvailable());
        Assertions.assertEquals(30000,player.getBalance());
    }

    @Test
    void when_buyHotels_and_playerHasNotSufficientMoney_noHotelsShouldBeBought(){
        // Act
        bank.buyHotels(player,2,20000);

        // Assert
        Assertions.assertEquals(20,bank.getHotelsAvailable());
        Assertions.assertEquals(30000,player.getBalance());
    }

    @Test
    void when_buyHotels_bankHotelsMustBeDeducted(){
        // Act
        bank.buyHotels(player,5,2500);

        // Assert
        Assertions.assertEquals(15,bank.getHotelsAvailable());
    }

    @Test
    void when_buyHotels_playerAmountMustBeDeducted(){
        // Act
        bank.buyHotels(player,5,2500);

        // Assert
        Assertions.assertEquals(17500,player.getBalance());
    }
}