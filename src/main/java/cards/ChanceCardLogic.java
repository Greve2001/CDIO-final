package cards;

import Board.Board;
import Logic.Bank;
import Logic.Player;

public class ChanceCardLogic {

    Board board;
    Deck deck;
    Bank bank;

    public ChanceCardLogic(Board board, Bank bank) {
        this.deck = new Deck();
        this.board = board;
        this.bank = bank;
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

        handleMoveFields(card, player);
        handleReceiveMoney(card, player);
        handlePayMoney(card, player);

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
    private void handleReceiveMoney(ChanceCard card, Player player){
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
    
}
