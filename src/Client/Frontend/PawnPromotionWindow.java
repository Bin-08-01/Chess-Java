package Client.Frontend;

import Client.Backend.GameObjects.Pieces.PieceColor;
import Client.Backend.GameObjects.Pieces.PieceType;
import Client.GameManagers.ChessGame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PawnPromotionWindow {

    private static final int QUEEN_POSITION = 0;
    private final int ROOK_POSITION = 1;
    private final int KNIGHT_POSITION = 2;
    private final int BISHOP_POSITION = 3;
    private final JDialog dialog;
    private final ChessGame game;

    public PawnPromotionWindow(ChessGame game, PieceColor playersColor, JFrame frame) {
        this.game = game;
        JOptionPane promotionWindow = new JOptionPane();
        JButton[] buttons = getButtons(playersColor);
        promotionWindow.setMessage("Please choose the piece you'd like to promote to");
        promotionWindow.setMessageType(JOptionPane.QUESTION_MESSAGE);
        promotionWindow.setOptions(new Object[] { buttons[0], buttons[1], buttons[2], buttons[3] });
        dialog = promotionWindow.createDialog(frame, "Pawn Promotion");
        dialog.setVisible(true);
    }

    private JButton[] getButtons(PieceColor playersColor) {
        JButton[] buttons = new JButton[4];
        buttons[QUEEN_POSITION] = new JButton(getImage(playersColor, PieceType.QUEEN));
        buttons[ROOK_POSITION] = new JButton(getImage(playersColor, PieceType.ROOK));
        buttons[KNIGHT_POSITION] = new JButton(getImage(playersColor, PieceType.KNIGHT));
        buttons[BISHOP_POSITION] = new JButton(getImage(playersColor, PieceType.BISHOP));
        for (JButton button :buttons) {
            button.setPreferredSize(new Dimension(GraphicBoard.TILE_SIZE, GraphicBoard.TILE_SIZE));
        }
        addListeners(buttons);
        return buttons;
    }

    private ImageIcon getImage(PieceColor pieceColor, PieceType pieceType) {
        try {
            Image image = getImageFromFileName(getUrl(pieceColor, pieceType));
            image.getScaledInstance(GraphicBoard.TILE_SIZE, GraphicBoard.TILE_SIZE, Image.SCALE_DEFAULT);
            return new ImageIcon(image);
        } catch (IOException e) {
            return null;
        }
    }

    private void addListeners(JButton[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i].addActionListener(actionEvent -> clicked(finalI));
        }
    }

    private void clicked(int buttonIndex) {
        dialog.dispose();
        switch (buttonIndex){
            case ROOK_POSITION:
                game.promotionClick(PieceType.ROOK);
                break;
            case KNIGHT_POSITION:
                game.promotionClick(PieceType.KNIGHT);
                break;
            case BISHOP_POSITION:
                game.promotionClick(PieceType.BISHOP);
                break;
            default:
                game.promotionClick(PieceType.QUEEN);
        }
    }

    private String getUrl(PieceColor playersColor, PieceType pieceType) {
        return "Images/" + playersColor.toString().toLowerCase() + "_" + pieceType.toString().toLowerCase() + ".png";
    }

    private BufferedImage getImageFromFileName(String fileName) throws IOException {
        return ImageIO.read(getClass().getResource(fileName));
    }

}