
import Client.Frontend.MainWindow;
import Server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MainWindow mainWindow = new MainWindow();
        mainWindow.Display();
//        new Server();
    }
}
