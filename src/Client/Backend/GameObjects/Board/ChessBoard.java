package Client.Backend.GameObjects.Board;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Pieces.King;
import Client.Backend.GameObjects.Pieces.Piece;
import Client.Backend.GameObjects.Pieces.PieceColor;
import java.awt.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static Client.GameManagers.ChessGame.SUM_OF_ROWS;

public abstract class ChessBoard {

    protected Tile[][] board;
    protected King whiteKing;
    protected King blackKing;
    private Board backendBoard;
    private final boolean[] whiteEnpassant;
    private final boolean[] blackEnpassant;

    public ChessBoard() {
        whiteEnpassant = new boolean[SUM_OF_COLUMNS];
        blackEnpassant = new boolean[SUM_OF_COLUMNS];
    }

    public boolean isLegalMove(Point origin, Point destination, int depth) throws IllegalMoveException, NoPieceFoundException {
        if(origin.equals(destination)) return false;
        Piece piece = board[origin.y][origin.x].getPiece();
        if(!piece.isLegalMove(origin, destination, backendBoard)) {
            return false;
        }
        boolean isInCheck = isInCheck(piece, origin, destination, depth);
        return !isInCheck;
    }

    public boolean hasPieceInThisPosition(Point position) {
        try {
            board[position.y][position.x].getPiece();
            return true;
        } catch (NoPieceFoundException ignore) {
            return false;
        }

    }

    public Piece getPiece(Point position) throws NoPieceFoundException {
        return board[position.y][position.x].getPiece();
    }

    public King getKing(PieceColor pieceColor) {
        return (pieceColor == PieceColor.BLACK)? blackKing: whiteKing;
    }

    public Point getKingPosition(PieceColor kingColor) {
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = getPiece(new Point(x, y));
                    if(piece.equals(getKing(kingColor))) {
                        return new Point(x, y);
                    }
                } catch (NoPieceFoundException ignored) {}
            }
        }
        throw new NoSuchElementException(kingColor.toString());
    }

    public boolean getEnpassant(PieceColor playersColor, int index) {
        return (playersColor == PieceColor.BLACK)? blackEnpassant[index]: whiteEnpassant[index];
    }

    public void setEnpassant(PieceColor playersColor, int index) {
        if(playersColor == PieceColor.BLACK) {
            blackEnpassant[index] = true;
        } else {
            whiteEnpassant[index] = true;
        }
    }

    public void resetOpponentsEnpassant(PieceColor pieceColor) {
        if(pieceColor == PieceColor.WHITE) {
            Arrays.fill(blackEnpassant, false);
        } else {
            Arrays.fill(whiteEnpassant, false);
        }
    }

    protected static void setKings(Board board) {
        try {
            Point whiteKingPosition = getKingPosition(PieceColor.WHITE, board.board);
            board.whiteKing = (King) board.getPiece(whiteKingPosition);
        } catch (NoSuchElementException | NoPieceFoundException e) {
            board.whiteKing = null;
        }
        try {
            Point blackKingPosition = getKingPosition(PieceColor.BLACK, board.board);
            board.blackKing = (King) board.getPiece(blackKingPosition);
        } catch (NoSuchElementException | NoPieceFoundException e) {
            board.blackKing = null;
        }
    }

    protected boolean isInCheck(Piece piece, Point origin, Point destination, int depth) {
        boolean isInCheck;
        Piece oldPiece;
        try {
            oldPiece = board[destination.y][destination.x].getPiece();
        } catch (NoPieceFoundException e) {
            oldPiece = null;
        }
        board[destination.y][destination.x].setPiece(piece);
        board[origin.y][origin.x].setPiece(null);
        King king = getKing(piece.getPieceColor());
        if(king == null) return true;
        isInCheck = king.isInCheck(backendBoard, depth);
        board[origin.y][origin.x].setPiece(piece);
        board[destination.y][destination.x].setPiece(oldPiece);
        return isInCheck;
    }

    protected static Point getKingPosition(PieceColor kingColor, Tile[][] board) {
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                if(board[y][x].hasPiece()) {
                    try {
                        Piece piece = board[y][x].getPiece();
                        if(piece instanceof King && piece.getPieceColor() == kingColor) {
                            return new Point(x,y);
                        }
                    } catch (NoPieceFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        throw new NoSuchElementException("no king of color " + kingColor + " found");
    }

    protected void setContext(Board backendBoard) {
        this.backendBoard = backendBoard;
    }

}
