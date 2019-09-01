package Parser;

import DataTypes.PreconditionsException;
import InputKnowledge.KnowledgeBase;
import InputKnowledge.ProteinKA;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TSVParser implements KBParser{
    @Override
    public KnowledgeBase parse(String kbPath) throws XMLStreamException, IOException, FormatNotSupportedException, PreconditionsException, ParserConfigurationException, SAXException {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

// creates a TSV parser
        TsvParser parser = new TsvParser(settings);

// parses all rows in one go.
        KnowledgeBase kb = new KnowledgeBase("paxDB", kbPath);
        List<String[]> allRows = parser.parseAll(getReader(kbPath));
        for (String[] row: allRows){
            String internalID = row[0];
            String externalID =  row[1].split("\\|")[0];
            Double abundance = Double.valueOf(row[2]);
            ProteinKA proteinKA = new ProteinKA(internalID, false, kb);
            proteinKA.initializeAbundance(abundance);
            proteinKA.initializeExternalId(externalID);
        }
        return kb;

    }

    public Reader getReader(String path) throws UnsupportedEncodingException, FileNotFoundException {
        return new FileReader(new File(path));
    }
}
