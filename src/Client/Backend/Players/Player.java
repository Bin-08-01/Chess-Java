package Client.Backend.Players;

import Client.Backend.GameObjects.Pieces.PieceColor;
import Client.GameManagers.ChessGame;

import java.awt.*;

public class Player {

    private final PieceColor playersPieceColor;
    private Positions currentPositions;
    protected ChessGame game;

    public Player(PieceColor playersPieceColor) {
        this.playersPieceColor = playersPieceColor;
    }

    public void addPositionToMove(Point position) {
       if(currentPositions == null) {
           currentPositions = new Positions(position, playersPieceColor);
       } else if(game.getPiece(position) != null && game.getPiece(position).getPieceColor() == playersPieceColor){
           currentPositions = new Positions(position, playersPieceColor);
       }  else if(!currentPositions.isReadyToBeUsed()) {
           currentPositions.setDestination(position);
           game.move(currentPositions, this);
       } else {
           currentPositions = new Positions(position, playersPieceColor);
       }
    }

    public Positions getMove() {
        if (currentPositions == null || !currentPositions.isReadyToBeUsed()) throw new Error("Move data is not complete and ready for usage");
        return currentPositions;
    }

    public PieceColor getPlayersColor() {
        return playersPieceColor;
    }

    public void setContext(ChessGame game) {
        if(this.game == null) {
            this.game = game;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Player)) return false;
        Player player = (Player) obj;
        return this.playersPieceColor == player.playersPieceColor;
    }

}
