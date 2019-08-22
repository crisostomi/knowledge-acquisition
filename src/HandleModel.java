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
import java.util.logging.Level;
import java.util.logging.Logger;

public class HandleModel {

    private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );


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
        LOGGER.info("Creating model");
        Model model = ConsolidateKB.consolidateKB(KBs);
        LOGGER.info("Model succesfully created");
        return model;
    }
}