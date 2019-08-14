package Parser;

import InputKnowledge.KnowledgeBase;

public class Parser implements KBParser {
    @Override
    public KnowledgeBase parse(String kbPath) throws FormatNotSupportedException{
        SBMLParser sbmlParser = new SBMLParser();
        XMLParser xmlParser = new XMLParser();

        if (kbPath.endsWith(".xml")) {
            return sbmlParser.parse(kbPath);
        } else if (kbPath.endsWith(".sbml")) {
            return xmlParser.parse(kbPath);
        } else {
            throw new FormatNotSupportedException(
                    "Could not recognize format of file " + kbPath
            );
        }
    }
}
