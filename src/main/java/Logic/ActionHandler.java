package Logic;

import Board.*;
import Interface.GUIController;
import Utilities.Language;

public class ActionHandler {
    private final Bank bank = new Bank();
    private Board board;

    public void squareAction(Player player, Square square) {
        switch (square.getType()) {
            case "street" :
                streetAction(player, square);
                break;
            case "brewery" :
                breweryAction(player, square);
                break;
            case "ferry" :
                ferryAction(player, square);
                break;
            case "tax" :
                taxAction(player, square);
                break;
            case "incomeTax" :
                incomeTaxAction(player, square);
                break;
            case "chance" :
                cardAction(player);
                break;
            case "goToPrison" :
                goToPrison(player);
                break;
            default:
                // TODO Implement default case
                break;
        }
    }

    public void streetAction(Player player, Square square) {
        if (square.getOwner() == null) {
            boolean answer = GUIController.askPlayerAccept(Language.get("buyStreet"));

            if(answer) {
                bank.payToBank(player, square.getPrice());
                square.setOwner(player);
                GUIController.setOwner(player, square.getPOSITION());
            }
        } else {
            int amountToPay = board.getCurrentCost(square.getPOSITION());
            bank.payToPlayer(square.getOwner(), amountToPay, player);
        }

    }

    public void breweryAction(Player player, Square square) {

    }

    public void ferryAction(Player player, Square square) {

    }

    public void taxAction(Player player, Square square) {
        bank.payToBank(player, ((Tax) square).getAmount());
    }

    public void incomeTaxAction(Player player, Square square){
        bank.payToBank(player, ((IncomeTax) square).getAmount());
    }

    public void goToPrison(Player player) {
        board.setPlayerInJail(player);
    }

    public void cardAction(Player player) {

    }

    public Bank getBank() {
        return bank;
    }
}
