package Client.Backend.GameObjects.Pieces;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.GameObjects.Board.Board;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Client.Backend.GameRules.BoardConditionsChecker.*;
import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static Client.GameManagers.ChessGame.SUM_OF_ROWS;

public class Rook extends Piece implements PieceMoves{

    private boolean moved;

    public Rook(PieceColor color) {
        super(PieceType.ROOK, color);
        moved = false;
    }

    @Override
    public Piece getClone() {
        return new Rook(getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType() + " ";
    }

    @Override
    public boolean isLegalMove(Point origin, Point destinationsPosition, Board board) throws IllegalMoveException {
        if(destinationsPosition.x != origin.x && destinationsPosition.y != origin.y) return false;
        this.board = board;
        Point tempDestination = new Point(origin);
        int direction;
        if(destinationsPosition.x == origin.x) {
            direction = getDirection(origin.y, destinationsPosition.y);
            while (tempDestination.y != destinationsPosition.y) {
                tempDestination.y += direction;
                if(!isInBounds(tempDestination)) {
                    break;
                }
                if(hasPieceInPathToDestination(destinationsPosition, tempDestination)) {
                    break;
                }
                if(destinationsPosition.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())){
                    return true;
                }
            }
        }
        if(destinationsPosition.y == origin.y) {
            direction = getDirection(origin.x, destinationsPosition.x);
            while (tempDestination.x != destinationsPosition.x) {
                tempDestination.x += direction;
                try {
                    if(!isInBounds(tempDestination)) {
                        break;
                    }
                } catch (IllegalMoveException ime) {
                    if(isOutOfBounds(tempDestination, ime)) break;
                    throw ime;
                }
                if(hasPieceInPathToDestination(destinationsPosition, tempDestination)) {
                    break;
                }
                if(destinationsPosition.equals(tempDestination) && (canMoveThere(tempDestination, getPieceColor()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOutOfBounds(Point tempDestination, IllegalMoveException ime) {
        return ime.getMessage().contains(String.format("x = %d y = %d out of bounds", tempDestination.x, tempDestination.y));
    }

    private int getDirection(int origin, int destination) {
        return (Math.min(origin, destination) ==  origin)? 1: -1;
    }

    @Override
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        for (int x = piecePosition.x + 1; x < SUM_OF_COLUMNS; x++) {
            temp = new Point(x, piecePosition.y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int x = piecePosition.x - 1; x >= 0; x--) {
            temp = new Point(x, piecePosition.y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int y = piecePosition.y + 1; y < SUM_OF_ROWS; y++) {
            temp = new Point(piecePosition.x, y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int y = piecePosition.y - 1; y >= 0; y--) {
            temp = new Point(piecePosition.x, y);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        return legalMoves;
    }

    public boolean getHasMoved() {
        return moved;
    }

    public void moved() {
        moved = true;
    }
}
