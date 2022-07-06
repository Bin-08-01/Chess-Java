package Client.Backend.GameRules;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Board.Board;
import Client.Backend.GameObjects.Pieces.King;
import Client.Backend.GameObjects.Pieces.Piece;
import Client.Backend.GameObjects.Pieces.PieceColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static Client.GameManagers.ChessGame.SUM_OF_ROWS;

public class CheckChecker {

    private static final int MAX_DEPTH = 3;
    private final int DEPTH;
    private final PieceColor PLAYERS_COLOR;
    private final Board BOARD;

    public CheckChecker(int depth, PieceColor playersColor, Board board) {
        this.DEPTH = depth;
        this.PLAYERS_COLOR = playersColor;
        this.BOARD = Board.getClone(board);
    }

    public boolean isInCheck() {
        List<Point> enemyPiecePositions = getEnemyPiecePositions();
        Point kingPosition = getKingPosition(PLAYERS_COLOR);
        for (Point enemyPiecePosition: enemyPiecePositions) {
            try {
                Piece piece = BOARD.getPiece(enemyPiecePosition);
                assert piece != null;
                if(DEPTH < MAX_DEPTH) {
                    if(BOARD.isLegalMove(enemyPiecePosition, kingPosition, DEPTH + 1)) {
                        return true;
                    }
                }
            } catch (NoPieceFoundException | IllegalMoveException ignored) {}
        }
        return false;
    }

    private Point getKingPosition(PieceColor kingColor) {
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = BOARD.getPiece(new Point(x, y));
                    if(piece instanceof King && piece.getPieceColor() == kingColor) {
                        return new Point(x, y);
                    }
                } catch (NoPieceFoundException ignored) {}
            }
        }
        throw new NoSuchElementException(PLAYERS_COLOR.toString());
    }

    private List<Point> getEnemyPiecePositions() {
        List<Point> positions = new ArrayList<>();
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = BOARD.getPiece(new Point(x, y));
                    if(piece.getPieceColor() != PLAYERS_COLOR) {
                        positions.add(new Point(x, y));
                    }
                } catch (NoPieceFoundException ignored) {}
            }
        }
        return positions;
    }

}
