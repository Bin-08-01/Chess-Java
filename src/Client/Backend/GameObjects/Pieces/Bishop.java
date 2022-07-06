package Client.Backend.GameObjects.Pieces;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static Client.Backend.GameRules.BoardConditionsChecker.*;

public class Bishop extends Piece implements PieceMoves {

    public Bishop(PieceColor color) {
        super(PieceType.BISHOP, color);
    }

    @Override
    public Piece getClone() {
        return new Bishop(getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return getPieceType().toString();
    }

    @Override
    public boolean isLegalMove(Point origin, Point destinationsPosition, Board board) throws IllegalMoveException {
        if(movingInStraightLine(origin, destinationsPosition)) return false;
        this.board = board;
        Point tempDestination = new Point(origin);
        Point direction = getDirection(origin, destinationsPosition);
        while (!tempDestination.equals(destinationsPosition)) {
            tempDestination.x += direction.x;
            tempDestination.y += direction.y;
            if(!isInBounds(tempDestination)) {
                return false;
            }
            if(hasPieceInPathToDestination(destinationsPosition, tempDestination)) {
                return false;
            }
            if(destinationsPosition.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())) {
                return true;
            }
        }
        return false;
    }

    private boolean movingInStraightLine(Point origin, Point destination) {
        return origin.x == destination.x || origin.y == destination.y;
    }

    private Point getDirection(Point origin, Point destination) {
        Point direction = new Point();
        direction.x = (Math.min(origin.x, destination.x) == origin.x)? 1 : -1;
        direction.y = (Math.min(origin.y, destination.y) == origin.y)? 1 : -1;
        return direction;
    }

    @Override
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        final int SUM_OF_LEGAL_MOVES = 13;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        // the loops are separated because if one move is illegal in any direction we want to stop looking in that direction
        // but that doesn't mean it's illegal in the other directions
        for (int i = 1; i <= 7; i++) {
            temp = new Point(piecePosition.x + i, piecePosition.y + i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int i = 1; i <= 7; i++) {
            temp = new Point(piecePosition.x - i, piecePosition.y + i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int i = 1; i <= 7; i++) {
            temp = new Point(piecePosition.x + i, piecePosition.y - i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int i = 1; i <= 7; i++) {
            if (legalMoves.size() >= SUM_OF_LEGAL_MOVES) break;
            temp = new Point(piecePosition.x - i, piecePosition.y - i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        return legalMoves;
    }

}
