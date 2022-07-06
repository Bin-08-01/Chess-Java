package Client.Frontend;

import Client.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class SocketWindow {
    private JButton room1Btn, room2Btn, room3Btn;

    public SocketWindow() throws IOException {
        setFrame();
    }

    private void setFrame() throws IOException {
        Image image = ImageIO.read(getClass().getResource("Images/room_bg.png"));
        image.getScaledInstance(GraphicBoard.TILE_SIZE, GraphicBoard.TILE_SIZE, Image.SCALE_DEFAULT);

        JFrame frame = new JFrame();
        Container container = frame.getContentPane();

        container.setLayout(new  FlowLayout());

        room1Btn = new JButton(new ImageIcon(image));
        room1Btn.setPreferredSize(new Dimension(200, 200));
        room1Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.setVisible(false);
                    Client.startMultiplayerGame(1007);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        room2Btn = new JButton(new ImageIcon(image));
        room2Btn.setPreferredSize(new Dimension(200, 200));
        room2Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.setVisible(false);
                    Client.startMultiplayerGame(1008);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });

        room3Btn = new JButton(new ImageIcon(image));
        room3Btn.setPreferredSize(new Dimension(200, 200));
        room3Btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.setVisible(false);
                    Client.startMultiplayerGame(1009);
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });


        JPanel firstRoom = new JPanel();
        firstRoom.setLayout(new BoxLayout(firstRoom, BoxLayout.Y_AXIS));
        JLabel firstRoomLb = new JLabel("Room 1");
        firstRoom.add(room1Btn);
        firstRoom.add(firstRoomLb);

        JPanel secondRoom = new JPanel();
        secondRoom.setLayout(new BoxLayout(secondRoom, BoxLayout.Y_AXIS));
        JLabel secondRoomLb = new JLabel("Room 2");
        secondRoom.add(room2Btn);
        secondRoom.add(secondRoomLb);

        JPanel thirdRoom = new JPanel();
        thirdRoom.setLayout(new BoxLayout(thirdRoom, BoxLayout.Y_AXIS));
        JLabel thirdRoomLb = new JLabel("Room 3");
        thirdRoom.add(room3Btn);
        thirdRoom.add(thirdRoomLb);

        container.add(firstRoom);
        container.add(secondRoom);
        container.add(thirdRoom);

        frame.setSize(660, 300);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
