package Client.Frontend;

import Client.GameManagers.ChessGame;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseListener implements java.awt.event.MouseListener {
    
    private final ChessGame game;
    private final Point position;

    public MouseListener(ChessGame game, Point position) {
        this.game = game;
        this.position = position;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        game.tileClicked(position);
    }   

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
