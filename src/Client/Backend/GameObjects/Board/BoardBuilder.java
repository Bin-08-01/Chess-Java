package Client.Backend.GameObjects.Board;

import Client.Backend.GameObjects.Pieces.*;

import java.awt.*;
import java.util.List;

import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static Client.GameManagers.ChessGame.SUM_OF_ROWS;

public abstract class BoardBuilder {

    private static Tile[][] board;

    public static Tile[][] newBoard() {
        initializeNewBoard();
        return board;
    }

    public static Tile[][] costumeBoard(List<Piece> pieces, List<Point> piecePositions, List<Point> emptyTiles) {
        setBoard();
        initializeCostumeBoard(emptyTiles, piecePositions, pieces);
        return board;
    }

    public static Tile[][] clone(Board oldBoard) {
        board = oldBoard.board;
        board = getClone(board);
        return board;
    }

    private static Tile[][] getClone(Tile[][] board) {
        Tile[][] tempBoard = new Tile[SUM_OF_ROWS][SUM_OF_COLUMNS];
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                tempBoard[y][x] = board[y][x].getClone();
            }
        }
        return tempBoard;
    }

    private static void initializeNewBoard() {
        Tile tile;
        Point position;
        setBoard();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                position = new Point(x, y);
                tile = new Tile(position);
                if(isAddingAPieceToNewBoard(y)) {
                    tile.setPiece(getPieceForNewBoard(position, getPieceColorForNewBoard(y)));
                }
                board[y][x] = tile;
            }
        }
    }

    private static void initializeCostumeBoard(List<Point> emptyTiles, List<Point> piecePositions, List<Piece> pieces) {
        initializeNewBoard();
        removeEmptyTiles(emptyTiles);
        addNewPiece(pieces, piecePositions);
    }

    private static void removeEmptyTiles(List<Point> emptyTiles) {
        for (Tile[] tiles: board) {
            for(Tile tile: tiles) {
                for (Point emptyTile : emptyTiles) {
                    if (tile.getPosition().equals(emptyTile)) {
                        tile.setPiece(null);
                    }
                }
            }
        }
    }

    private static void addNewPiece(List<Piece> pieces, List<Point> piecePositions) {
        assert pieces.size() == piecePositions.size();
        for (int i = 0; i < pieces.size(); i++) {
            board[piecePositions.get(i).y][piecePositions.get(i).x].setPiece(pieces.get(i));
        }
    }

    private static void setBoard() {
        board = new Tile[SUM_OF_ROWS][SUM_OF_COLUMNS];
    }

    private static boolean isAddingAPieceToNewBoard(int row) {
        return row != 2 && row != 3 && row != 4 && row != 5;
    }

    private static PieceColor getPieceColorForNewBoard(int row) {
        return (row == 0 || row == 1) ? PieceColor.BLACK : PieceColor.WHITE;
    }

    private static Piece getPieceForNewBoard(Point position, PieceColor color) {
        if(position.y == 1 || position.y == 6) {
            return new Pawn(color);
        } else {
            switch (position.x) {
                case 0:
                case 7:
                    return new Rook(color);
                case 1:
                case 6:
                    return new Knight(color);
                case 2:
                case 5:
                    return new Bishop(color);
                case 3:
                    return new Queen(color);
                case 4:
                    return new King(position.x, color);
                default:
                    throw new Error("no piece matches row " + position.y + " column " + position.x);
            }
        }
    }

}
