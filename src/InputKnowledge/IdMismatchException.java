package InputKnowledge;

import DataTypes.PreconditionsException;

public class IdMismatchException extends ConflictingKnowledgeException {
    public IdMismatchException() {
        super();
    }
    public IdMismatchException(String message) {
        super(message);
    }
}
