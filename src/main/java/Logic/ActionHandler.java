package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;
import cards.ChanceCard;
import cards.Deck;

public class ActionHandler {
    private final Bank BANK = new Bank();
    private final Board BOARD;
    private Player[] players;
    private Deck deck;
    private int activeBidders;

    public ActionHandler(Board BOARD) {
        this.BOARD = BOARD;
        this.deck = new Deck();

    }

    public void squareAction(Player player, Square square, int diceSum) {
        switch (square.getClass().getSimpleName()) {
            case "Street":
                streetAction(player, square);
                break;
            case "Brewery":
                breweryAction(player, square, diceSum);
                break;
            case "Ferry":
                ferryAction(player, square);
                break;
            case "Tax":
                taxAction(player, square);
                break;
            case "IncomeTax":
                incomeTaxAction(player, square);
                break;
            case "Chance":
                cardAction(player);
                break;
            case "GoToPrison":
                goToPrison(player);
                break;
            default:

                // TODO Implement default case
                break;
        }
    }

    private void streetAction(Player player, Square square) {
        if (square.getOwner() == null) { // Buy it
            buySquare(player, square, "buyStreet");

        } else { // Pay the rent
            int amountToPay = BOARD.getCurrentCost(square.getPOSITION());
            payRent(player, square, amountToPay);
        }

    }

    private void breweryAction(Player player, Square square, int diceSum) {
        if (square.getOwner() == null) { // Ask to buy
            buySquare(player, square, "buyBrewery");
        } else { // Pay the rent
            boolean payDouble = BOARD.amountOwnedWithinTheColor(square.getPOSITION()) == 2;

            // Doubles the amount of rent if the player owns all breweries.
            int amountToPay;
            if (!payDouble) {
                amountToPay = diceSum * square.getRent()[0];
            } else {
                amountToPay = diceSum * square.getRent()[1];
            }

            payRent(player, square, amountToPay);
        }

    }

    private void ferryAction(Player player, Square square) {
        if (square.getOwner() == null) { // Buy if no owner
            buySquare(player, square, "buyFerry");
        } else { // Pay the rent
            int amountOwned = BOARD.amountOwnedWithinTheColor(square.getPOSITION());
            int amountToPay = square.getCurrentCost(amountOwned);

            payRent(player, square, amountToPay);
        }

    }

    // Ask the player to buy the square and withdraw amount from bank.
    private void buySquare(Player player, Square square, String msg) {
        boolean answer = GUIController.askPlayerAccept(Language.get(msg));

        if (answer) {
            BANK.payToBank(player, square.getPrice());
            square.setOwner(player);
            GUIController.setOwner(player, square.getPOSITION());
        } else {
            holdAuction(player, square);
        }
    }

    public void holdAuction(Player player, Square square) {
        int biddingPlayer = 0;
        activeBidders = 0;
        boolean[] participants = new boolean[players.length];

        for (int i = 0; i < players.length; i++) {
            // Checks which players initially are allowed to participate in the auction.
            if (players[i].getActive()) {
                participants[i] = true;
                activeBidders++;
            }

            // Finds the current player out of all players.
            if (players[i].getName().equals(player.getName()))
                biddingPlayer = i;
        }

        boolean notSold = true;
        int highestBid = 0;
        while (notSold) {
            // Declares the last active bidder in the auction the winner.
            if (activeBidders == 1) {
                declareAuctionWinner(participants, square, highestBid);
                notSold = false;
            } else if (participants[biddingPlayer]) { // Checks if the player is still an active bidder before letting them place a new bet.
                highestBid = biddingRound(participants, highestBid, biddingPlayer);
            }

            // Change the bidding player.
            if (biddingPlayer >= players.length - 1)
                biddingPlayer = 0;
            else
                biddingPlayer++;
        }
    }

    public void declareAuctionWinner(boolean[] participants, Square square, int highestBid) {
        for (int i = 0; i < participants.length; i++) {
            if (participants[i]) {
                GUIController.showMessage(players[i].getName() + Language.get("hasWonAuction"));

                square.setOwner(players[i]);
                BANK.payToBank(players[i], highestBid);
                GUIController.setOwner(players[i], square.getPOSITION());
            }
        }
    }

    public int biddingRound(boolean[] participants, int highestBid, int biddingPlayer) {
        boolean wantToBid = GUIController.askPlayerAccept(players[biddingPlayer].getName() + Language.get("wishToBid"));

        // Asks if the players wants to bet and if not takes them out of the auction.
        if (!wantToBid) {
            participants[biddingPlayer] = false;
            activeBidders--;
        } else {
            int bid;

            // Ask the player how much they want to bet and checks if the bet is large enough.
            do {
                bid = GUIController.getPlayerInteger(players[biddingPlayer].getName() +
                        Language.get("askForBid") + highestBid + " kr.)");

            } while (bid < highestBid + 100);

            if (bid >= highestBid + 100)
                highestBid = bid;
        }

        return highestBid;
    }

    // Pay the owner if they are not in jail and the square is not pledged.
    private void payRent(Player player, Square square, int amount) {
        if (!square.getOwner().isInJail() && !square.getPledge() && square.getOwner().getActive())
            BANK.playersPayToPlayer(square.getOwner(), amount, player);
    }

    private void taxAction(Player player, Square square) {
        BANK.payToBank(player, ((Tax) square).getAmount());
    }

    private void incomeTaxAction(Player player, Square square) {
        IncomeTax incomeTaxSquare = (IncomeTax) square;
        String[] choices = {Language.get("payPct") + incomeTaxSquare.getPercentage() + "%", incomeTaxSquare.getAmount() + " kr."};
        String answer = GUIController.givePlayerChoice(Language.get("payIncomeTax"), choices);

        if (answer.equals(choices[0])) {
            int fortune = BOARD.playerTotalValue(player);
            int amountToPay = fortune * incomeTaxSquare.getPercentage() / 100;
            amountToPay = roundToNearest50(amountToPay);
            BANK.payToBank(player, amountToPay);
        } else {
            BANK.payToBank(player, incomeTaxSquare.getAmount());
        }
    }

    // TODO fix rounding errors
    public int roundToNearest50(int valueToRound) {
        int modulo = valueToRound % 50;
        if (modulo < 25)
            valueToRound = valueToRound - modulo;
        else
            valueToRound = valueToRound + (50 - modulo);

        return valueToRound;
    }

    private void goToPrison(Player player) {
        BOARD.setPlayerInJail(player);
    }

    /**
     * Call this method, when a player needs to pull a ChanceCard from the deck.
     * The method provides a chanceCard, via the pullCard-method in Deck, and then follows the
     * instructions on that card.
     *
     * @param player represents the player that draws the ChanceCard
     */
    public void cardAction(Player player) {
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
            BOARD.updatePlayerPosition(player, fieldsToMove);
        }
    }

    // This method updates the playerBalance.
    // The player RECEIVES money FROM the bank.
    private void handleReceiveMoney(ChanceCard card, Player player){
        int amount = card.updateBalancePositive();
        if (amount != 0) {
            BANK.bankPayToPlayer(player, amount);
        }
    }

    // This method updates the playerBalance.
    // The player PAYS money TO the bank.
    private void handlePayMoney(ChanceCard card, Player player){
        int amount = card.updateBalanceNegative();
        if (amount != 0) {
            BANK.payToBank(player, amount);
        }
    }


    public void boardPaymentsToBank(Player player, int amount) {
        if (amount < 0)
            BANK.bankPayToPlayer(player, Math.abs(amount));
        else
            BANK.payToBank(player, amount);
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public boolean buyHouse(Player player, int price, int amount) {
        return BANK.buyHouses(player, amount, price);
    }

    public boolean buyHotels(Player player, int price, int amount) {
        return BANK.buyHotels(player, amount, price);
    }

    public int getHousesAvailable() {
        return BANK.getHousesAvailable();
    }

    public int getHotelsAvailable() {
        return BANK.getHotelsAvailable();
    }
}
