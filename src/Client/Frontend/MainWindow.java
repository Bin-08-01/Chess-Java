package Client.Frontend;

import Client.Backend.GameObjects.Pieces.PieceColor;
import Client.Client;
import Server.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;


public class MainWindow extends JFrame{
    public JButton singlePlayBtn, multiPlayBtn, closeBtn;
    public ImageIcon imageIcon;
    public MainWindow(){
        super("Chess");
        setFrame();
        setEvents();
    }

    private void setEvents() {
        singlePlayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frameColorSelect();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        multiPlayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    startPlayingRole(false, PieceColor.WHITE);
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainWindow.this.dispose();
            }
        });
    }

    private void setFrame() {
        Container container = this.getContentPane();

        imageIcon = new ImageIcon(getClass().getResource("Images/chess.png"));
        JLabel lbIcon = new JLabel(imageIcon);
        Dimension size = lbIcon.getPreferredSize();
        lbIcon.setBounds(30, 30, size.width, size.height);
        container.add(lbIcon);

        singlePlayBtn = new JButton("Single");
        singlePlayBtn.setBounds(225, 400, 100, 30);
        container.add(singlePlayBtn);

        multiPlayBtn = new JButton("Multi");
        multiPlayBtn.setBounds(225, 440, 100, 30);
        container.add(multiPlayBtn);

        closeBtn = new JButton("Exit");
        closeBtn.setBounds(225, 480, 100, 30);
        container.add(closeBtn);

        JLabel lbMain = new JLabel();
        lbMain.setBackground(Color.gray);
        lbMain.setOpaque(true);
        container.add(lbMain);
    }

    private void frameColorSelect(){
        JFrame frame = new JFrame();

        JButton blackBtn = new JButton("Black");
        blackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorSinglePlayer(PieceColor.BLACK);
                frame.setVisible(false);
            }
        });
        blackBtn.setBounds(160, 40, 80, 40);
        frame.add(blackBtn);

        JButton whiteBtn = new JButton("WHITE");
        whiteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorSinglePlayer(PieceColor.WHITE);
                frame.setVisible(false);
            }
        });
        whiteBtn.setBounds(40, 40, 80, 40);
        frame.add(whiteBtn);

        JButton randomColorBtn = new JButton("Random");
        randomColorBtn.setBounds(100, 90, 80, 40);
        randomColorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colorSinglePlayer(PieceColor.NULL);
                frame.setVisible(false);
            }
        });
        frame.add(randomColorBtn);

        JLabel bg = new JLabel();
        bg.setBackground(Color.gray);
        bg.setOpaque(true);
        frame.add(bg);
        Image icon = null;
        URL iconUrl = getClass().getResource("Images/icon.png");
        try {
            icon = ImageIO.read(iconUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setIconImage(icon);
        frame.setSize(300, 200);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void colorSinglePlayer(PieceColor pieceColor){
        try {
            startPlayingRole(true, pieceColor);
            MainWindow.this.setVisible(false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void Display(){
        Image icon = null;
        URL iconUrl = getClass().getResource("Images/icon.png");
        try {
            icon = ImageIO.read(iconUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setSize(560, 600);
        this.setIconImage(icon);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void startPlayingRole(boolean role, PieceColor pieceColor) throws IOException, ClassNotFoundException {
        if(role) {
            Client.startSinglePlayerGame(pieceColor);
        } else {
            new SocketWindow();
//            Server server = new Server();
//            server.runServer();
            MainWindow.this.setVisible(false);
//            Client.startMultiplayerGame();
        }
    }
}
