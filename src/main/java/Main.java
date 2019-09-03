import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import DataTypes.PreconditionsException;
import DataTypes.RateParameter;
import Miner.SabioMiner;
import Model.Model;
import Model.Reaction;
import Parser.ConfigBuilder;
import Util.CustomLogger;
import Model.LinkTypeComprises;
import Model.BiologicalEntity;
import Model.Protein;
import com.thoughtworks.xstream.XStream;

import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) {

        String projectFolder = "/home/don/Dropbox/Tesisti/software";
        String testFolder = projectFolder + "/test-cases/test-case-urea";

        String kbPath = testFolder + "/in/urea.sbml";
//        String xmlPath = testFolder + "/in/quantitative.xml";
        String tsvPath = testFolder + "/in/abundances.tsv";

        String logPath = testFolder + "/out/log.txt";
        String dumpPath = testFolder + "/out/model_dump.xml";

        CustomLogger.setup(logPath);

        try {
            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
//            kbPaths.add(xmlPath);
            kbPaths.add(tsvPath);

            Model m = HandleModel.createModel(kbPaths);
//            ConfigBuilder c = new ConfigBuilder(m, xmlPath);
//            c.buildConfig();
            System.out.println("All done!");
            m.dump(dumpPath);


        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
