package Client.Backend.GameObjects.Pieces;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static Client.Backend.GameRules.BoardConditionsChecker.isInBounds;

public class Knight extends Piece implements PieceMoves{

    private final List<Point> possibleDirections;

    public Knight(PieceColor color) {
        super(PieceType.KNIGHT, color);
        possibleDirections = getPossibleDirections();
    }

    @Override
    public Piece getClone() {
        return new Knight(getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return getPieceType().toString();
    }

    @Override
    public boolean isLegalMove(Point origin, Point destination, Board board) throws IllegalMoveException {
        this.board = board;
        Point tempDestination;
        if(isLegalDistance(origin, destination)) {
            for (Point direction: possibleDirections) {
                tempDestination = new Point(origin);
                tempDestination.x += direction.x;
                tempDestination.y += direction.y;
                try {
                    if (isInBounds(tempDestination) && destination.equals(tempDestination)) {
                        return canMoveThere(tempDestination, getPieceColor());
                    }
                } catch (IllegalMoveException ime) {
                    if (!ime.getMessage().contains(String.format("x = %d y = %d out of bounds", tempDestination.x, tempDestination.y))) {
                        throw ime;
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalDistance(Point origin, Point destination) {
        return (Math.abs(destination.x - origin.x) == 1 && Math.abs(destination.y - origin.y) == 2) || (Math.abs(destination.x - origin.x) == 2 && Math.abs(destination.y - origin.y) == 1);
    }

    private List<Point> getPossibleDirections() {
        List<Point> possiblePositions = new ArrayList<>();
        int [] half1 = {1,-1};
        int [] half2 = {2,-2};
        for (int i : half1) {
            for (int j : half2) {
                possiblePositions.add(new Point(j, i));
                possiblePositions.add(new Point(i, j));
            }
        }
        return possiblePositions;
    }

    @Override
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        int x = piecePosition.x;
        int y = piecePosition.y;
        for (Point possibleDirection: possibleDirections) {
            temp = new Point(x + possibleDirection.x, y + possibleDirection.y);
            if(shouldAddPositionToLegalMovesList(new Point(x, y), temp)) {
                legalMoves.add(temp);
            }
        }
        return legalMoves;
    }

}
