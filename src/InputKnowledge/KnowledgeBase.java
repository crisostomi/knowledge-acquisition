package InputKnowledge;

public class KnowledgeBase {

    private final String name;
    private final String filePath;

    /**
     * Class that represents a knowledge base, which is associated with a stream of
     * @see KnowledgeAtom
     * @param name the name of the KB
     * @param filePath the filePath of the KB
     */
    public KnowledgeBase(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }
}
