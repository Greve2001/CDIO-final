package Logic;

import Board.*;
import Interface.GUIController;
import SimpleClasses.Player;
import Utilities.Language;
import Cards.ChanceCard;
import Cards.Deck;

public class ActionHandler {
    private final Bank BANK = new Bank();
    private final Board BOARD;
    private Player[] players;
    private final Deck DECK;
    private int activeBidders;

    public ActionHandler(Board BOARD) {
        this.BOARD = BOARD;
        this.DECK = new Deck();

    }

    public void squareAction(Player player, Square square, int diceSum) {
        switch (square.getClass().getSimpleName()) {
            case "Street" -> streetAction(player, square);
            case "Brewery" -> breweryAction(player, square, diceSum);
            case "Ferry" -> ferryAction(player, square);
            case "Tax" -> taxAction(player, square);
            case "IncomeTax" -> incomeTaxAction(player, square);
            case "Chance" -> cardAction(player);
            case "GoToPrison" -> goToPrison(player);
            default -> {
                // If square doesn't have any associated action do nothing.
            }
        }
    }

    private void streetAction(Player player, Square square) {
        if (square.getOwner() == null) { // Buy it
            buySquare(player, square, "buyStreet");

        } else if (square.getOwner() != player){ // Pay the rent
            int amountToPay = BOARD.getCurrentCost(square.getPOSITION());
            payRent(player, square, amountToPay);
        }
    }

    private void breweryAction(Player player, Square square, int diceSum) {
        if (square.getOwner() == null) { // Ask to buy
            buySquare(player, square, "buyBrewery");

        } else if (square.getOwner() != player) { // Pay the rent
            // Doubles the amount of rent if the player owns all breweries.
            int amountToPay = BOARD.getCurrentCost(square.getPOSITION()) * diceSum;
            payRent(player, square, amountToPay);
        }
    }

    private void ferryAction(Player player, Square square) {
        if (square.getOwner() == null) { // Buy if no owner
            buySquare(player, square, "buyFerry");

        } else if (square.getOwner() != player) { // Pay the rent
            int amountToPay = BOARD.getCurrentCost(square.getPOSITION());
            payRent(player, square, amountToPay);
        }
    }

    // Ask the player to buy the square and withdraw amount from bank.
    private void buySquare(Player player, Square square, String msg) {
        boolean answer = GUIController.askPlayerAccept(Language.get(msg));

        if (answer) {
            BANK.payToBank(player, square.getPrice());
            square.setOwner(player);
            GUIController.setOwner(player.getName(), player.getColor(), square.getPOSITION());
            GUIController.updateRent(square.getPOSITION(), BOARD.getCurrentCost(square.getPOSITION()));

        } else {
            holdAuction(player, square);
        }
    }

    private void holdAuction(Player player, Square square) {
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

    private void declareAuctionWinner(boolean[] participants, Square square, int highestBid) {
        for (int i = 0; i < participants.length; i++) {
            if (participants[i]) {
                GUIController.showMessage(players[i].getName() + Language.get("hasWonAuction"));

                square.setOwner(players[i]);
                BANK.payToBank(players[i], highestBid);
                GUIController.setOwner(players[i].getName(), players[i].getColor(), square.getPOSITION());
                GUIController.updateRent(square.getPOSITION(), BOARD.getCurrentCost(square.getPOSITION()));
            }
        }
    }

    private int biddingRound(boolean[] participants, int highestBid, int biddingPlayer) {
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

    private int roundToNearest50(int valueToRound) {
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
    private void cardAction(Player player) {
        ChanceCard card = DECK.pullCard();

        GUIController.showCenterMessage(card.getDescription());
        GUIController.getPlayerAction(player.getName(), Language.get("hitChance"));

        switch (card.getType()) {
            case PAY_MONEY_TO_BANK_CARD -> handlePayMoneyToBank(card, player);
            case RECEIVE_MONEY_FROM_BANK_CARD -> handleReceiveMoneyFromBank(card, player);
            case MOVE_NR_OF_FIELDS_CARD -> handleMoveNrOfFields(card, player);
            case RECEIVE_MONEY_FROM_PLAYERS_CARD -> handleReceiveMoneyFromPlayers(card, player, players);
            case MOVE_TO_SPECIFIC_FIELD_CARD -> handleMoveToSpecificField(card, player);
            case MATADOR_GRANT_CARD -> handleMatadorGrantCard(card, player);
            case GET_OUT_OF_JAIL_CARD -> handleGetOutOfJailCard(player);
        }
    }

    // The player PAYS money TO the bank.
    private void handlePayMoneyToBank(ChanceCard card, Player player) {
        int amount = card.getValue();

        BANK.payToBank(player, amount);
    }

    // The player RECEIVES money FROM the bank.
    private void handleReceiveMoneyFromBank(ChanceCard card, Player player) {
        int amount = card.getValue();

        BANK.bankPayToPlayer(player, amount);
    }

    // This method calls the updatePlayerPosition in Board, with the int fieldsToMove, provided by the drawn ChanceCard.
    // fieldsToMove can be either positive or negative.
    private void handleMoveNrOfFields(ChanceCard card, Player player) {
        int fieldsToMove = card.getValue();

        BOARD.updatePlayerPosition(player, fieldsToMove);
    }

    // The rest of the players PAY money TO the player who drew the ChanceCard
    private void handleReceiveMoneyFromPlayers(ChanceCard card, Player player, Player... players) {
        int amount = card.getValue();

        BANK.playersPayToPlayer(player, amount, players);
    }

    private void handleMoveToSpecificField(ChanceCard card, Player player) {
        int fieldNr = card.getValue();

        boolean goingToJail = fieldNr == BOARD.getJailPosition();

        BOARD.setPlayerPosition(player, fieldNr, goingToJail);
    }

    private void handleMatadorGrantCard(ChanceCard card, Player player) {
        int grant = card.getValue();
        int playerPossessions = BOARD.playerTotalValue(player);

        if (playerPossessions <= 15000) {
            BANK.bankPayToPlayer(player, grant);
        }
    }

    private void handleGetOutOfJailCard(Player player) {
        player.giveOneGetOutOfJailCard();
    }

    // TODO Implement the chance card that uses this
    private void moveToNearest(Player player, String type) {
        int position = player.getPosition();
        Square[] squares = BOARD.getALL_SQUARES();

        boolean notFound = true;
        while (notFound) {
            if (squares.length <= position + 1)
                position = 0;

            String squareType = squares[++position].getClass().getSimpleName();

            if (type.equals(squareType)) {
                notFound = false;
            }
        }

        BOARD.setPlayerPosition(player, position, false);
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

    public void buyHouse(Player player, int price, int amount) {
        BANK.buyHouses(player, amount, price);
    }

    public void buyHotels(Player player, int price, int amount) {
        BANK.buyHotels(player, amount, price);
    }

    public void sellHouses(Player player, int pricePerHouse, int numOfHouses) {
        BANK.bankPayToPlayer(player, pricePerHouse * numOfHouses);
        BANK.setHousesAvailable(BANK.getHousesAvailable() + numOfHouses);
    }

    public void sellHotels(Player player, int pricePerHotel, int numOfHotels) {
        BANK.bankPayToPlayer(player, pricePerHotel * numOfHotels);
        BANK.setHotelsAvailable(BANK.getHotelsAvailable() + numOfHotels);
    }

    public int getHousesAvailable() {
        return BANK.getHousesAvailable();
    }

    public int getHotelsAvailable() {
        return BANK.getHotelsAvailable();
    }

    public void bankruptPlayerHandover(int amountOfHouses, int amountOfHotels) {
        BANK.setHousesAvailable(BANK.getHousesAvailable() + amountOfHouses);
        BANK.setHotelsAvailable(BANK.getHotelsAvailable() + amountOfHotels);
    }
}
