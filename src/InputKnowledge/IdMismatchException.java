package InputKnowledge;

import DataTypes.PreconditionsException;

public class IdMismatchException extends PreconditionsException {
    public IdMismatchException() {
        super();
    }
    public IdMismatchException(String message) {
        super(message);
    }
}
