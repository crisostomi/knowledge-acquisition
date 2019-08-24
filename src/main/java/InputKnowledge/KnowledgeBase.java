package InputKnowledge;

import DataTypes.PreconditionsException;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class KnowledgeBase {

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final String name;
    private final String filePath;

    private Set<LinkTypeContains> linkContainsSet = new HashSet<>();

    public String getName() {
        return name;
    }

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

    public void insertLinkContains(LinkContains pass, LinkTypeContains l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "E’ necessario esibire un oggetto di class " +
                            "AssociazioneAssoc per invocare questo metodo!");
        linkContainsSet.add(l);
    }

    public Set<LinkTypeContains> getLinkContainsSet() {
        return (Set<LinkTypeContains>)((HashSet<LinkTypeContains>)linkContainsSet).clone();
    }
}
