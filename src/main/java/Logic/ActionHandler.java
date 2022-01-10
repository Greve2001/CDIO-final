package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;

public class ActionHandler {
    private final Bank bank = new Bank();
    private final Board BOARD;

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
        if (square.getOwner() == null) {
            buySquare(player, square, "buyBrewery");
        } else {
            boolean payDouble = BOARD.amountOwnedWithinTheColor(square.getPOSITION()) == 2;

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
        if (square.getOwner() == null) {
            buySquare(player, square, "buyFerry");
        } else {
            int amountOwned = BOARD.amountOwnedWithinTheColor(square.getPOSITION());
            int amountToPay = square.getCurrentCost(amountOwned);

            payRent(player, square, amountToPay);
        }

    }

    // Ask the player to buy the square and withdraw amount from bank.
    private void buySquare(Player player, Square square, String msg) {
        boolean answer = GUIController.askPlayerAccept(Language.get(msg));

        if (answer) {
            bank.payToBank(player, square.getPrice());
            square.setOwner(player);
            GUIController.setOwner(player, square.getPOSITION());
        }
        // TODO Implement auction as else clause
    }

    // Pay the owner if they are not in jail.
    private void payRent(Player player, Square square, int amount) {
        if (!square.getOwner().isInJail())
            bank.payToPlayer(square.getOwner(), amount, player);
    }

    private void taxAction(Player player, Square square) {
        bank.payToBank(player, ((Tax) square).getAmount());
    }

    private void incomeTaxAction(Player player, Square square) {
        IncomeTax incomeTaxSquare = (IncomeTax) square;
        String[] choices = {Language.get("payPct") + incomeTaxSquare.getPercentage(), incomeTaxSquare.getAmount() + " kr."};
        String answer = GUIController.givePlayerChoice(Language.get("payIncomeTax"), choices);

        if (answer.equals(choices[0])) {
            int fortune = BOARD.playerTotalValue(player);
            int amountToPay = fortune * incomeTaxSquare.getPercentage() / 100;
            bank.payToBank(player, amountToPay);
        } else {
            bank.payToBank(player, incomeTaxSquare.getAmount());
        }
    }

    private void goToPrison(Player player) {
        BOARD.setPlayerInJail(player);
    }

    public void cardAction(Player player) {

    }

    public void boardPaymentsToBank(Player player, int amount) {
        bank.payToBank(player, amount);
    }
}
