import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import Model.Model;
import Parser.ConfigBuilder;
import Util.CustomLogger;
import Model.LinkTypeComprises;
import Model.BiologicalEntity;
import Model.Protein;
import com.thoughtworks.xstream.XStream;

public class Main {
    public static void main(String[] args) {

        String projectFolder = "/home/don/Dropbox/Tesisti/software";
        String testFolder = projectFolder + "/test-cases/test-case-4";

        String kbPath = testFolder + "/in/R-HSA-70370.sbml";
        String xmlPath = testFolder + "/in/quantitative.xml";
        String tsvPath = testFolder + "/in/galactose-catabolism.tsv";
        String logPath = testFolder + "/out/log.txt";
        String dumpPath = testFolder + "/out/model_dump.xml";

        CustomLogger.setup(logPath);


        try {
            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);
            kbPaths.add(xmlPath);
            kbPaths.add(tsvPath);

            Model m = HandleModel.createModel(kbPaths);
            ConfigBuilder c = new ConfigBuilder(m, xmlPath);
            c.buildConfig();
            System.out.println("All done!");
            m.dump(dumpPath);
            for (LinkTypeComprises link: m.getLinkComprisesSet() ){
                BiologicalEntity biologicalEntity = link.getBiologicalEntity();
                if (biologicalEntity instanceof Protein){
                    Protein protein = (Protein) biologicalEntity;
                    System.out.println(protein.getId() +" "+ protein.getAbundance());
                }
            }


        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
