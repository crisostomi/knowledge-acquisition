import java.util.HashSet;
import java.util.Set;
import Model.*;
import Parser.ConfigBuilder;
import Util.CustomLogger;



public class Main {
    public static final String ABUNDANCES_FILENAME = "abundances.tsv";
    public static final String REACTOME_FILENAME = "pathway.sbml";
    public static final String LOG_FILENAME = "log.txt";
    public static final String TEST = "urea";

    public static final double HeLaProteins = 2.3e9;

    public static void main(String[] args) {

        String username = System.getProperty("user.name");
        String projectFolder = "/home/"+username+"/Dropbox/Tesisti/software";

        String testFolder = projectFolder + "/test-cases/"+TEST;
        String kbPath = testFolder + "/in/"+REACTOME_FILENAME;
        String tsvPath = testFolder +"/in/"+ ABUNDANCES_FILENAME;
        String logPath = testFolder + LOG_FILENAME;
        String dumpPath = testFolder + "/out/model_dump.xml";
        String xmlPath = testFolder + "/in/quantitative.xml";
        System.out.println("KnowledgeAcquisition: testing test-case "+TEST);
        CustomLogger.setup(logPath);

        CustomLogger.setup(logPath);

        try {
            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
            kbPaths.add(xmlPath);
            kbPaths.add(tsvPath);

            Model m = HandleModel.createModel(kbPaths);
            CellType helaCell = new CellType("HeLa", HeLaProteins);
            m.setCellType(helaCell);
            m.consolidateAbundance();
//            ConfigBuilder c = new ConfigBuilder(m, xmlPath);
//            c.buildConfig();
            System.out.println("All done!");
            m.dump(dumpPath);


        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
