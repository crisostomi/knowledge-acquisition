import org.sbml.jsbml.*;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("hello world!");

        String kbPath = "/home/scacio/Downloads/R-HSA-3828062.sbml";

        try {
            SBase tree = SBMLReader.read(new File(kbPath));
            Reaction r = tree.getModel().getListOfReactions().get(0);
            String id = r.getId();
            String name = r.getName();
            String c = r.getCompartment();

            Integer sboTerm = r.getSBOTerm();

            System.out.println("File read!");
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
