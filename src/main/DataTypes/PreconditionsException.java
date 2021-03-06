package DataTypes;

import java.io.Serializable;

public class PreconditionsException extends Exception implements Serializable {

    /**
     * Class that represents the failure to comply with datatypes methods preconditions
     */
    public PreconditionsException() {
        super();
    }

    /**
     * Method to provide the client with a descriptive message of the exception
     * @param message the descriptive message
     */
    public PreconditionsException(String message) {
        super(message);
    }

}
