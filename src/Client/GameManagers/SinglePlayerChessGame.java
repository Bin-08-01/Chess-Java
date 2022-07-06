package Client.GameManagers;

import Client.Backend.GameObjects.Board.Board;
import Client.Backend.GameObjects.Pieces.*;
import Client.Backend.Players.Ai;
import Client.Backend.Players.Player;
import Client.Backend.Players.Positions;
import Client.Frontend.GraphicBoard;
import Client.Frontend.PawnPromotionWindow;
import java.awt.*;
import java.util.ArrayList;

import static Client.Backend.GameRules.SpecialMoves.*;

public class SinglePlayerChessGame extends ChessGame {

    private final Ai ai;

    public SinglePlayerChessGame(PieceColor playersColor) {
        backendBoard = new Board();
        moves = new ArrayList<>();
        player = new Player(playersColor);
        player.setContext(this);
        ai = new Ai(getOtherColor(playersColor), this);
        if(playersColor == PieceColor.WHITE) {
            currentPlayer = player;
        } else {
            currentPlayer = ai;
        }
        onScreenBoard = new GraphicBoard(backendBoard, this);
        if(currentPlayer.equals(ai)) {
            ai.makeAMove(backendBoard);
        }
    }

    @Override
    public void tileClicked(Point tilePosition) {
        if(currentPlayer.equals(player)) {
            super.tileClicked(tilePosition);
        }
    }

    @Override
    public void move(Positions positions, Player player) {
        assert currentPlayer.equals(player);
        if(isLegalMove(positions)) {
            addMoveToMoveList(positions);
            updateBoards(positions);
            handleSpecialMoves(positions);
            if (gameIsOver(positions.getPlayersColor(), positions.getDestination())) {
                if(gameIsWon(positions.getPlayersColor())) {
                    endGameWithWinner(positions.getPlayersColor());
                } else {
                    endGameWithoutWinner();
                }
            }
            changeCurrentPlayer();
            if (enemyKingIsInCheck(positions.getDestination())) {
                putKingInCheck(currentPlayer);
            }
            if(currentPlayer.equals(ai)) {
                Thread aiMove = new Thread(this::makeAiMove);
                aiMove.start();
            }
        }
    }

    private void makeAiMove() {
        if(currentPlayer.equals(ai)) {
            ai.makeAMove(backendBoard);
        }
    }

    private void changeCurrentPlayer() {
        if(currentPlayer == player) {
            currentPlayer = ai;
        } else {
            currentPlayer = player;
        }
    }

    private void handleSpecialMoves(Positions positions) {
        updateEnpassantData(positions);
        isPromoting = false;
        if(wasEnpassant(backendBoard, moves.get(moves.size() - 1).getPositions().getOrigin(), moves.get(moves.size() - 1).getPositions().getDestination(), moves.get(moves.size() - 1).getPieceAtDestination())) {
            backendBoard.updateTile(new Point(positions.getDestination().x, positions.getOrigin().y), null);
            onScreenBoard.updateTile(new Point(positions.getDestination().x, positions.getOrigin().y), null, null);
        } else if(wasCastling(backendBoard, positions)) {
            int y = positions.getDestination().y;
            Piece rook;
            int originalX;
            int newX;
            if(positions.getDestination().x == 1) {
                originalX = 0;
                newX = 2;
            } else {
                originalX = 7;
                newX = 5;
            }
            rook = getPiece(new Point(originalX, positions.getDestination().y));
            assert rook != null;
            backendBoard.updateTile(new Point(newX, y), rook);
            backendBoard.updateTile(new Point(originalX, y), null);
            onScreenBoard.updateTile(new Point(newX, y), PieceType.ROOK, positions.getPlayersColor());
            onScreenBoard.updateTile(new Point(originalX, y), null, null);
        } else if(wasPromotion(backendBoard, positions.getDestination())) {
            Piece piece;
            Point promotionPosition = positions.getDestination();
            if(currentPlayer.equals(player)) {
                promotedPieceType = null;
                new PawnPromotionWindow(this, positions.getPlayersColor(), onScreenBoard.getFrame());
                assert promotedPieceType != null;
                switch (promotedPieceType) {
                    case ROOK:
                        piece = new Rook(positions.getPlayersColor());
                        break;
                    case QUEEN:
                        piece = new Queen(positions.getPlayersColor());
                        break;
                    case BISHOP:
                        piece = new Bishop(positions.getPlayersColor());
                        break;
                    case KNIGHT:
                        piece = new Knight(positions.getPlayersColor());
                        break;
                    default:
                        throw new Error("wrong type " + promotedPieceType);
                }
            } else {
                piece = new Queen(positions.getPlayersColor());
            }
            backendBoard.updateTile(promotionPosition, piece);
            onScreenBoard.updateTile(promotionPosition, piece.getPieceType(), piece.getPieceColor());
        }
    }

    private PieceColor getOtherColor(PieceColor playersColor) {
        return (playersColor == PieceColor.BLACK)? PieceColor.WHITE: PieceColor.BLACK;
    }

}
