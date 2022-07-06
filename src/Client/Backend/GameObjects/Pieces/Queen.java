package Client.Backend.GameObjects.Pieces;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.GameObjects.Board.Board;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Client.Backend.GameRules.BoardConditionsChecker.*;
import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static Client.GameManagers.ChessGame.SUM_OF_ROWS;

public class Queen extends Piece implements PieceMoves {

    public Queen(PieceColor color) {
        super(PieceType.QUEEN, color);
    }

    @Override
    public Piece getClone() {
        return new Queen(getPieceColor());
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType();
    }

    @Override
    public boolean isLegalMove(Point origin, Point destination, Board board) throws IllegalMoveException {
        this.board = board;
        Point tempDestination = new Point(origin);
        Point direction = getDirection(origin, destination);
        while (!tempDestination.equals(destination)) {
            tempDestination.x += direction.x;
            tempDestination.y += direction.y;
            if(!isInBounds(tempDestination)) {
                break;
            }
            if(hasPieceInPathToDestination(destination, tempDestination)) {
                break;
            }
            if(destination.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())) {
                return true;
            }
        }
        return false;
    }

    private Point getDirection(Point origin, Point destination) {
        Point direction = new Point();
        if(origin.x == destination.x) {
            direction.x = 0;
        } else {
            direction.x = (Math.min(origin.x, destination.x) == origin.x)? 1 : -1;
        }
        if (origin.y == destination.y) {
            direction.y = 0;
        } else {
            direction.y = (Math.min(origin.y, destination.y) == origin.y)? 1 : -1;
        }
        return direction;
    }

    @Override
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        final int SUM_OF_LEGAL_MOVES = 27;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        // straight
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
        // diagonal
        for (int i = 1; i <= 4; i++) {
            temp = new Point(piecePosition.x + i, piecePosition.y + i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int i = 1; i <= 4; i++) {
            temp = new Point(piecePosition.x - i, piecePosition.y + i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int i = 1; i <= 4; i++) {
            temp = new Point(piecePosition.x + i, piecePosition.y - i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        for (int i = 1; i <= 4; i++) {
            if (legalMoves.size() >= SUM_OF_LEGAL_MOVES) break;
            temp = new Point(piecePosition.x - i, piecePosition.y - i);
            if(!addedDestinationToLegalMovesList(legalMoves, piecePosition, temp)) {
                break;
            }
        }
        return legalMoves;
    }
}
