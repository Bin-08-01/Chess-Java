package Server;

import Client.Backend.GameObjects.Pieces.PieceColor;
import Client.Backend.GameObjects.PromotionMessage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static Server.Server.runGame;

public class Server {

    public static final int PORT = 1007;

    private static Player[] players, players1, players2;
    private static Player currentPlayer;
    private static Player otherPlayer;
    private static boolean run;

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() throws IOException {
        excute();
    }

    public void excute() throws IOException {
        Servers servers = new Servers(1007);
        Servers servers1 = new Servers(1008);
        Servers servers2 = new Servers(1009);
        servers.start();
        servers2.start();
        servers1.start();
    }

//    public void runServer () throws IOException, ClassNotFoundException {
//        ServerSocket serverSocket;
//        Socket socket;
//        serverSocket = new ServerSocket(PORT);
//        players = new Player[2];
//        socket = serverSocket.accept();
//        players[0] = new Player(socket, PieceColor.WHITE);
//        socket = serverSocket.accept();
//        players[1] = new Player(socket, PieceColor.BLACK);
//        serverSocket.close();
//        runGame();
//    }

    static void runGame(Player[] players) throws IOException, ClassNotFoundException {
        run = true;
        System.out.println(players);
        currentPlayer = players[0];
        otherPlayer = players[1];
        do {
            Object msg = currentPlayer.getMove();
            if(msg instanceof PromotionMessage) {
                otherPlayer.sendMove(currentPlayer.getMove());
            }
//            else {
//                stop();
//            }
            otherPlayer.sendMove(msg);
            updatePlayers();
        } while (run);
    }

    private static void updatePlayers() {
        Player temp = currentPlayer;
        currentPlayer = otherPlayer;
        otherPlayer = temp;
    }

    private static void stop() {
        run = false;
    }

}

class Servers extends Thread{
    private int PORT;
    private Player[] players = new Player[2];
    private Player[] players1 = new Player[2];
    private Player[] players2 = new Player[2];
    private ServerSocket serverSocket;

    public Servers(int PORT) throws IOException {
        this.PORT = PORT;
        this.serverSocket = new ServerSocket(PORT);
    }

    @Override
    public void run() {
        System.out.println("Server " + PORT + "is running ...");
        System.out.println(serverSocket);
        try {
            Socket socket = null;
            if(PORT==1007){
                System.out.println("connect" + PORT);
                System.out.println(socket);
                socket = serverSocket.accept();
                players[0] = new Player(socket, PieceColor.WHITE);
                while (true){
                    socket = serverSocket.accept();
                    players[1] = new Player(socket, PieceColor.BLACK);
                    runGame(players);
                    if(socket!=null){
                        break;
                    }
                }

            }

            if(PORT==1008){
                System.out.println("connet" + PORT);
                System.out.println(socket);

                socket = serverSocket.accept();
                players1[0] = new Player(socket, PieceColor.WHITE);

                while (true){
                    socket = serverSocket.accept();
                    players1[1] = new Player(socket, PieceColor.BLACK);
                    runGame(players1);
                }
            }

            if(PORT==1009){
                System.out.println("connet" + PORT);
                System.out.println(socket);

                socket = serverSocket.accept();
                players2[0] = new Player(socket, PieceColor.WHITE);

                while (true){
                    socket = serverSocket.accept();
                    players2[1] = new Player(socket, PieceColor.BLACK);
                    runGame(players2);
                }
            }
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
