import java.util.HashSet;
import java.util.Set;
import Model.Model;

public class Main {
    public static void main(String[] args) {

        String kbPath = "/home/scacio/Downloads/R-HSA-3828062.sbml";

        try {

            Set<String> kbPaths = new HashSet<>();
            kbPaths.add(kbPath);

            Model m = HandleKB.createModel(kbPaths);
            System.out.println("hello world!");

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
