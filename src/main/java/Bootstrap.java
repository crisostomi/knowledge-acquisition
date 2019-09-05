import InputKnowledge.ProteinKA;
import Parser.SBMLParser;
import Parser.TSVParser;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bootstrap {

    public static void joinAbundances(String sbmlPath, String tsvPath, String outputPath) throws IOException, XMLStreamException {
        SBMLParser sbmlParser = new SBMLParser(true);
        Set<String> externalIds = sbmlParser.getProteinsExternalIds(sbmlPath);
        TSVParser tsvParser = new TSVParser();
        List<String[]> rows = tsvParser.getContent(tsvPath);
        StringBuilder output = new StringBuilder();
        for(String id:externalIds){
            for (String[] row: rows){
                String externalID =  row[1].split("\\|")[0];
                if (externalID.equals(id)){
                    output.append(row[0] + "\t"+row[1]+"\t"+row[2]+"\n");
                }
            }
        }
        File outputFile = new File(outputPath);
        outputFile.createNewFile();
        FileWriter writer = new FileWriter(outputFile);
        writer.write(output.toString());
        writer.flush();
        writer.close();

    }
}
