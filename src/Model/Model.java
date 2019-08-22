package Model;

import DataTypes.PreconditionsException;
import Util.BiologicalEntityJSONAdapter;
import Util.MyExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Model {

    private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    private Set<LinkTypeComprises> linkComprisesSet = new HashSet<>();

    public void overrideModel(Model overrider) throws PreconditionsException{
        for (LinkTypeComprises linkComprises: overrider.getLinkComprisesSet()){
            this.overrideEntity(linkComprises.getBiologicalEntity());
        }
    }

    public BiologicalEntity getBioEntityById(String id){
        for (LinkTypeComprises link : linkComprisesSet) {
            BiologicalEntity be = link.getBiologicalEntity();
            if (be.getId().equals(id)) return be;
        }
        return null;
    }

    public void overrideEntity(BiologicalEntity other) throws PreconditionsException{
        LOGGER.info("overriding "+other.getId());
        boolean exists = false;
        for (LinkTypeComprises link: this.getLinkComprisesSet()) {
            BiologicalEntity modelBioEntity = link.getBiologicalEntity();
            if ( modelBioEntity.getId().equals(other.getId()) ) {
                LOGGER.info("BioEntity already exists in model");
                modelBioEntity.override(other);
                exists = true;
                break;
            }
        }
        if(!exists){
            LOGGER.info("BioEntity doesn't exist in model");
            other.cloneIntoModel(this);
        }
    }

    // Comprises association (without attributes), of which BiologicalEntity and Model are both responsible. (n to 1)

    public void insertLinkComprises(LinkComprises pass, LinkTypeComprises l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkComprises to invoke this method");
        linkComprisesSet.add(l);
        LOGGER.info("Added bidirectional link: "+l.getBiologicalEntity().getId()+" to model");

    }

    public void removeLinkComprises(LinkComprises pass, LinkTypeComprises l)
            throws PreconditionsException {
        if (pass == null)
            throw new PreconditionsException(
                    "It is necessary to show an instance of LinkComprises to invoke this method");
        linkComprisesSet.remove(l);
        LOGGER.info("Removed bidirectional link: "+l.getBiologicalEntity().getId()+" to "+l.getModel());
    }

    public Set<LinkTypeComprises> getLinkComprisesSet() {
        return (Set<LinkTypeComprises>)((HashSet<LinkTypeComprises>)linkComprisesSet).clone();
    }

//    public void dump(String path) throws IOException {
//        Gson g = new GsonBuilder()
//                .setExclusionStrategies(new MyExclusionStrategy())
//                .registerTypeAdapter(BiologicalEntity.class,
//                        new BiologicalEntityJSONAdapter())
//                .create();
//
//        String json = g.toJson(this);
//        File f = new File(path);
//        FileWriter fr = new FileWriter(f);
//        fr.write(json);
//        fr.close();
//    }
//

    public void dump(String path) throws IOException {
        XStream xStream = new XStream();
        String xml = xStream.toXML(this);
        File f = new File(path);
        FileWriter fr = new FileWriter(f);
        fr.write(xml);
        fr.close();
    }

    public static Model load(String path) throws IOException {
        Gson g = new GsonBuilder()
                .setExclusionStrategies(new MyExclusionStrategy())
                .registerTypeAdapter(BiologicalEntity.class,
                        new BiologicalEntityJSONAdapter())
                .create();

        JsonElement jelement = new JsonParser().parse(new FileReader(path));
        return g.fromJson(jelement, Model.class);
    }
}
