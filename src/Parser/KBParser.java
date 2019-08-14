package Parser;

import InputKnowledge.KnowledgeBase;

public interface KBParser {

    KnowledgeBase parse(String kbPath) throws FormatNotSupportedException;
}
