package Model;

import DataTypes.PreconditionsException;

import java.io.Serializable;

public class BiologicalEntityNotUniqueException extends PreconditionsException implements Serializable {
    BiologicalEntityNotUniqueException() {super();}
    BiologicalEntityNotUniqueException(String message) {super(message);}
}
