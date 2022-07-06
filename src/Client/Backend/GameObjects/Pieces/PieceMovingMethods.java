package Client.Backend.GameObjects.Pieces;

import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.util.List;
import static Client.Backend.GameRules.BoardConditionsChecker.*;

abstract class PieceMovingMethods {

    protected Board board;

    protected boolean canMoveThere(Point tempDestination, PieceColor playersColor) {
        return isVacantPosition(tempDestination, board) || hasEnemyPiece(playersColor, tempDestination, board);
    }

    protected boolean hasPieceInPathToDestination(Point destination, Point tempDestination) {
        return !destination.equals(tempDestination) && !isVacantPosition(tempDestination, board);
    }

    protected boolean addedDestinationToLegalMovesList(List<Point> legalMoves, Point origin, Point destination) {
        if(shouldAddPositionToLegalMovesList(origin, destination)) {
            legalMoves.add(destination);
            return true;
        }
        return false;
    }

    protected boolean shouldAddPositionToLegalMovesList(Point origin, Point destination) {
        try {
            if (isInBounds(destination)) {
                return board.isLegalMove(origin, destination, 1);
            }
        } catch (IllegalMoveException | NoPieceFoundException e) {
            return false;
        }
        return false;
    }

}
