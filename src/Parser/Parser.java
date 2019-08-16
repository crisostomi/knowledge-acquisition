package Parser;

import DataTypes.PreconditionsException;
import InputKnowledge.KnowledgeBase;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Parser implements KBParser {
    @Override
    public KnowledgeBase parse(String kbPath)
            throws XMLStreamException, IOException,
                FormatNotSupportedException, PreconditionsException {

        if (kbPath.endsWith(".xml")) {
            XMLParser xmlParser = new XMLParser();
            return xmlParser.parse(kbPath);
        } else if (kbPath.endsWith(".sbml")) {
            SBMLParser sbmlParser = new SBMLParser();
            return sbmlParser.parse(kbPath);
        } else {
            throw new FormatNotSupportedException(
                    "Could not recognize format of file " + kbPath
            );
        }
    }
}
