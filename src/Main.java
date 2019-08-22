import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.*;

import Model.Model;
import Parser.ConfigBuilder;
import Parser.XMLParser;
import Util.CustomLogger;

public class Main {
    public static void main(String[] args) {

        String projectFolder = "/home/scacio/Dropbox/Tesisti/software/development";
        String testFolder = projectFolder + "/test-cases/test-case-4";

        String kbPath = testFolder + "/in/galactose.sbml";
        String xmlPath = testFolder + "/in/quantitative.xml";
        String logPath = testFolder + "/out/log.txt";
        String dumpPath = testFolder + "/out/model_dump.json";

        CustomLogger.setup(logPath);


        try {
            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
            kbPaths.add(xmlPath);

            Model m = HandleModel.createModel(kbPaths);
            // ConfigBuilder c = new ConfigBuilder(m, xmlPath);
            // c.buildConfig();
            System.out.println("All done!");
            m.dump(dumpPath);

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
