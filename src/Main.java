import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.*;

import Model.Model;
import Parser.ConfigBuilder;
import Parser.XMLParser;

public class Main {
    public static void main(String[] args) {

        String projectFolder = "/home/don/Dropbox/Tesisti/software/development/KnowledgeAcquisition";
        String testFolder = projectFolder + "/test-cases/test-case-4";

        String kbPath = testFolder + "/in/galactose.sbml";
        String xmlPath = testFolder + "/out/quantitative.xml";

        CustomLogger.setup();


        try {
            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
            kbPaths.add(xmlPath);

            Model m = HandleModel.createModel(kbPaths);
            /*ConfigBuilder c = new ConfigBuilder(m, xmlPath);
            c.buildConfig();*/
            System.out.println("All done!");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
