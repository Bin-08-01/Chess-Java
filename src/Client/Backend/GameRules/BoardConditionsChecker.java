package Client.Backend.GameRules;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Board.Board;
import Client.Backend.GameObjects.Pieces.PieceColor;

import java.awt.*;

import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;
import static Client.GameManagers.ChessGame.SUM_OF_ROWS;

public abstract class BoardConditionsChecker {

    public static boolean isInBounds(Point position) throws IllegalMoveException {
        if(position.x < SUM_OF_COLUMNS && position.x >= 0 && position.y < SUM_OF_ROWS && position.y >= 0) {
            return true;
        }
        throw new IllegalMoveException(String.format("x = %d y = %d out of bounds", position.x, position.y));
    }

    public static boolean isVacantPosition(Point position, Board board) {
        return !board.hasPieceInThisPosition(position);
    }

    public static boolean hasEnemyPiece(PieceColor playersColor, Point destination, Board board) {
        if (!isVacantPosition(destination, board)) {
            try {
                return playersColor != board.getPiece(destination).getPieceColor();
            } catch (NoPieceFoundException e) {
                return false;
            }
        }
        return false;
    }



}
