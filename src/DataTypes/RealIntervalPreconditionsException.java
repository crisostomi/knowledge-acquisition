package DataTypes;

public class RealIntervalPreconditionsException extends Exception {

    /**
     * Class that represents the failure to comply with RealInterval constructor
     * preconditions
     */
    public RealIntervalPreconditionsException() {
        super();
    }

    /**
     * Method to provide the client with a descriptive message of the exception
     * @param message the descriptive message
     */
    public RealIntervalPreconditionsException(String message) {
        super(message);
    }

}
