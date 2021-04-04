package Util;

import DataTypes.PreconditionsException;
import InputKnowledge.KnowledgeBase;
import Miner.SabioMiner;
import Parser.SBMLParser;
import Parser.TSVParser;
import org.sbml.jsbml.SBase;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PathwayFinder {

    public static final String username = System.getProperty("user.name");
    public static final String projectFolder = "/home/"+username+"/Dropbox/Tesisti/software";
    public static final String testFolder = projectFolder + "/test-cases";

    public static void main(String args[]) throws IOException, XMLStreamException {
        PathwayFinder finder = new PathwayFinder();
//        finder.findKineticInfoPathways(testFolder, 15, 2);
//        finder.findProteinPathways(testFolder, 2, 5);
//        finder.findPathwaysWithFewReactions(testFolder, 5);
        finder.cataloguePathways(testFolder);
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

    public void findProteinPathways(String folder, int minNumProteins, int maxNumSpecies) throws IOException, XMLStreamException {
        File folderFile = new File(folder);
        SBMLParser parser = new SBMLParser(true);
        FileWriter fileWrite = new FileWriter(folder+"/../../proteinInfo.txt");
        StringBuilder stringBuilder = new StringBuilder();

        for(String pathway: folderFile.list()) {
            String pathwayFullPath = folder + "/" + pathway;
            File pathwayFile = new File(pathwayFullPath);
            SBase tree = parser.getContent(pathwayFullPath);
            Set<String> reaction_Ids = parser.getReactionIds(tree);
            int numSpecies = parser.getSpeciesCount(tree);
            int numReactions = (reaction_Ids).size();
            int numProteins = parser.getProteinsExternalIds(pathwayFullPath).size();

            if (numProteins < minNumProteins || numSpecies > maxNumSpecies) {
                pathwayFile.delete();
                continue;
            }
            stringBuilder.append("Pathway: "+pathway+ "\n\treactions: "+numReactions+ "\n\tproteins: "+numProteins+"\n\tspecies: "+numSpecies+"\n");
        }
        fileWrite.append(stringBuilder.toString());
        fileWrite.close();
    }

    public void findPathwaysWithFewReactions(String folder, int maxNumReactions) throws IOException, XMLStreamException {
        File folderFile = new File(folder);
        SBMLParser parser = new SBMLParser(true);

        for(String pathway: folderFile.list()) {

            String pathwayFullPath = folder + "/" + pathway;
            File pathwayFile = new File(pathwayFullPath);
            SBase tree = parser.getContent(pathwayFullPath);

            Set<String> reaction_Ids = parser.getReactionIds(tree);
            int numReactions = (reaction_Ids).size();
            if (numReactions > maxNumReactions) {
                pathwayFile.delete();
            }
        }
    }

    public void cataloguePathways(String folder) throws IOException, XMLStreamException {
        File folderFile = new File(folder);
        SBMLParser sbmlParser = new SBMLParser(true);
        TSVParser tsvParser = new TSVParser();
        SabioMiner sabioMiner = new SabioMiner();

        class PathwayInfo {
            String pathwayName;
            String pathwayId;
            int numberOfSpecies;
            int numberOfProteins;
            int numberOfConstrainedProteins;
            int numberOfReactions;
            int numberOfConstrainedReactions;

            PathwayInfo(String pathwayName, String pathwayId, int numberOfSpecies, int numberOfProteins, int numberOfConstrainedProteins, int numberOfReactions, int numberOfConstrainedReactions) {
                this.pathwayName = pathwayName;
                this.pathwayId = pathwayId;
                this.numberOfSpecies = numberOfSpecies;
                this.numberOfProteins = numberOfProteins;
                this.numberOfConstrainedProteins = numberOfConstrainedProteins;
                this.numberOfReactions = numberOfReactions;
                this.numberOfConstrainedReactions = numberOfConstrainedReactions;
            }

            String getInfo() {
                String info = "Pathway id:" + "\t".repeat(7) + pathwayId + "\n" +
                        "Number of species:" + "\t".repeat(5) + numberOfSpecies + "\n" +
                        "Number of proteins:" + "\t".repeat(5) + numberOfProteins + "\n" +
                        "Number of constrained proteins:" + "\t".repeat(2) + numberOfConstrainedProteins + "\n" +
                        "Number of reactions:" + "\t".repeat(4) + numberOfReactions + "\n" +
                        "Number of constrained reactions:" + "\t" +
                        ((numberOfConstrainedReactions == -1)? "COULD NOT GATHER INFO FROM SABIO" : String.valueOf(numberOfConstrainedReactions));
                return info;
            }
        }

        List<PathwayInfo> pathwayInfos = new ArrayList<>();

        for (File pathway: folderFile.listFiles()) {
            if (!pathway.isDirectory()) continue;
            String pathwayName = pathway.getName();
            System.out.print("Catalogue of " + pathwayName + "... ");
            String pathwayFolderPath = folderFile + "/" + pathwayName;
            String pathwayPath = pathwayFolderPath + "/in/pathway.sbml";
            SBase tree = sbmlParser.getContent(pathwayPath);
            String pathwayId = tree.getModel().getId();
            int numberOfSpecies = tree.getModel().getListOfSpecies().size();
            int numberOfReactions = tree.getModel().getListOfReactions().size();
            int numberOfProteins = sbmlParser.getProteins(pathwayPath).size();

            String abundancePath = pathwayFolderPath+ "/in/abundances.tsv";
            int numberOfConstrainedProteins = tsvParser.getContent(abundancePath).size();

            Set<String> reactionIds = tree.getModel().getListOfReactions().stream()
                    .map(reaction -> reaction.getId())
                    .collect(Collectors.toSet());

            int numberOfConstrainedReactions;
            try {
                KnowledgeBase sabio = sabioMiner.mine(reactionIds);

                numberOfConstrainedReactions = (int)sabio.getLinkContainsSet().stream()
                        .map(link -> link.getKnowledgeAtom())
                        .count();
                sabio = null;

            } catch (PreconditionsException exc) {
                numberOfConstrainedReactions = -1;
            }

            PathwayInfo pathwayInfo = new PathwayInfo(pathwayName, pathwayId,
                    numberOfSpecies, numberOfProteins, numberOfConstrainedProteins,
                    numberOfReactions, numberOfConstrainedReactions);

            pathwayInfos.add(pathwayInfo);
            FileWriter fileWriter = new FileWriter(pathwayFolderPath + "/info.txt");

            fileWriter.write(pathwayInfo.getInfo());
            fileWriter.flush();
            fileWriter.close();
            System.out.print("done.\n");
        }

        System.out.print("Writing summary info... ");
        FileWriter fileWriter = new FileWriter(folderFile + "/info.txt");

        String info = "";
        pathwayInfos.sort(Comparator.comparing((PathwayInfo p) -> p.pathwayName));
        for (PathwayInfo pathwayInfo: pathwayInfos) {
            info += pathwayInfo.pathwayName + "\n";
            info += "\t" + pathwayInfo.getInfo().replace("\n", "\n\t") + "\n";
        }

        fileWriter.write(info);
        fileWriter.flush();
        fileWriter.close();
        System.out.print("done.\n");
    }
}
