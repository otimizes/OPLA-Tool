package br.otimizes.oplatool.architecture.representation;

import br.otimizes.isearchai.learning.MLElement;
import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import com.rits.cloning.Cloner;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.eclipse.uml2.uml.Interface;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Element representation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class Element implements Serializable, MLElement {

    private static final long serialVersionUID = 4736685073967472613L;

    protected String id;

    private String name;

    private VariationPoint variationPoint;

    private Variant variant;

    private final Set<Concern> concerns = new HashSet<>();

    private Set<Comment> comments = new HashSet<>();

    private String typeElement;

    private String namespace;

    private boolean belongsToGeneralization;

    private boolean freezedByCluster = false;

    private String posX = "0";

    private String posY = "0";

    private String globalPosX = "0";

    private String globalPosY = "0";

    private String height = "0";

    private String width = "0";

    private boolean mandatory = true;

    private boolean isStatic;

    private boolean isFinal;

    public Element(String name, Variant variant, String typeElement, String namespace, String id) {
        setId(id);
        setName(name);
        setVariant(variant);
        setTypeElement(typeElement);
        setNamespace(namespace);
    }

    public Element deepCopy() {
        try {
            return this.deepClone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Element deepClone() throws CloneNotSupportedException {
        Cloner cloner = new Cloner();
        Element pkg = cloner.deepClone(this);
        cloner = null;
        return pkg;
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

    public String getId() {
        return id;
    }

    public float getNumberId() {
        int minValue = Integer.MIN_VALUE;
        int maxValue = Integer.MAX_VALUE;
        int numberId = (this.getNamespace() + ":" + this.getTypeElement() + ":" + this.getName()).hashCode();
        //        return (numberId - minValue) / (maxValue - minValue);
        return numberId * 0.0000001f;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getTypeElement() {
        return this.typeElement;
    }

    public void setTypeElement(String typeElement) {
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

    public Set<Concern> getOwnConcerns() {
        if (concerns.isEmpty())
            return Collections.emptySet();
        return concerns;
    }

    public boolean containsConcern(Concern concern) {
        for (Concern ownConcern : getOwnConcerns()) {
            if (ownConcern.getName().equalsIgnoreCase(concern.getName()))
                return true;
        }
        return false;
    }

    public void addConcerns(List<String> concernsNames) throws ConcernNotFoundException {
        for (String name : concernsNames) addConcern(name);
    }

    public void addExternalConcern(Concern concern) {
        concerns.add(concern);
    }

    public void addConcern(String concernName) throws ConcernNotFoundException {
        Concern concern = ConcernHolder.INSTANCE.getOrCreateConcern(concernName);
        concerns.add(concern);
    }

    public void removeConcern(String concernName) {
        Concern concern = ConcernHolder.INSTANCE.getConcernByName(concernName);
        concerns.remove(concern);
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public VariationPoint getVariationPoint() {
        return variationPoint;
    }

    public void setVariationPoint(VariationPoint variationPoint) {
        this.variationPoint = variationPoint;
    }

    public boolean doesBelongToGeneralization() {
        return this.belongsToGeneralization;
    }

    public void setBelongsToGeneralization(boolean belongsToGeneralization) {
        this.belongsToGeneralization = belongsToGeneralization;
    }

    public boolean isFreezeByDM() {
        return this.comments != null && this.comments.stream().anyMatch(c -> (c.getValue() != null && c.getValue().contains("freeze")) || (c.getName() != null && c.getName().contains("freeze")));
    }

    public boolean hasComments() {
        return !comments.isEmpty();
    }

    public boolean unsetFreezeFromDM() {
        this.comments = this.comments.stream().filter(c -> c.getValue() != null && !c.getValue().contains("freeze")).collect(Collectors.toSet());
        return this.isFreezeByDM();
    }

    public boolean setFreezeFromDM() {
        if (!isFreezeByDM()) {
            this.comments.add(new Comment("freeze"));
        }
        return isFreezeByDM();
    }

    public boolean setFreezeFromDM(boolean bool) {
        return bool ? setFreezeFromDM() : unsetFreezeFromDM();
    }

    public boolean setFreezeFromDM(double bool) {
        return bool > 0 ? setFreezeFromDM() : unsetFreezeFromDM();
    }

    public boolean isFreezedByCluster() {
        return freezedByCluster;
    }

    public void setFreezedByCluster(boolean freezedByCluster) {
        this.freezedByCluster = freezedByCluster;
    }

    public void setFreezedByCluster() {
        this.freezedByCluster = true;
    }

    public boolean isTotalyFreezed() {
        return this.isFreezeByDM() || this.isFreezedByCluster();
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public String getStringComments() {
        return comments.stream().map(c -> c.getValue() + "\n").collect(Collectors.joining());
    }

    public void setComments(String comments) {
        if (comments != null && !"null".equals(comments.trim()) && !comments.isEmpty()) {
            this.comments.add(new Comment(comments));
        }
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
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
        if (obj == null)
            return false;
        String objClass = obj.getClass().toString();
        if (this == obj)
            return true;
        if (!getClass().toString().equals(objClass))
            return false;
        Element other = (Element) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (namespace == null) {
            return other.namespace == null;
        } else
            return namespace.equals(other.namespace);
    }
}
