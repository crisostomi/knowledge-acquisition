package InputKnowledge;

import DataTypes.PreconditionsException;

public class NameMismatchException extends PreconditionsException {
    NameMismatchException(){super();}
    NameMismatchException(String message) {super(message);}
}
