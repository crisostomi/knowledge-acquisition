package Model.Exceptions;

import DataTypes.PreconditionsException;
import Model.Model;

import java.io.Serializable;

public class BiologicalEntityNotUniqueException extends PreconditionsException implements Serializable {
    public BiologicalEntityNotUniqueException() {super();}
    public BiologicalEntityNotUniqueException(String message) {super(message);}
}
