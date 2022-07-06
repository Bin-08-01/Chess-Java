package Client.Backend.GameObjects;

import Client.Backend.GameObjects.Pieces.Piece;

import java.awt.*;
import java.io.Serializable;

public class PromotionMessage implements Serializable {

    private final Point position;
    private final Piece newPiece;

    public PromotionMessage(Point position, Piece newPiece) {
        this.position = position;
        this.newPiece = newPiece.getClone();
    }

    public Point getPosition() {
        return position;
    }

    public Piece getPiece() {
        return newPiece;
    }

    @Override
    public String toString() {
        return position.toString() + newPiece.toString();
    }
}
