import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import DataTypes.RateParameter;
import DataTypes.RealInterval;
import Model.*;
import Util.CustomLogger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;


public class Main {
    public static final String PROJECT_FOLDER = "";
    public static final String ABUNDANCES_FILENAME = "abundances.tsv";
    public static final String REACTOME_FILENAME = "pathway.sbml";
    public static final String LOG_FILENAME = "log.txt";
    public static final String TEST = "estrogen-biosynthesis";

    public static final double HeLaSize = 3e-12;
    public static final double HeLaProteins = 2.3e9;

    public static void main(String[] args) {

        String testFolder = PROJECT_FOLDER + "/test-cases/"+TEST;
        String kbPath = testFolder + "/in/"+REACTOME_FILENAME;
        String globalAbundancesPath = PROJECT_FOLDER +"/knowledge/"+ ABUNDANCES_FILENAME;
        String logPath = testFolder +"/out/"+ LOG_FILENAME;
        String localAbundancesPath = testFolder+"/in/"+ABUNDANCES_FILENAME;
        String dumpPath = testFolder + "/out/model_dump.xml";
        String xmlPath = testFolder + "/in/quantitative.xml";

        System.out.println("KnowledgeAcquisition: testing test-case "+TEST);

        CustomLogger.setup(logPath);

        try {
            Bootstrap.joinAbundances(kbPath, globalAbundancesPath, localAbundancesPath);
            Bootstrap.buildQuantitativeFile(kbPath, xmlPath);
            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
            kbPaths.add(xmlPath);
            kbPaths.add(localAbundancesPath);
            Model m = HandleModel.createModel(kbPaths);
            CellType helaCell = new CellType("HeLa", HeLaSize, HeLaProteins);
            m.setCellType(helaCell);
            System.out.println("All done!");
            m.dump(dumpPath);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
