package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.exceptions.AttributeNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.MethodNotFoundException;
import br.otimizes.oplatool.architecture.flyweights.VariantFlyweight;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.papyrus.touml.Types.Type;
import br.otimizes.oplatool.architecture.papyrus.touml.VisibilityKind;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import com.rits.cloning.Cloner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Class extends Element implements Linkable {

    private static final long serialVersionUID = -5450511036321846093L;

    static Logger LOGGER = LogManager.getLogger(Class.class.getName());
    private final Set<Attribute> attributes = new HashSet<Attribute>();
    private final Set<Method> methods = new HashSet<Method>();
    private boolean isAbstract;
    private final Set<Interface> implementedInterfaces = new HashSet<Interface>();
    private final Set<Interface> requiredInterfaces = new HashSet<Interface>();

    private PatternsOperations patternsOperations;
    private RelationshipsHolder relationshipHolder;

    public Class(RelationshipsHolder relationshipHolder, String name, Variant variantType, boolean isAbstract,
                 String namespace, String id) {
        super(name, variantType, "klass", namespace, id);
        setAbstract(isAbstract);
        setRelationshipHolder(relationshipHolder);
        this.setPatternOperations(new PatternsOperations());
    }

    public Class(RelationshipsHolder relationshipHolder, String name, boolean isAbstract) {
        this(relationshipHolder, name, null, isAbstract, UtilResources.createNamespace(ArchitectureHolder.getName(),
                name), UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public Class(RelationshipsHolder relationshipHolder, String name, boolean isAbstract, String packageName) {
        this(relationshipHolder, name, null, isAbstract, UtilResources.createNamespace(ArchitectureHolder.getName(),
                packageName), UtilResources.getRandomUUID());
        this.setPatternOperations(new PatternsOperations());
    }

    public void matchImplementedInterface(Architecture architecture) {
        if (implementedInterfaces == null)
            return;
        if (implementedInterfaces.size() == 0)
            return;
        ArrayList<String> id_list = new ArrayList<>();
        for (Interface inter : implementedInterfaces) {
            id_list.add(inter.getId());
        }
        implementedInterfaces.clear();
        for (String id : id_list) {
            Interface interface_arch = architecture.findInterfaceById(id);
            if (interface_arch != null) {
                implementedInterfaces.add(interface_arch);
            }
        }
    }

    public void matchRequiredInterface(Architecture architecture) {
        if (requiredInterfaces == null)
            return;
        if (requiredInterfaces.size() == 0)
            return;
        ArrayList<String> id_list = new ArrayList<>();
        for (Interface inter : requiredInterfaces) {
            id_list.add(inter.getId());
        }
        requiredInterfaces.clear();
        for (String id : id_list) {
            Interface interface_arch = architecture.findInterfaceById(id);
            if (interface_arch != null) {
                requiredInterfaces.add(interface_arch);
            }
        }
    }

    public Attribute findAttributeById(String id) {

        for (Attribute att : getAllAttributes())
            if (att.getId().equalsIgnoreCase(id))
                return att;
        return null;
    }

    public Method findMethodById(String id) {
        for (Method m : getAllMethods()) {
            if (m.getId().equalsIgnoreCase(id)) {
                return m;
            }
        }

        return null;
    }

    public void removeMethodByID(String id) {
        Set<Method> newHash = new HashSet<>();
        for (Method m : this.methods) {
            if (!m.getId().equals(id)) {
                newHash.add(m);
            }
        }
        this.methods.clear();
        this.methods.addAll(newHash);
    }

    public void removeAttributeByID(String id) {
        Set<Attribute> newHash = new HashSet<>();
        for (Attribute m : this.attributes) {
            if (!m.getId().equals(id)) {
                newHash.add(m);
            }
        }
        this.attributes.clear();
        this.attributes.addAll(newHash);
    }

    public boolean hasGeneralization() {
        for (Relationship r : this.getRelationships()) {
            if (r instanceof GeneralizationRelationship) {
                GeneralizationRelationship re1 = (GeneralizationRelationship) r;
                if ((re1.getParent() != null) && (re1.getChild() != null)) {
                    if (re1.getParent().getId().equals(this.getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Attribute createAttribute(String name, Type type, VisibilityKind visibility) {
        String id = UtilResources.getRandomUUID();
        Attribute a = new Attribute(name, visibility.toString(), type.getName(), ArchitectureHolder.getName() + "::"
                + this.getName(), id);
        addExternalAttribute(a);
        return a;
    }

    public void setAttribute(Attribute attr) {
        this.attributes.add(attr);
    }

    public Set<Attribute> getAllAttributes() {
        if (attributes.isEmpty())
            return Collections.emptySet();
        return Collections.unmodifiableSet(attributes);
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public Set<Method> getAllMethods() {
        if (methods.isEmpty())
            return Collections.emptySet();
        return Collections.unmodifiableSet(methods);
    }

    public Attribute findAttributeByName(String name) throws AttributeNotFoundException {
        String message = "atributo '" + name + "' não encontrado na classe '" + this.getName() + "'.\n";

        for (Attribute att : getAllAttributes())
            if (name.equalsIgnoreCase(att.getName()))
                return att;
        LOGGER.info(message);
        throw new AttributeNotFoundException(message);
    }

    public boolean moveAttributeToClass(Attribute attribute, Class destinationKlass) {
        if (!destinationKlass.addExternalAttribute(attribute))
            return false;

        if (!removeAttribute(attribute)) {
            destinationKlass.removeAttribute(attribute);
            return false;
        }
        attribute.setNamespace(ArchitectureHolder.getName() + "::" + destinationKlass.getName());
        LOGGER.info("Moveu atributo: " + attribute.getName() + " de " + this.getName() + " para "
                + destinationKlass.getName());
        return true;
    }

    /**
     * Move um método de uma classe para outra.
     *
     * @param method           - Método a ser movido
     * @param destinationKlass - Classe que irá receber o método
     * @return -false se o método a ser movido não existir na classe.<br/>
     * -true se o método for movido com sucesso.
     */
    public boolean moveMethodToClass(Method method, Class destinationKlass) {
        if (!destinationKlass.addExternalMethod(method))
            return false;

        if (!removeMethod(method)) {
            destinationKlass.removeMethod(method);
            return false;
        }

        method.setNamespace(ArchitectureHolder.getName() + "::" + destinationKlass.getName());
        LOGGER.info("Moveu método: " + method.getName() + " de " + this.getName() + " para "
                + destinationKlass.getName());
        return true;
    }

    public Method createMethod(String name, String type, boolean isAbstract, List<ParameterMethod> parameters) {
        if (!methodExistsOnClass(name, type)) {
            String id = UtilResources.getRandomUUID();
            Method method = new Method(name, type, this.getName(), isAbstract, id);
            if (parameters != null)
                method.getParameters().addAll(parameters);
            methods.add(method);
            return method;
        }
        return null;
    }

    private boolean methodExistsOnClass(String name, String type) {
        for (Method m : getAllMethods()) {
            if ((name.equalsIgnoreCase(m.getName())) && (type.equalsIgnoreCase(m.getReturnType()))) {
                LOGGER.info("Método '" + name + ":" + type + "' já existe na classe: '" + this.getName() + "'.\n");
                return true;
            }
        }

        return false;
    }

    // TODO verificar metodos com mesmo nome e tipo diferente
    public Method findMethodByName(String name) throws MethodNotFoundException {
        String message = "Método '" + name + "' não encontrado na classe '" + this.getName() + "'.\n";

        for (Method m : getAllMethods())
            if ((name.equalsIgnoreCase(m.getName())))
                return m;

        LOGGER.info(message);
        throw new MethodNotFoundException(message);
    }

    public boolean addExternalMethod(Method method) {
        if (methods.add(method)) {
            LOGGER.info("Metodo " + method.getName() + " adicionado na classe " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover o Método: " + method.getName() + " da classe: " + this.getName()
                    + " porém não consegiu");
            return false;
        }

    }

    public boolean addExternalAttribute(Attribute a) {
        if (this.attributes.add(a)) {
            LOGGER.info("Atributo: " + a.getName() + " adicionado na classe: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover o Atributo: " + a.getName() + " da classe: " + this.getName()
                    + " porém não consegiu");
            return false;
        }
    }

    /**
     * Remove Método da classe
     *
     * @param method - Método a ser removido.
     * @return -true se o método for removido.<br/>
     * -false se método não existir na classe.
     */
    public boolean removeMethod(Method method) {
        if (this.methods.remove(method)) {
            LOGGER.info("Método: " + method.getName() + " removido da classe: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover o método: " + method.getName() + " classe: " + this.getName()
                    + " porém não consegiu");
            return false;
        }
    }

    /**
     * @param attribute
     * @return
     */
    public boolean removeAttribute(Attribute attribute) {
        if (this.attributes.remove(attribute)) {
            LOGGER.info("Atributo: " + attribute.getName() + " removido da classe: " + this.getName());
            return true;
        } else {
            LOGGER.info("TENTOU remover o atributo: " + attribute.getName() + " classe: " + this.getName()
                    + " porém não consegiu");
            return false;
        }
    }

    public List<Method> getAllAbstractMethods() {
        List<Method> abstractMethods = new ArrayList<Method>();

        for (Method m : getAllMethods())
            if (m.isAbstract())
                abstractMethods.add(m);

        return abstractMethods;
    }

    // public boolean dontHaveAnyRelationship() {
    //
    // if(getRelationshipHolder().getRelationships().isEmpty()){
    // return true;
    // }else{
    // return false;
    // }
    // }

    public void updateId(String id) {
        super.id = id;

    }

    public Object getAllStereotype() {
        return null;
    }

    /**
     * Retorna o tipo de variant (ex: alternative_OR).<br/>
     * <p>
     * Retorna null se não existir
     *
     * @return String (ex: alternative_OR).
     */
    public String getVariantType() {
        for (Variant v : VariantFlyweight.getInstance().getVariants()) {
            if (v.getName().equalsIgnoreCase(this.getName()))
                return v.getVariantType();
        }

        return null;
    }

    /**
     * Retorna todo os interesses da classe, como "todos", entende-se:<br/>
     * <ul>
     * <li>Interesses anotados na classe</li>
     * <li>Interesses anotados nos atributos e métodos da classe</li>
     * <li>Interesses anotados nas interesses que a classe implementa</li>
     * </ul>
     *
     * @return {@link Set} Imutável
     */
    @Override
    public Set<Concern> getAllConcerns() {
        Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns());

        for (Method method : getAllMethods())
            concerns.addAll(method.getAllConcerns());
        for (Attribute attribute : getAllAttributes())
            concerns.addAll(attribute.getAllConcerns());
        for (Interface inte : this.getImplementedInterfaces())
            concerns.addAll(inte.getAllConcerns());

        return Collections.unmodifiableSet(concerns);
    }

    public List<AssociationClassRelationship> getAllAssociationClass() {
        List<AssociationClassRelationship> associationsClasses = new ArrayList<AssociationClassRelationship>();

        for (Relationship r : getRelationshipHolder().getRelationships()) {
            if (r instanceof AssociationClassRelationship) {
                AssociationClassRelationship asc = (AssociationClassRelationship) r;
                if (asc.getAssociationClass().equals(this))
                    associationsClasses.add((AssociationClassRelationship) r);
                for (MemberEnd member : asc.getMembersEnd()) {
                    if (member.getType().equals(this))
                        associationsClasses.add((AssociationClassRelationship) r);
                }
            }
        }

        return associationsClasses;
    }

    public Set<Interface> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public Set<Interface> getRequiredInterfaces() {
        return requiredInterfaces;
    }

    public void removeImplementedInterface(Interface inter) {
        this.implementedInterfaces.remove(inter);
    }

    public boolean removeRequiredInterface(Interface supplier) {
        if (!requiredInterfaces.contains(supplier))
            return false;
        requiredInterfaces.remove(supplier);
        return true;
    }

    public void addImplementedInterface(Interface supplierElement) {
        this.implementedInterfaces.add(supplierElement);
    }

    public void addRequiredInterface(Interface supplier) {
        this.requiredInterfaces.add(supplier);
    }

    public Class deepCopy() {
        try {
            return this.deepClone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class deepClone() throws CloneNotSupportedException {
        Cloner cloner = new Cloner();
        Class klass = cloner.deepClone(this);
        cloner = null;
        return klass;
    }

    public RelationshipsHolder getRelationshipHolder() {
        return relationshipHolder;
    }

    public void setRelationshipHolder(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
    }

    public Set<Relationship> getRelationships() {
        if (relationshipHolder == null) return new HashSet<>();
        return RelationshipCommons.getRelationships(relationshipHolder.getRelationships(), this);
    }

    public void setPatternOperations(PatternsOperations patternOperations) {
        this.patternsOperations = patternOperations;
    }

    public PatternsOperations getPatternsOperations() {
        return this.patternsOperations;
    }

    public Set<Relationship> getGeneralizations() {
        return getRelationships().stream().filter(r -> r instanceof GeneralizationRelationship).collect(Collectors.toSet());
    }

    public Set<Concern> getAllConcernsWithoutImplementedInterfaces() {
        Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns());

        for (Method method : getAllMethods())
            concerns.addAll(method.getAllConcerns());
        for (Attribute attribute : getAllAttributes())
            concerns.addAll(attribute.getAllConcerns());

        return concerns;
    }

    public Set<Attribute> getAllModifiableAttributes() {
        if (attributes.isEmpty())
            return Collections.emptySet();
        return attributes;
    }

    public Set<Method> getAllModifiableMethods() {
        if (methods.isEmpty())
            return Collections.emptySet();
        return methods;
    }

    public List<Method> getAllModifiableAbstractMethods() {
        List<Method> abstractMethods = new ArrayList<Method>();

        for (Method m : getAllModifiableMethods())
            if (m.isAbstract())
                abstractMethods.add(m);

        return abstractMethods;
    }

    public Set<Concern> getPriConcerns() {
        Set<Concern> concerns = new HashSet<Concern>(getOwnConcerns()); //titulo
        for (Method method : getAllMethods()) { //para cada metodo da classe
            for (Concern stereotype : method.getAllConcerns()) { //pra cada esteriotipo do metodo.
                if (!listContainConcern(concerns, stereotype)) //verifica se o esteriotipo esta presente na lista, se nao estiver
                    concerns.add(stereotype); //ele add na lista.
            }
        }

        for (Attribute attribute : getAllAttributes()) { //para cada atributo da classe
            for (Concern stereotype : attribute.getAllConcerns()) { //pra cada esteriotipo do atributo.
                if (!listContainConcern(concerns, stereotype)) //verifica se o esteriotipo esta presente na lista, se nao estiver
                    concerns.add(stereotype);//ele add na lista.
            }
        }
        return concerns;
    }

    public boolean listContainConcern(Set<Concern> listConcerns, Concern concern) {
        for (Concern conc : listConcerns) {
            if (conc.getName().equalsIgnoreCase(concern.getName()))
                return true;
        }
        return false;
    }

}