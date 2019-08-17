import java.util.HashSet;
import java.util.Set;
import Model.Model;
import Parser.ConfigBuilder;
import Parser.XMLParser;

public class Main {
    public static void main(String[] args) {

        String projectFolder = "/home/scacio/Dropbox/Tesisti/software/development/KnowledgeAcquisition";
        String testFolder = projectFolder + "/test-cases/test-case-4";

        String kbPath = testFolder + "/in/galactose.sbml";
        String xmlPath = testFolder + "/out/quantitative.xml";
        try {

            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
            kbPaths.add(xmlPath);

            Model m = HandleKB.createModel(kbPaths);
            /*ConfigBuilder c = new ConfigBuilder(m, xmlPath);
            c.buildConfig();*/
            System.out.println("All done!");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
