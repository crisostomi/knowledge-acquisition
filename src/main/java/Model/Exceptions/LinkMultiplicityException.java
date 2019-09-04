package Model.Exceptions;

import DataTypes.PreconditionsException;
import Model.Model;

import java.io.Serializable;

public class LinkMultiplicityException extends PreconditionsException implements Serializable {
    public LinkMultiplicityException() {super();}
    public LinkMultiplicityException(String message) {super(message);}
}
