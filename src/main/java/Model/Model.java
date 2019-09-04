package Model;

import DataTypes.PreconditionsException;
import Model.Link.LinkComprises;
import Model.LinkType.LinkTypeComprises;
import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Model implements Serializable {

    private static final Logger LOGGER = Logger.getLogger( Logger.GLOBAL_LOGGER_NAME );

    private CellType cellType = null;
    private Set<LinkTypeComprises> linkComprisesSet = new HashSet<>();

    public Model(){}

    public Model(CellType cellType) {
        this.cellType = cellType;
    }

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

    public void consolidateProteins(){
        for (Protein protein1: this.getProteins()){
            for (Protein protein2: this.getProteins()){
                if (!(protein1.getId().equals(protein2.getId()))){
                    HashSet<String> intersection = new HashSet<>(protein1.getExternalIds());
                    intersection.retainAll(protein2.getExternalIds());
                    if (! intersection.isEmpty() ){
                        try {
                            if (protein1.getLinkReactantSet().isEmpty() && protein1.getLinkProductSet().isEmpty() && protein1.getLinkModifierSet().isEmpty()) {
                                protein2.merge(protein1);
                                LinkTypeComprises link1 = new LinkTypeComprises(this, protein1);
                                LinkComprises.removeLink(link1);
                            } else {
                                protein1.merge(protein2);
                                LinkTypeComprises link2 = new LinkTypeComprises(this, protein2);
                                LinkComprises.removeLink(link2);
                            }
                        } catch (PreconditionsException e) {}
                    }
                }
            }
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

    // Getters and setters

    public void removeCellType() {
        this.cellType = null;
    }

    public void setCellType(CellType cellType) throws PreconditionsException {
        if (this.cellType != null) throw new PreconditionsException();
        this.cellType = cellType;
    }

    public CellType getCellType() {
        return cellType;
    }

    public Set<BiologicalEntity> getBiologicalEntities(){
        Set<BiologicalEntity> biologicalEntities = new HashSet<BiologicalEntity>();
        for ( LinkTypeComprises linkComprises: this.linkComprisesSet ){
            biologicalEntities.add(linkComprises.getBiologicalEntity());
        }
        return biologicalEntities;
    }

    public Set<Reaction> getReactions(){
        Set<Reaction> reactions = new HashSet<>();
        for (BiologicalEntity bioEntity: this.getBiologicalEntities()){
            if (bioEntity instanceof Reaction){
                Reaction reaction = (Reaction) bioEntity;
                reactions.add(reaction);
            }
        }
        return reactions;
    }

    public Set<Compartment> getCompartments(){
        Set<Compartment> compartments = new HashSet<>();
        for (BiologicalEntity bioEntity: this.getBiologicalEntities()){
            if (bioEntity instanceof Compartment){
                Compartment compartment = (Compartment) bioEntity;
                compartments.add(compartment);
            }
        }
        return compartments;
    }

    public Set<Species> getSpecies(){
        Set<Species> speciesSet = new HashSet<>();
        for (BiologicalEntity bioEntity: this.getBiologicalEntities()){
            if (bioEntity instanceof Species){
                Species species = (Species) bioEntity;
                speciesSet.add(species);
            }
        }
        return speciesSet;
    }

    public Set<Protein> getProteins(){
        Set<Protein> proteins = new HashSet<>();
        for(Species species: this.getSpecies()){
            if (species instanceof Protein){
                proteins.add((Protein) species);
            }
        }
        return proteins;
    }

    // Serialization functions

    public void dump(String path) throws IOException {
        XStream xStream = new XStream();
        String xml = xStream.toXML(this);
        File f = new File(path);
        FileWriter fr = new FileWriter(f);
        fr.write(xml);
        fr.close();
    }

    public static Model load(String path) throws IOException {
        Path filePath = Paths.get(path);
        String deserializedXML = Files.readString(filePath, StandardCharsets.US_ASCII);
        XStream xStream = new XStream();
        Model deserializedModel = (Model) xStream.fromXML(deserializedXML);

        return deserializedModel;
    }


}
