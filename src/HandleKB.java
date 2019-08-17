import DataTypes.PreconditionsException;
import Model.*;
import InputKnowledge.*;
import Parser.KBParser;
import Parser.Parser;
import Parser.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class HandleKB {

    public static Model createModel(Set<String> kbPaths)
            throws XMLStreamException,
            IOException,
            FormatNotSupportedException,
            PreconditionsException,
            ParserConfigurationException,
            SAXException {
        Set<KnowledgeBase> KBs = new HashSet<>();

        for (String kbPath : kbPaths) {
            KBParser parser = new Parser();
            KnowledgeBase parsedKB = parser.parse(kbPath);

            KBs.add(parsedKB);
        }

        return ConsolidateKB.consolidateKB(KBs);
    }
}
