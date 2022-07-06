package Client.GameManagers;



import Client.Backend.Exceptions.IllegalMoveException;
import Client.Backend.Exceptions.NoPieceFoundException;
import Client.Backend.GameObjects.Board.Board;
import Client.Backend.GameObjects.Move;
import Client.Backend.GameObjects.Pieces.*;
import Client.Backend.Players.Player;
import Client.Backend.Players.Positions;
import Client.Frontend.GraphicBoard;
import Client.Frontend.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class ChessGame {

    public static final int SUM_OF_ROWS = 8;
    public static final int SUM_OF_COLUMNS = 8;
    public static final CharSequence TIE_MESSAGE = "tie";
    protected Board backendBoard;
    protected GraphicBoard onScreenBoard;
    protected List<Move> moves;
    protected Player player;
    protected Player currentPlayer;
    protected PieceType promotedPieceType;
    protected boolean isPromoting;

    public abstract void move(Positions positions, Player player);

    public void tileClicked(Point tilePosition) {
        player.addPositionToMove(tilePosition);
        try {
            if(backendBoard.getPiece(tilePosition).getPieceColor() == currentPlayer.getPlayersColor()) {
                onScreenBoard.resetTilesColor();
                onScreenBoard.drawLegalTiles(backendBoard.getPiece(tilePosition).getLegalMoves(backendBoard, tilePosition));
            } else {
                onScreenBoard.resetTilesColor();
            }
        } catch (NoPieceFoundException e) {
            onScreenBoard.resetTilesColor();
        }
    }

    protected boolean enemyKingIsInCheck(Point movedPiecePosition) {
        try {
            return backendBoard.isLegalMove(movedPiecePosition, backendBoard.getKingPosition(backendBoard.getPiece(movedPiecePosition).getPieceColor()), 1);
        } catch (IllegalMoveException | NoPieceFoundException e) {
            return false;
        }
    }

    protected void putKingInCheck(Player currentPlayer) {
        King playersKing = backendBoard.getKing(currentPlayer.getPlayersColor());
        playersKing.setToIsInCheck();
    }

    protected void addMoveToMoveList(Positions positions) {
        Piece piece;
        try {
            piece = backendBoard.getPiece(positions.getDestination());
        } catch (NoPieceFoundException e) {
            piece = null;
        }
        moves.add(new Move(positions, piece));
    }

    protected void updateBoards(Positions positions) {
        Piece piece = getPiece(positions.getOrigin());
        assert piece != null;
        if(piece instanceof King) {
            ((King) piece).moved();
        } else if(piece instanceof Rook) {
            ((Rook) piece).moved();
        }
        backendBoard.updateTile(positions.getOrigin(), null);
        backendBoard.updateTile(positions.getDestination(), piece);
        onScreenBoard.updateTile(positions.getDestination(), piece.getPieceType(), piece.getPieceColor());
        onScreenBoard.updateTile(positions.getOrigin(), null, null);
    }

    protected void updateEnpassantData(Positions positions) {
        try {
            Piece piece = backendBoard.getPiece(positions.getDestination());
            if(piece instanceof Pawn) {
                if(Math.abs(positions.getOrigin().y - positions.getDestination().y) == 2) {
                    backendBoard.setEnpassant(piece.getPieceColor(), positions.getDestination().x);
                }
            }
            backendBoard.resetOpponentsEnpassant(piece.getPieceColor());
        } catch (NoPieceFoundException e) {
            e.printStackTrace();
        }
    }

    protected boolean isLegalMove(Positions positions) {
        try {
            if(currentPlayer.getPlayersColor() != backendBoard.getPiece(positions.getOrigin()).getPieceColor()) return false;
            int STARTING_DEPTH = 1;
            return backendBoard.isLegalMove(positions.getOrigin(), positions.getDestination(), STARTING_DEPTH);
        } catch (IllegalMoveException | NoPieceFoundException e) {
            return false;
        }
    }

    protected boolean gameIsOver(PieceColor playersColor, Point destination) {
        List<Point> enemyPiecesPositions = getPiecePositions(getOtherPLayersColor(playersColor));
        for (Point enemyPiecePosition: enemyPiecesPositions) {
            Piece piece = getPiece(enemyPiecePosition);
            assert piece != null;
            if(piece.getLegalMoves(backendBoard, enemyPiecePosition).size() != 0) {
                return false;
            }
        }
        return true;
    }

    protected boolean gameIsWon(PieceColor playersColor) {
        Point enemyKingPosition = getEnemyKingPosition(playersColor);
        List<Point> currentPlayersPiecePositions = getPiecePositions(playersColor);
        for (Point piecePosition: currentPlayersPiecePositions) {
            Piece piece = getPiece(piecePosition);
            assert piece != null;
            try {
                if(piece.isLegalMove(piecePosition, enemyKingPosition, backendBoard)) {
                    return true;
                }
            } catch (IllegalMoveException ignored) {}
        }

        return false;
    }

    private PieceColor getOtherPLayersColor(PieceColor playersColor) {
        return (playersColor == PieceColor.BLACK)? PieceColor.WHITE: PieceColor.BLACK;
    }

    private Point getEnemyKingPosition(PieceColor playersColor) {
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                Piece piece = getPiece(new Point(x, y));
                if (piece instanceof King && piece.getPieceColor() != playersColor) {
                    return new Point(x, y);
                }
            }
        }
        throw new Error("no king found " + playersColor);
    }

    private List<Point> getPiecePositions(PieceColor playersColor) {
        final int MAX_SUM_OF_PIECES = 16;
        List<Point> pieces = new ArrayList<>();
        for (int y = 0; y < SUM_OF_ROWS; y++) {
            for (int x = 0; x < SUM_OF_COLUMNS; x++) {
                if(pieces.size() == MAX_SUM_OF_PIECES) {
                    return pieces;
                }
                Piece piece = getPiece(new Point(x, y));
                if (piece != null && piece.getPieceColor() == playersColor) {
                    pieces.add(new Point(x, y));
                }
            }
        }
        return pieces;
    }

    public void endGameWithWinner(PieceColor playersColor) {
        //todo make popup window;
        System.out.println("Game Over\n" + playersColor.toString().toLowerCase() + " player Won!!!!!!!");
        JOptionPane.showMessageDialog(null, "Game Over! "+playersColor.toString().toLowerCase()+ " win");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {}
        System.exit(0);
    }

    public void endGameWithoutWinner() {
        //todo make popup window
        System.out.println("stale mate :(");
        JOptionPane.showMessageDialog(null, "Draw");
        System.exit(0);
    }

    public Piece getPiece(Point position) {
        try {
            return backendBoard.getPiece(position);
        } catch (NoPieceFoundException e) {
            return null;
        }
    }

    public PieceColor getPlayersColor() {
        return player.getPlayersColor();
    }

    public void promotionClick(PieceType pieceType) {
        promotedPieceType = pieceType;
    }

}

