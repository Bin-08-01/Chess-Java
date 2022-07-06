package Client.Backend.GameObjects.Pieces;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public abstract class Piece extends PieceMovingMethods implements PieceMoves, Serializable {

    private final PieceType type;
    private final PieceColor color;

    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }

    public PieceType getPieceType() {
        return type;
    }

    public PieceColor getPieceColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type && color == piece.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }

    @Override
    public String toString() {
        return "Piece{" +
                ", type=" + type +
                ", color=" + color +
                '}';
    }

    public abstract Piece getClone();

    public abstract String getPieceInString();

    public abstract boolean isLegalMove(Point origin, Point destination, Board board) throws IllegalMoveException;

}
