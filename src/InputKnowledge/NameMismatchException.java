package InputKnowledge;

import DataTypes.PreconditionsException;

public class NameMismatchException extends ConflictingKnowledgeException {
    NameMismatchException(){super();}
    NameMismatchException(String message) {super(message);}
}
