package Client.Backend.Exceptions;

import java.awt.*;

public class NoPieceFoundException extends Exception {

    public NoPieceFoundException(Point position) {
        super("no piece found in position x = " + position.x + " y = " + position.y);
    }

}
