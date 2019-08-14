import DataTypes.PreconditionsException;
import Model.*;
import InputKnowledge.*;
import Parser.KBParser;
import Parser.Parser;
import Parser.*;

import java.util.HashSet;
import java.util.Set;

public class HandleKB {

    public static Model createModel(Set<String> kbPaths)
            throws FormatNotSupportedException, PreconditionsException {
        Set<KnowledgeBase> KBs = new HashSet<>();

        for (String kbPath : kbPaths) {
            KBParser parser = new Parser();
            KnowledgeBase parsedKB = parser.parse(kbPath);

            KBs.add(parsedKB);
        }

        return ConsolidateKB.consolidateKB(KBs);
    }
}
