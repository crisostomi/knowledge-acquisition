package Model;

import DataTypes.PreconditionsException;

import java.io.Serializable;

public class LinkMultiplicityException extends PreconditionsException implements Serializable {
    public LinkMultiplicityException() {super();}
    public LinkMultiplicityException(String message) {super(message);}
}
