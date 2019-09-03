package Miner;

import DataTypes.PreconditionsException;
import DataTypes.RateParameter;
import DataTypes.RealInterval;
import InputKnowledge.KnowledgeBase;
import InputKnowledge.ReactionKA;
import org.sbml.jsbml.LocalParameter;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBase;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static Util.Utils.containsIgnoreCase;


public class SabioMiner implements Miner {
    private static final String sabioUrl = "http://sabiork.h-its.org/sabioRestWebServices/searchKineticLaws/sbml?";
    private static HttpURLConnection con;
    private static final String EMPTY_QUERY_MESSAGE = "No results found for query\n";

    @Override
    public KnowledgeBase mine(Set<String> ka_ids) throws IOException, XMLStreamException, PreconditionsException {
        KnowledgeBase kb = new KnowledgeBase("Sabio",sabioUrl);
        for(String ka_id: ka_ids) {
            String idNumber = ka_id.replace("reaction_","");
            SBase tree = request(idNumber);
            if (tree != null){
                Map<String, Double> params = parseParameters(tree, kb);
                ReactionKA reactionKA = new ReactionKA(ka_id, false, kb);
                for (Map.Entry<String, Double> entry : params.entrySet()) {
                    if (containsIgnoreCase(entry.getKey(), "Km")){
                        RealInterval realInt = new RealInterval(entry.getValue(), entry.getValue());
                        reactionKA.addRateParameter(RateParameter.Km,realInt);
                    }
                    else if (containsIgnoreCase(entry.getKey(), "Kcat")){
                        RealInterval realInt = new RealInterval(entry.getValue(), entry.getValue());
                        reactionKA.addRateParameter(RateParameter.Kcat, realInt);
                    }
                }
            }
        }
        return kb;
    }




    public SBase request(String reactionId) throws IOException, XMLStreamException {
        URL url = new URL(sabioUrl);
        String urlParameters = "q=ReactomeReactionID%3A%22R-HSA-"+reactionId+"%22%20AND%20Organism%3A%22human%22%20AND%20Parametertype%3A%22km%22%20AND%20Parametertype%3A%22kcat%22%20AND%20KineticMechanismType%3A%22michaelis-menten%22";
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        con = (HttpURLConnection) url.openConnection();

        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Java client");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }

        StringBuilder content;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {

            String line;
            content = new StringBuilder();

            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }
        if(content.toString().equals(EMPTY_QUERY_MESSAGE)){
           return null;
        }
        SBase tree = SBMLReader.read(content.toString());
        return tree;
    }


    private Map<String, Double> parseParameters(SBase tree, KnowledgeBase kb) {
        Map<String, Double> map = new HashMap<>();
        List<LocalParameter> params = tree.getModel().getListOfReactions().get(0).getKineticLaw().getListOfLocalParameters();
        for(LocalParameter param:params){
            String id = param.getId();
            Double value = param.getValue();
            map.put(id, value);
        }
        return map;

    }
}
