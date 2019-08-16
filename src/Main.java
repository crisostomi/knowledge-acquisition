import java.util.HashSet;
import java.util.Set;
import Model.Model;

public class Main {
    public static void main(String[] args) {

        String kbPath = "/home/scacio/Dropbox/Tesisti/software/development/tool/test-cases/test-case-4/in/galactose.sbml";

        try {

            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);

            Model m = HandleKB.createModel(kbPaths);
            System.out.println("All done!");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
