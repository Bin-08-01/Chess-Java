package Client.Backend.GameObjects;

import Client.Backend.GameObjects.Pieces.Piece;
import Client.Backend.Players.Positions;

public class Move {

    private final Positions positions;
    private final Piece pieceAtDestination;

    public Move(Positions positions, Piece pieceAtDestination) {
        this.positions = positions;
        this.pieceAtDestination = pieceAtDestination;
    }

    public Positions getPositions() {
        return positions;
    }

    public Piece getPieceAtDestination() {
        return pieceAtDestination;
    }
}
