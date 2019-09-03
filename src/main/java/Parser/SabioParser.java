package Parser;

import DataTypes.PreconditionsException;
import InputKnowledge.KnowledgeBase;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBase;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

public class SabioParser implements KBParser {

    @Override
    public KnowledgeBase parse(String kbPath) throws XMLStreamException, IOException, FormatNotSupportedException, PreconditionsException, ParserConfigurationException, SAXException {
        File f = new File(kbPath);
        SBase tree = SBMLReader.read(f);

        KnowledgeBase kb = new KnowledgeBase(f.getName(), kbPath);

        return kb;
    }

    private void parseParameters(SBase tree, KnowledgeBase kb)
            throws PreconditionsException {

    }
}
