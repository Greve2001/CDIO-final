package Logic;

import Board.Board;
import Board.IncomeTax;
import Board.Square;
import Board.Tax;
import Logic.Player;import javax.swing.*;

public class ActionHandler {
    private final Bank bank;
    ActionHandler actionHandler;
    Square[] ALL_SQUARES;
    Board board;

    public ActionHandler(Board board){
        bank = new Bank();
        this.board = board;
    }

    public Bank bank(){
        return bank;
    }

    public void squareAction(Player currentPlayer){
        Square square = ALL_SQUARES[currentPlayer.getPosition()];
        if (square.getOwnable()){ //check if player land on a Street, Ferry or Brewery.
            if (square.getOwner() == null){
                //TODO logik to handle purchase
            }
            else{
                switch (square.getName()){
                    case "Street":
                }
            }
        }
        else {
            if (square.getName().equals("Tax")) {
                Tax tempSquare = (Tax) square;
                actionHandler.bank().payToBank(currentPlayer, tempSquare.getAmount());
            } else if (square.getName().equals("IncomeTax")) {
                IncomeTax tempSquare = (IncomeTax) square;
                actionHandler.bank().payToBank(currentPlayer, tempSquare.getAmount());
                //TODO dicision if you want to pay 10% or 4000
            } else if (square.getName().equals("ChanceCard")){
                //TODO logic
            } else if(square.getName().equals("GoToJain")){
                board.setPlayerPosition(currentPlayer,10, true);
            }
        }
    }
}
