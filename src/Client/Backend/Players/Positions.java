package Client.Backend.Players;

import Client.Backend.GameObjects.Pieces.PieceColor;
import java.awt.*;
import java.io.Serializable;

public class Positions implements Serializable {

    private final Point origin;
    private final PieceColor playersColor;
    private Point destination;

    public Positions(Point origin, PieceColor playersColor) {
        this.origin = origin;
        this.playersColor = playersColor;
    }

    public void setDestination(Point destination) {
        this.destination = destination;
    }

    public Point getOrigin() {
        return origin;
    }

    public Point getDestination() {
        return destination;
    }

    public boolean isReadyToBeUsed() {
        return origin != null && destination != null;
    }

    public PieceColor getPlayersColor() {
        return playersColor;
    }

    @Override
    public String toString() {
        return origin.toString() + destination.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Positions)) return false;
        Positions positions = (Positions) obj;
        return origin.equals(positions.origin) && destination.equals(positions.destination);
    }
}
