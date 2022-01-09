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
        System.out.println("StreetAction");
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
            boolean payDouble = board.amountOwnedWithinTheColor(square.getPOSITION()) == 2;

            int priceToPay;
            if (!payDouble) {
                priceToPay = diceSum * square.getRent()[0];
            } else {
                priceToPay = diceSum * square.getRent()[1];
            }
            if (!square.getOwner().isInJail()) //TODO man kan godt modtage penge selvom man er i fængsel
                Bank.payToPlayer(square.getOwner(), priceToPay, player);
        }

    }

    private void ferryAction(Player player, Square square) {
        if (square.getOwner() == null) {
            buySquare(player, square, "buyFerry");
            // Use this when making payment method:
            // if (!square.getOwner().isInJail())
        }
        else{
            int amountOwned = board.amountOwnedWithinTheColor(square.getPOSITION());
            int amountToPay = square.getCurrentCost()
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
