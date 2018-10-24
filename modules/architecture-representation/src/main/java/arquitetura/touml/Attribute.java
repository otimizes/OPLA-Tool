package arquitetura.touml;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.Concern;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Attribute {

    private String id;
    private String name;
    private VisibilityKind visibility;
    private Types.Type type;
    private Set<Concern> concerns = new HashSet<Concern>();
    private boolean generateVisualAttribute;

    public static Attribute create() {
        Attribute attr = new Attribute();
        attr.setId(UtilResources.getRandonUUID());
        return attr;

    }

    /**
     * @return the generateVisualAttribute
     */
    public boolean isGenerateVisualAttribute() {
        return generateVisualAttribute;
    }

    /**
     * @return the concerns
     */
    public Set<Concern> getConcerns() {
        return Collections.unmodifiableSet(concerns);
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    private void setId(String randonUUID) {
        this.id = randonUUID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the visibility
     */
    public String getVisibility() {
        return visibility.getName();
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(VisibilityKind visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type.getName();
    }

    /**
     * @param type the type to set
     */
    public void setType(Types.Type type) {
        this.type = type;
    }

    public Attribute withName(String name) {
        this.name = name;
        return this;
    }

    public Attribute withVisibility(VisibilityKind visibility) {
        this.visibility = visibility;
        return this;
    }

    public Attribute withType(Types.Type type) {
        this.type = type;
        return this;
    }

    public Attribute withConcerns(Set<Concern> ownConcerns) {
        this.concerns = ownConcerns;
        return this;
    }

    /**
     * Gera ou não graticamente o atributo
     *
     * @param generatVisualAttribute
     * @return
     */
    public Attribute grafics(boolean generatVisualAttribute) {
        this.generateVisualAttribute = generatVisualAttribute;
        return this;
    }

}