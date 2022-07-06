package Client.Backend.GameObjects.Board;

import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Pieces.*;
import java.awt.*;
import java.util.List;

public class Board extends ChessBoard {

    public Board() {
        board = BoardBuilder.newBoard();
        try {
            whiteKing = (King) board[7][4].getPiece();
            blackKing = (King) board[0][4].getPiece();
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
        setContext(this);
    }

    private Board(Tile[][] costumeBoard) {
        this.board = costumeBoard;
        setKings(Board.this);
        setContext(this);
    }

    public static Board getConsumeBoard(List<Piece> piecesChanged, List<Point> piecePositions, List<Point> emptyTiles) {
        Board board = new Board(BoardBuilder.costumeBoard(piecesChanged, piecePositions, emptyTiles));
        setKings(board);
        return board;
    }

    public static Board getClone(Board board) {
        Board clone = new Board(BoardBuilder.clone(board));
        setKings(clone);
        return clone;
    }

    public Tile[][] getBoard() {
        return board;
    }

    public void updateTile(Point position, Piece piece) {
        board[position.y][position.x].setPiece(piece);
    }

    @Override
    public String toString() {
        StringBuilder bo = new StringBuilder();
        String line = "\n----------------------------------------------------------------------------\n";
        bo.append(line);
        for (Tile[] tiles: board) {
            bo.append("|");
            for (Tile tile: tiles) {
                bo.append(tile.toString());
            }
            bo.append(line);
        }
        return bo.toString();
    }

}
