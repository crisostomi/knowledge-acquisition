package InputKnowledge;

import DataTypes.PreconditionsException;

import java.util.logging.Logger;

public class LinkContains {
    private LinkContains(){}

    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void insertLink(KnowledgeBase kb, KnowledgeAtom ka) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkContains pass = new LinkContains();
        // Creo il link
        LinkTypeContains link = new LinkTypeContains(kb, ka);
        // Inserisco il link nei due oggetti esibendo il pass
        kb.insertLinkContains(pass, link);
        ka.insertLinkContains(pass, link);

        logger.info(
                "Link contains between KnowledgeBase " + kb.getName()
                        + ", " + ka.getClass().getCanonicalName() + " " + ka.getId()
                + " inserted"
        );
    }
}
