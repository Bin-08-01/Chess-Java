package Client.Frontend;

import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Board.Board;
import Client.Backend.GameObjects.Pieces.Piece;
import Client.Backend.GameObjects.Pieces.PieceColor;
import Client.GameManagers.ChessGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import static Client.Frontend.GraphicBoard.TILE_SIZE;
import static Client.GameManagers.ChessGame.SUM_OF_ROWS;
import static Client.GameManagers.ChessGame.SUM_OF_COLUMNS;

public abstract class BoardBuilder {
    private static final Color white = new Color(242, 218, 182);
    private static final Color black = new Color(181, 137, 102);

    public static void updateGraphicsBoard(Board board, GraphicTile[][] graphicTiles) {
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                try {
                    Piece piece = board.getPiece(new Point(x,y));
                    graphicTiles[y][x].update(piece.getPieceType(), piece.getPieceColor());
                } catch (NoPieceFoundException ignore) {}
            }
        }
    }

    public static JFrame getChessBoardFrame(URL iconUrl) {
        JFrame frame = new JFrame("Chess");
        int windowWidth = TILE_SIZE * SUM_OF_ROWS;
        int windowHeight = TILE_SIZE * SUM_OF_ROWS;
        frame.setLayout(new GridLayout(SUM_OF_ROWS, SUM_OF_COLUMNS));
        frame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.setSize(windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        Image icon = null;
        try {
            icon = ImageIO.read(iconUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setIconImage(icon);
        return frame;
    }

    public static GraphicTile[][] getStartingBoard(Board board, ChessGame game) {
        GraphicTile[][] startingBoard = new GraphicTile[SUM_OF_ROWS][SUM_OF_COLUMNS];
        GraphicTile tile;
        Color color;
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                if((x + y) % 2 == 0) {
                    color = white;
                } else {
                    color = black;
                }
                tile = new GraphicTile(new Point(x,y), color, game);
                startingBoard[y][x] = tile;
            }
        }
        updateGraphicsBoard(board, startingBoard);
        return startingBoard;
    }

    public static void addTilesToFrame(PieceColor playersColor, JFrame frame, GraphicTile[][] board) {

        if(playersColor == PieceColor.WHITE) {
            for (int y = 0; y < SUM_OF_ROWS; y++) {
                for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                    frame.add(board[y][x]);
                }
            }
        } else {
            for (int y = SUM_OF_ROWS - 1; y >= 0; y--) {
                for (int x = SUM_OF_COLUMNS - 1; x >= 0; x--) {
                    frame.add(board[y][x]);
                }
            }
        }
    }

}
