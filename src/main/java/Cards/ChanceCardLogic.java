package Cards;

import Board.Board;
import Interface.GUIController;
import Logic.Bank;
import SimpleClasses.Player;

public class ChanceCardLogic {

    Board board;
    Deck deck;
    Bank bank;
    private Player[] players;

    public ChanceCardLogic(Board board, Bank bank, Player[] players) {
        this.deck = new Deck();
        this.board = board;
        this.bank = bank;
        this.players = players;
    }

    /**
     * Call this method, when a player needs to pull a ChanceCard from the deck.
     * The method provides a chanceCard, via the pullCard-method in Deck, and then follows the
     * instructions on that card.
     *
     * @param player represents the player that draws the ChanceCard
     */
    public void handle(Player player){
        ChanceCard card = deck.pullCard();

        GUIController.showCenterMessage(card.getDescription());

        handleMoveFields(card, player);
        handleReceiveMoneyFromBank(card, player);
        handlePayMoney(card, player);
        handleReceiveMoneyFromPlayers(card, player, players);
    }

    // This method calls the updatePlayerPosition in Board, with the int fieldsToMove, provided by the drawn ChanceCard.
    // fieldsToMove can be either positive or negative.
    private void handleMoveFields(ChanceCard card, Player player){
        int fieldsToMove = card.moveNumOfFields();
        if(fieldsToMove != 0) {
            board.updatePlayerPosition(player, fieldsToMove);
        }
    }

    // This method updates the playerBalance.
    // The player RECEIVES money FROM the bank.
    private void handleReceiveMoneyFromBank(ChanceCard card, Player player){
        int amount = card.updateBalancePositive();
        if (amount != 0) {
            bank.bankPayToPlayer(player, amount);
        }
    }

    // This method updates the playerBalance.
    // The player PAYS money TO the bank.
    private void handlePayMoney(ChanceCard card, Player player){
        int amount = card.updateBalanceNegative();
        if (amount != 0) {
            bank.payToBank(player, amount);
        }
    }

    // This method updates the playerBalance
    // The rest of the players PAYS money TO the player who drew the ChanceCard
    private void handleReceiveMoneyFromPlayers(ChanceCard card, Player player, Player ... players){
        int amount = card.updateBalancePositive();
        if (amount !=0) {
            bank.playersPayToPlayer(player, amount, players);
        }

    }
    
}
