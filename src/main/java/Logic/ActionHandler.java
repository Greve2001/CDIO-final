package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;

public class ActionHandler {
    private final Bank BANK = new Bank();
    private final Board BOARD;
    private Player[] players;

    public ActionHandler(Board BOARD) {
        this.BOARD = BOARD;
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
        int activeBidders = 0;
        boolean[] participants = new boolean[players.length];
        for (int i = 0; i < players.length; i++) {
            if(players[i].getActive()) {
                participants[i] = true;
                activeBidders++;
            }

            if(players[i].getName().equals(player.getName()))
                biddingPlayer = i;
        }

        boolean notSold = true;
        int highestBid = 0;
        while(notSold) {
            boolean wantToBid;
            if (activeBidders == 1) {
               for (int i = 0; i < participants.length; i++) {
                    if (participants[i]) {
                        square.setOwner(players[i]);
                        BANK.payToBank(players[i], highestBid);
                        GUIController.setOwner(players[i], square.getPOSITION());
                        notSold = false;
                    }
                }
            } else {

                if (participants[biddingPlayer]) {
                    wantToBid = GUIController.askPlayerAccept(players[biddingPlayer].getName() + Language.get("wishToBid"));
                    if (!wantToBid) {
                        participants[biddingPlayer] = false;
                        activeBidders--;
                    } else {
                        int bid;

                        do {
                            bid = GUIController.getPlayerInteger(players[biddingPlayer].getName() +
                                    Language.get("askForBid") + highestBid + " kr.)");

                        } while (bid < highestBid + 100);

                        if (bid >= highestBid + 100)
                            highestBid = bid;
                    }
                }

                if (biddingPlayer >= players.length - 1)
                    biddingPlayer = 0;
                else
                    biddingPlayer++;
            }
        }
    }

    // Pay the owner if they are not in jail.
    // TODO Implement if the square is pledged as well.
    private void payRent(Player player, Square square, int amount) {
        if (!square.getOwner().isInJail())
            BANK.PlayersPayToPlayer(square.getOwner(), amount, player);
    }

    private void taxAction(Player player, Square square) {
        BANK.payToBank(player, ((Tax) square).getAmount());
    }

    private void incomeTaxAction(Player player, Square square) {
        IncomeTax incomeTaxSquare = (IncomeTax) square;
        String[] choices = {Language.get("payPct") + incomeTaxSquare.getPercentage(), incomeTaxSquare.getAmount() + " kr."};
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

    public int roundToNearest50(int valueToRound) {
        int modulo = valueToRound % 50;
        if (modulo < 25)
            valueToRound = valueToRound - (50 + modulo);
        else
            valueToRound = valueToRound + (50 - modulo);

        return valueToRound;
    }

    private void goToPrison(Player player) {
        BOARD.setPlayerInJail(player);
    }

    public void cardAction(Player player) {

    }

    public void boardPaymentsToBank(Player player, int amount) {
        BANK.payToBank(player, amount);
    }

    public void setPlayers(Player[] players){
        this.players = players;
    }
}
