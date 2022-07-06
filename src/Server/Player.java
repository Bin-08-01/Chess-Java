package Server;

import Client.Backend.GameObjects.Pieces.PieceColor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class Player {

    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;

    public Player(Socket socket, PieceColor playersColor) throws IOException {
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream.writeObject(playersColor.toString());
    }

    public Object getMove() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }

    public void sendMove(Object msg) throws IOException {
        outputStream.writeObject(msg);
    }

}
