package Client.Backend.GameObjects.Pieces;

import Client.Backend.GameObjects.Board.Board;
import java.awt.*;
import java.util.List;

interface PieceMoves {

    List<Point> getLegalMoves(Board board, Point piecePosition);

}
