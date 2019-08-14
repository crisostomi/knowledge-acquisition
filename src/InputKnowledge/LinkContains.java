package InputKnowledge;

import DataTypes.PreconditionsException;

public class LinkContains {
    private LinkContains(){}

    public static void insertLink(KnowledgeBase kb, KnowledgeAtom ka) throws PreconditionsException {
        // Creo un pass: solo i metodi di questa classe possono farlo!
        LinkContains pass = new LinkContains();
        // Creo il link
        LinkTypeContains link = new LinkTypeContains(kb, ka);
        // Inserisco il link nei due oggetti esibendo il pass
        kb.insertLinkContains(pass, link);
        ka.insertLinkContains(pass, link);
    }
}
