package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;

public class ActionHandler {
    private final Bank bank = new Bank();
    private Board board;

    public ActionHandler(Board board) {
        this.board = board;
    }

    public void squareAction(Player player, Square square, int diceSum) {
        switch (square.getType()) {
            case "street":
                streetAction(player, square);
                break;
            case "brewery":
                breweryAction(player, square, diceSum);
                break;
            case "ferry":
                ferryAction(player, square);
                break;
            case "tax":
                taxAction(player, square);
                break;
            case "incomeTax":
                incomeTaxAction(player, square);
                break;
            case "chance":
                cardAction(player);
                break;
            case "goToPrison":
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
            int amountToPay = board.getCurrentCost(square.getPOSITION());

            if (!square.getOwner().isInJail())
                Bank.payToPlayer(square.getOwner(), amountToPay, player);
        }

    }

    private void breweryAction(Player player, Square square, int diceSum) {
        if (square.getOwner() == null) {
            buySquare(player, square, "buyBrewery");
        } else {
            boolean payDouble = board.hasMonopoly(square.getPOSITION(), square.getOwner());

            int priceToPay;
            if (!payDouble) {
                priceToPay = diceSum * square.getRent()[0];
            } else {
                priceToPay = diceSum * square.getRent()[1];
            }
            if (!square.getOwner().isInJail())
                Bank.payToPlayer(square.getOwner(), priceToPay, player);
        }

    }

    private void ferryAction(Player player, Square square) {
        if (square.getOwner() == null) {
            buySquare(player, square, "buyFerry");
            // Use this when making payment method:
            // if (!square.getOwner().isInJail())
        }

    }

    private void buySquare(Player player, Square square, String msg) {
        boolean answer = GUIController.askPlayerAccept(Language.get(msg));

        if (answer) {
            Bank.payToBank(player, square.getPrice());
            square.setOwner(player);
            GUIController.setOwner(player, square.getPOSITION());
        }
    }

    private void taxAction(Player player, Square square) {
        Bank.payToBank(player, ((Tax) square).getAmount());
    }

    private void incomeTaxAction(Player player, Square square) {
        // TODO get percentage from csv
        String[] choices = {Language.get("pay10pct"), "4000 kr."};
        String chosen = GUIController.givePlayerChoice(Language.get("payIncomeTax"), choices);

        if (chosen.equals(choices[0])) {
            // TODO Calculate player fortune and make the player pay 10% of this to the bank
            Bank.payToBank(player, ((IncomeTax) square).getAmount());
        } else {
            Bank.payToBank(player, ((IncomeTax) square).getAmount());
        }
    }

    private void goToPrison(Player player) {
        board.setPlayerInJail(player);
    }

    public void cardAction(Player player) {

    }

    public Bank getBank() {
        return bank;
    }
}
