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
import java.net.URLEncoder;
import java.util.logging.Logger;


public class SabioMiner implements Miner {
    private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    private static final String sabioUrl = "http://sabiork.h-its.org/sabioRestWebServices/searchKineticLaws/sbml?";
    private static HttpURLConnection con;
    private static final String EMPTY_QUERY_MESSAGE = "No results found for query\n";

    @Override
    public KnowledgeBase mine(Set<String> reaction_ids) throws IOException, XMLStreamException, PreconditionsException {
        KnowledgeBase kb = new KnowledgeBase("Sabio",sabioUrl);

        for(String reaction_id: reaction_ids) {
            String idNumber = reaction_id.replace("reaction_","");

            if (informationAvailable(idNumber)){

                for (RateParameter rateParam: RateParameter.values()){

                    Map<String, Double> params = retrieveRateParameter(idNumber, rateParam);
                    if (params != null){
                        ReactionKA reactionKA = new ReactionKA(reaction_id, false, kb);
                        for (Map.Entry<String, Double> entry : params.entrySet()) {
                            if (containsIgnoreCase(entry.getKey(), rateParam.name())){
                                RealInterval realInt = new RealInterval(entry.getValue(), entry.getValue());
                                reactionKA.addRateParameter(rateParam,realInt);
                            }
                        }
                    }

                }
            }
        }
        return kb;
    }

    public Map<String, Double> retrieveRateParameter(String reactionId, RateParameter rateParam) throws IOException, XMLStreamException {
        String query = getQueryRateParameter(reactionId, rateParam);
        SBase response = request(query);
        if (response != null){
            Map<String, Double> params = parseParameters(response);
            return params;
        }
        return null;
    }

    public String getQueryKmAndKcat(String reactionId){
        String organism = "human";
        String query = "ReactomeReactionID:\"R-HSA-"+reactionId+"\" AND Organism:\""+organism+"\" AND Parametertype:\"km\" AND Parametertype: \"kcat\"";
        return "q="+URLEncoder.encode(query);
    }

    public String getQueryRateParameter(String reactionId, RateParameter rateParam){
        String rateParamName;
        rateParamName = rateParam.name().equals("Kcat_over_Km")? "kcat/km":rateParam.name();
        String organism = "human";
        String query = "ReactomeReactionID:\"R-HSA-"+reactionId+"\" AND Organism:\""+organism+"\" AND Parametertype:\""+rateParamName.toLowerCase()+"\"";
        return "q="+URLEncoder.encode(query);
    }

    public SBase request(String query) throws IOException, XMLStreamException {
        URL url = new URL(sabioUrl);
        byte[] postData = query.getBytes(StandardCharsets.UTF_8);

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

    private Map<String, Double> parseParameters(SBase tree) {
        Map<String, Double> map = new HashMap<>();
        List<LocalParameter> params = tree.getModel().getListOfReactions().get(0).getKineticLaw().getListOfLocalParameters();
        for(LocalParameter param:params){
            String id = param.getId();
            Double value = param.getValue();
            map.put(id, value);
        }
        return map;

    }

    public boolean informationAvailable(String reactionId) throws IOException {
        String organism = "human";
        String query = "q="+URLEncoder.encode("ReactomeReactionID:\"R-HSA-"+reactionId+"\" AND Organism:\""+organism+"\"");
        URL url = new URL(sabioUrl);
        byte[] postData = query.getBytes(StandardCharsets.UTF_8);
        con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Java client");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String line;
            line = in.readLine();
            line += "\n";
            if (line.equals(EMPTY_QUERY_MESSAGE)){
                return false;
            }
            else{
                return true;
            }
        }
    }

}
