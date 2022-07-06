package Client;

import Client.Backend.GameObjects.Pieces.PieceColor;
import Client.Backend.Players.Enemy;
import Client.GameManagers.MultiPlayerChessGame;
import Client.GameManagers.SinglePlayerChessGame;
import Server.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;

public class Client {

    public static void startSinglePlayerGame(PieceColor pieceColor) {
        if(pieceColor == PieceColor.NULL){
            Random random = new Random();
            new SinglePlayerChessGame(PieceColor.values()[random.nextInt(PieceColor.values().length)]);
        }else {
            new SinglePlayerChessGame(pieceColor);
        }
    }

    public static void startMultiplayerGame(int PORT) throws IOException, ClassNotFoundException {
        Socket socket;
        PieceColor playersColor;
        Enemy enemy;
        socket = new Socket(InetAddress.getLocalHost(), PORT);
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        playersColor = PieceColor.valueOf((String) inputStream.readObject());
        enemy = new Enemy(getEnemyColor(playersColor), inputStream, outputStream);
        new MultiPlayerChessGame(playersColor, enemy);
    }

    private static PieceColor getEnemyColor(PieceColor playersColor) {
        return (playersColor == PieceColor.WHITE)? PieceColor.BLACK: PieceColor.WHITE;
    }

}
