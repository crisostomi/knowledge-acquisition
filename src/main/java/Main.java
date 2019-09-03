import java.util.HashSet;
import java.util.Set;
import Model.Model;
import Util.CustomLogger;



public class Main {
    public static void main(String[] args) {
        String username = System.getProperty("user.name");
        String projectFolder = "/home/"+username+"/Dropbox/Tesisti/software";

        String testFolder = projectFolder + "/test-cases/urea";

        String kbPath = testFolder + "/in/pathway.sbml";
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
