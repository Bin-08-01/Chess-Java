package Client.Backend.GameObjects.Pieces;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Board.Board;
import Client.Backend.GameRules.CheckChecker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static Client.Backend.GameRules.BoardConditionsChecker.*;
import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;

public class King extends Piece implements PieceMoves {

    private final int STARTING_COLUMN;
    private boolean wasInCheck;
    private boolean moved;

    public King(int startingX, PieceColor color) {
        super(PieceType.KING, color);
        wasInCheck = false;
        moved = false;
        STARTING_COLUMN = startingX;
    }

    @Override
    public Piece getClone() {
        King king = new King(STARTING_COLUMN, getPieceColor());
        king.wasInCheck = this.wasInCheck;
        king.moved = this.moved;
        return king;
    }

    @Override
    public String getPieceInString() {
        return " " + getPieceType() + " ";
    }

    public boolean isInCheck(Board board, int depth) {
        CheckChecker checkChecker = new CheckChecker(depth, getPieceColor(), board);
        return checkChecker.isInCheck();
    }

    @Override
    public boolean isLegalMove(Point origin, Point destination, Board board) throws IllegalMoveException {
        this.board = board;
        Point tempDestination = new Point(origin);
        Point direction = getDirection(origin, destination);
        tempDestination.x += direction.x;
        tempDestination.y += direction.y;
        if(!isInBounds(tempDestination)) {
            return false;
        }
        return (destination.equals(tempDestination) && canMoveThere(tempDestination, getPieceColor())) || isCastlingMove(origin, destination);
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

    private boolean isCastlingMove(Point origin, Point destination) {
        if (moved || wasInCheck || origin.x != STARTING_COLUMN || destination.x != 6 && destination.x != 1) return false;
        int rookX;
        if(destination.x < origin.x) {
            rookX = 0;
        } else {
            rookX = 7;
        }
        try {
            Piece piece = board.getPiece(new Point(rookX, origin.y));
            if(piece instanceof Rook) {
                if (piece.getPieceColor() != getPieceColor() || ((Rook) piece).getHasMoved()) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (NoPieceFoundException e) {
            return false;
        }
        Board isInCheckTesterBoard;
        switch (destination.x) {
            case 1:
                for (int x = origin.x - 1; x > 0; x--) {
                    isInCheckTesterBoard = Board.getClone(board);
                    isInCheckTesterBoard.updateTile(new Point(x, destination.y), this);
                    isInCheckTesterBoard.updateTile(origin, null);
                    if(board.hasPieceInThisPosition(new Point(x, origin.y)) || isInCheck(isInCheckTesterBoard, 1)) {
                        return false;
                    }
                }
                break;
            case 6:
                for (int x = origin.x + 1; x < SUM_OF_COLUMNS - 1; x++) {
                    isInCheckTesterBoard = Board.getClone(board);
                    isInCheckTesterBoard.updateTile(new Point(x, destination.y), this);
                    isInCheckTesterBoard.updateTile(origin, null);
                    if(board.hasPieceInThisPosition(new Point(x, origin.y)) || isInCheck(isInCheckTesterBoard, 1)) {
                        return false;
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public List<Point> getLegalMoves(Board board, Point piecePosition) {
        this.board = board;
        List<Point> legalMoves = new ArrayList<>();
        Point temp;
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if(y == 0 && x == 0) continue;// king original position
                temp = new Point(piecePosition.x + x, piecePosition.y + y);
                if(shouldAddPositionToLegalMovesList(piecePosition, temp)) {
                    legalMoves.add(temp);
                }
            }
        }
        if(shouldAddPositionToLegalMovesList(piecePosition, new Point(1, piecePosition.y))) {
            legalMoves.add(new Point(1, piecePosition.y));
        }
        if(shouldAddPositionToLegalMovesList(piecePosition, new Point(6, piecePosition.y))) {
            legalMoves.add(new Point(6, piecePosition.y));
        }
        return legalMoves;
    }

    public void moved() {
        moved = true;
    }

    public void setToIsInCheck() {
        wasInCheck = true;
    }

}

