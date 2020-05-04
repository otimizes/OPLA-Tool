package arquitetura.representation;

import arquitetura.exceptions.ConcernNotFoundException;

import java.io.Serializable;
import java.util.*;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public abstract class Element implements Serializable {

    private static final long serialVersionUID = 4736685073967472613L;

    protected String id;
    private String name;
    private VariationPoint variationPoint;
    private Variant variant;
    private Set<Concern> concerns = new HashSet<Concern>();
    private Architecture architecture;
    private String typeElement;
    private String namespace;
    private boolean belongsToGeneralization;

    private String posX = "0";
    private String posY = "0";
    private String globalPosX = "0";
    private String globalPosY = "0";
    private String height = "0";
    private String width = "0";
    private boolean mandatory = true;
    private boolean isStatic;
    private  boolean isFinal;


    public Element(String name, Variant variant, String typeElement, String namespace, String id) {
        setId(id);
        setName(name);
        setVariant(variant);
        setTypeElement(typeElement);
        setNamespace(namespace);
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getGlobalPosX() {
        return globalPosX;
    }

    public void setGlobalPosX(String globalPosX) {
        this.globalPosX = globalPosX;
    }

    public String getGlobalPosY() {
        return globalPosY;
    }

    public void setGlobalPosY(String globalPosY) {
        this.globalPosY = globalPosY;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public abstract Collection<Concern> getAllConcerns();

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    private void setId(String id) {
        this.id = id;
    }

    public String getTypeElement() {
        return this.typeElement;
    }

    private void setTypeElement(String typeElement) {
        this.typeElement = typeElement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isVariationPoint() {
        return this.getVariationPoint() != null;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Retorna apenas os interesses pertencentes a este elemento.<br />
     *
     * @return List<{@link Concern}>
     */
    public Set<Concern> getOwnConcerns() {
        if (concerns == null || concerns.isEmpty())
            return Collections.emptySet();
        return concerns;
    }

    public boolean containsConcern(Concern concern) {
        for (Concern conc : getOwnConcerns()) {
            if (conc.getName().equalsIgnoreCase(concern.getName()))
                return true;
        }
        return false;
    }

    public void addConcerns(List<String> concernsNames) throws ConcernNotFoundException {
        for (String name : concernsNames)
            addConcern(name);
    }

    /**
     * Adiciona um interesse a classe.<br/>
     * <p>
     * Somente irá incluir o interesse se o mesmo estive presente no arquivo de
     * perfil que contêm os interesses</br>
     *
     * @param concernName
     * @throws ConcernNotFoundException
     */
    public void addConcern(String concernName) throws ConcernNotFoundException {
        Concern concern = ConcernHolder.INSTANCE.getOrCreateConcern(concernName);
        concerns.add(concern);
    }

    public void addExternalConcern(Concern concern){
        if(!concerns.contains(concern))
            concerns.add(concern);
    }

    public void removeConcern(String concernName) {
        Concern concern = ConcernHolder.INSTANCE.getConcernByName(concernName);
        concerns.remove(concern);
    }

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    /**
     * @return the variationPoint
     */
    public VariationPoint getVariationPoint() {
        return variationPoint;
    }

    /**
     * @param variationPoint the variationPoint to set
     */
    public void setVariationPoint(VariationPoint variationPoint) {
        this.variationPoint = variationPoint;
    }

    public boolean belongsToGeneralization() {
        return this.belongsToGeneralization;
    }

    public void setBelongsToGeneralization(boolean belongsToGeneralization) {
        this.belongsToGeneralization = belongsToGeneralization;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        String objClass = obj.getClass().toString();
        if (this == obj)
            return true;
        if (!getClass().toString().equals(objClass.toString()))
            return false;
        Element other = (Element) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (namespace == null) {
            if (other.namespace != null)
                return false;
        } else if (!namespace.equals(other.namespace))
            return false;
        return true;
    }

}