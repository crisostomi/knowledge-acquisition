package Model;

import DataTypes.PreconditionsException;

public class BiologicalEntityNotUniqueException extends PreconditionsException {
    BiologicalEntityNotUniqueException() {super();}
    BiologicalEntityNotUniqueException(String message) {super(message);}
}
