package InputKnowledge;

import DataTypes.PreconditionsException;

public class ConflictingKnowledgeException extends PreconditionsException {
    ConflictingKnowledgeException() {super();}
    ConflictingKnowledgeException(String message) {super(message);}
}
