import Miner.SabioMiner;
import Parser.SBMLParser;
import Parser.SabioParser;
import org.sbml.jsbml.SBase;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class PathwayFinder {

    public static final String username = System.getProperty("user.name");
    public static final String projectFolder = "/home/"+username+"/Dropbox/Tesisti/software";
    public static final String testFolder = projectFolder + "/test-cases/whole-reactome";

    public static void main(String args[]) throws IOException, XMLStreamException {
        PathwayFinder finder = new PathwayFinder();
//        finder.findKineticInfoPathways(testFolder, 15, 2);
        finder.findProteinPathways(testFolder, 6, 6);
    }

    public void findKineticInfoPathways(String folder, int maxNumReactions, int minNumProteins) throws IOException, XMLStreamException {
        File folderFile = new File(folder);
        SBMLParser parser = new SBMLParser(true);
        SabioMiner sabioMiner = new SabioMiner();
        FileWriter fileWrite = new FileWriter(folder+"/../../kineticInfo.txt");

        for(String pathway: folderFile.list()){

            String pathwayFullPath = folder+"/"+pathway;
            File pathwayFile = new File(pathwayFullPath);
            SBase tree = parser.getContent(pathwayFullPath);

            Set<String> reaction_Ids = parser.getReactionIds(tree);
            int numReactions = (reaction_Ids).size();
            if( numReactions > maxNumReactions){
                pathwayFile.delete();
                continue;
            }

            int numProteins = parser.getProteinsExternalIds(pathwayFullPath).size();
            if (numProteins < minNumProteins){
                pathwayFile.delete();
                continue;
            }

            StringBuilder pathwayStringBuilder = new StringBuilder();
            int foundReactions = 0;

            for (String reaction_Id: reaction_Ids){
                String idNumber = reaction_Id.replace("reaction_","");
                boolean result = sabioMiner.informationAvailable(idNumber);
                String resultString = result?"YES":"NO";
                if (result){
                    foundReactions++;
                }
                pathwayStringBuilder.append("\t"+resultString+": "+reaction_Id+"\n");
            }
            if ((float)foundReactions/numReactions >= 0.3){
                fileWrite.append("Pathway: "+pathway+" found reactions: "+foundReactions+" total reactions: "+numReactions+" proteins: "+numProteins+"\n");
                fileWrite.append(pathwayStringBuilder.toString());
            }
            else{
                pathwayFile.delete();
            }
        }
        fileWrite.close();

    }

    public void findProteinPathways(String folder, int maxNumReactions, int minNumProteins) throws IOException, XMLStreamException {
        File folderFile = new File(folder);
        SBMLParser parser = new SBMLParser(true);
        FileWriter fileWrite = new FileWriter(folder+"/../../proteinInfo.txt");
        StringBuilder stringBuilder = new StringBuilder();

        for(String pathway: folderFile.list()) {
            String pathwayFullPath = folder + "/" + pathway;
            File pathwayFile = new File(pathwayFullPath);
            SBase tree = parser.getContent(pathwayFullPath);
            Set<String> reaction_Ids = parser.getReactionIds(tree);

            int numReactions = (reaction_Ids).size();
            int numProteins = parser.getProteinsExternalIds(pathwayFullPath).size();

            if (numReactions > maxNumReactions || numProteins < minNumProteins) {
                pathwayFile.delete();
                continue;
            }
            stringBuilder.append("Pathway: "+pathway+ " reactions: "+numReactions+ "proteins: "+numProteins+"\n");
        }
        fileWrite.append(stringBuilder.toString());
        fileWrite.close();
    }




}
