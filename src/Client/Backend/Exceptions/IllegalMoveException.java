package Client.Backend.Exceptions;

import java.awt.*;

public class IllegalMoveException extends Exception {

    public IllegalMoveException(String errorMessage) {
        super(errorMessage);
    }

    public IllegalMoveException(String className, Point origin, Point destination) {
        super(String.format("%s can not move from %d,%d to %d,%d", className, origin.x, origin.y, destination.x, destination.y));
    }

}
