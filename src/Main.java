import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import Model.Model;
import Parser.ConfigBuilder;
import Util.CustomLogger;
import com.thoughtworks.xstream.XStream;

public class Main {
    public static void main(String[] args) {

        String projectFolder = "/home/don/Dropbox/Tesisti/software/development";
        String testFolder = projectFolder + "/test-cases/test-case-4";

        String kbPath = testFolder + "/in/galactose.sbml";
        String xmlPath = testFolder + "/in/quantitative.xml";
        String logPath = testFolder + "/out/log.txt";
        String dumpPath = testFolder + "/out/model_dump.xml";

        CustomLogger.setup(logPath);


        try {
            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
            kbPaths.add(xmlPath);

            Model m = HandleModel.createModel(kbPaths);
            ConfigBuilder c = new ConfigBuilder(m, xmlPath);
            c.buildConfig();
            System.out.println("All done!");
            m.dump(dumpPath);
            XStream xStream = new XStream();
            Path path = Paths.get(dumpPath);
            String deserializedXML = Files.readString(path, StandardCharsets.US_ASCII);
            Model newModel = (Model) xStream.fromXML(deserializedXML);
            System.out.println(newModel.getLinkComprisesSet());

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
