package Parser;

import InputKnowledge.KnowledgeBase;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Parser implements KBParser {
    @Override
    public KnowledgeBase parse(String kbPath) throws XMLStreamException, IOException, FormatNotSupportedException {

        if (kbPath.endsWith(".xml")) {
            SBMLParser sbmlParser = new SBMLParser();
            return sbmlParser.parse(kbPath);
        } else if (kbPath.endsWith(".sbml")) {
            XMLParser xmlParser = new XMLParser();
            return xmlParser.parse(kbPath);
        } else {
            throw new FormatNotSupportedException(
                    "Could not recognize format of file " + kbPath
            );
        }
    }
}
