package br.ufpr.dinf.gres.architecture.representation;

import br.ufpr.dinf.gres.architecture.exceptions.ClassNotFound;
import br.ufpr.dinf.gres.architecture.flyweights.VariabilityFlyweight;
import br.ufpr.dinf.gres.architecture.flyweights.VariantFlyweight;
import br.ufpr.dinf.gres.architecture.flyweights.VariationPointFlyweight;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.domain.config.ApplicationFileConfigThreadScope;
import br.ufpr.dinf.gres.architecture.main.GenerateArchitecture;
import br.ufpr.dinf.gres.architecture.main.GenerateArchitectureSMarty;
import br.ufpr.dinf.gres.architecture.representation.architectureControl.ArchitectureFindElementControl;
import br.ufpr.dinf.gres.architecture.representation.architectureControl.ArchitectureRemoveElementControl;
import br.ufpr.dinf.gres.architecture.representation.relationship.DependencyRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.RealizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;
import br.ufpr.dinf.gres.common.Variable;
import com.rits.cloning.Cloner;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Architecture extends Variable {

    private static Logger LOGGER = Logger.getLogger(Architecture.class);
    private static final long serialVersionUID = -7764906574709840088L;
    public static String ARCHITECTURE_TYPE = "br.ufpr.dinf.gres.architecture.representation.Architecture";
    private Cloner cloner;
    private Set<Package> packages = new HashSet<Package>();
    private Set<Class> classes = new HashSet<Class>();
    private Set<Interface> interfaces = new HashSet<Interface>();
    private String name;
    private boolean appliedPatterns;

    private RelationshipsHolder relationshipHolder = new RelationshipsHolder();


    // MAMORU
    private ArrayList<Concern> lstConcerns = new ArrayList<>();
    private ArrayList<TypeSmarty> lstTypes = new ArrayList<>();
    private boolean isSMarty = false;
    private boolean toSMarty = false;   // variável para dizer se utiliza o decoding para SMarty ou não
    private String projectID = "5b729c3f25e758ce87cf8d710761283c";
    private String projectName = "Project0";
    private String projectVersion = "1.0";
    private String diagramID = "DIAGRAM#1";
    private String diagramName = "DIAGRAM#1";
    private List<VariationPoint> lstVariationPoint = new ArrayList<>();
    private List<Variability> lstVariability = new ArrayList<>();
    private List<Variant> lstVariant = new ArrayList<>();
    //

    public Architecture(String name) {
        setName(name);
    }

    public ArrayList<Concern> getLstConcerns() {
        return lstConcerns;
    }

    public void setLstConcerns(ArrayList<Concern> lstConcerns) {
        this.lstConcerns = lstConcerns;
        ConcernHolder.INSTANCE.getConcerns().clear();
        ConcernHolder.INSTANCE.allowedConcerns().clear();
        for (Concern c : this.lstConcerns) {
            if (!c.getPrimitive()) {
                ConcernHolder.INSTANCE.getConcerns().put(c.getName(), c);
                ConcernHolder.INSTANCE.allowedConcerns().add(c);
            }
        }
    }

    public Concern findConcernByName(String name) {
        return ArchitectureFindElementControl.getInstance().findConcernByName(this, name);
    }

    /**
     * get a list of duplicated interfaces if has
     * @return list of duplicated interfaces
     */
    public ArrayList<Interface> getDuplicateInterface() {
        ArrayList<String> interfacesID = new ArrayList<>();
        ArrayList<Interface> interfacesDup = new ArrayList<>();
        for (Interface inter : getInterfaces()) {
            if (interfacesID.contains(inter.getId())) {
                interfacesDup.add(inter);
            }
            interfacesID.add((inter.getId()));
        }
        for (Package pkg : getAllPackages()) {
            for (Interface inter : pkg.getAllInterfaces()) {
                if (interfacesID.contains(inter.getId())) {
                    interfacesDup.add(inter);
                }
                interfacesID.add((inter.getId()));
            }
        }
        return interfacesDup;
    }

    public ArrayList<TypeSmarty> getLstTypes() {
        return lstTypes;
    }

    public void setLstTypes(ArrayList<TypeSmarty> lstTypes) {
        this.lstTypes = lstTypes;
    }

    public TypeSmarty findTypeSMartyByID(String id) {
        return ArchitectureFindElementControl.getInstance().findTypeSMartyByID(this, id);
    }

    public TypeSmarty findTypeSMartyByName(String name) {
        return  ArchitectureFindElementControl.getInstance().findTypeSMartyByName(this, name);
    }

    public TypeSmarty findReturnTypeSMartyByName(String name) {
        return  ArchitectureFindElementControl.getInstance().findReturnTypeSMartyByName(this, name);
    }

    public boolean isSMarty() {
        return isSMarty;
    }

    public void setSMarty(boolean SMarty) {
        isSMarty = SMarty;
    }

    public boolean isToSMarty() {
        return toSMarty;
    }

    public void setToSMarty(boolean toSMarty) {
        this.toSMarty = toSMarty;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    public String getDiagramID() {
        return diagramID;
    }

    public void setDiagramID(String diagramID) {
        this.diagramID = diagramID;
    }

    public String getDiagramName() {
        return diagramName;
    }

    public void setDiagramName(String diagramName) {
        this.diagramName = diagramName;
    }

    public Element findElementByNameInPackageAndSubPackage(String elementName) {
        return  ArchitectureFindElementControl.getInstance().findElementByNameInPackageAndSubPackage(this, elementName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    public List<Element> getElements() {
        final List<Element> elts = new ArrayList<Element>();

        for (Package p : getAllPackages())
            for (Element element : p.getElements())
                elts.add(element);

        for (Class c : this.classes)
            elts.add(c);
        for (Interface i : this.interfaces)
            elts.add(i);

        return elts;
    }

    public List<Element> getElementsWithPackages() {
        final List<Element> elts = new ArrayList<Element>();

        for (Package p : getAllPackagesAllowedMofification()) {
            elts.add(p);
            for (Element element : p.getElements())
                elts.add(element);
        }

        for (Class c : this.classes)
            elts.add(c);
        for (Interface i : this.interfaces)
            elts.add(i);

        return elts;
    }

    public List<Element> findElementByNumberId(Double idElem) {
        List<Element> elementsWithPackages = getElementsWithPackages();
        List<Element> collect = elementsWithPackages.stream().filter(e -> Double.valueOf(e.getNumberId()).equals(idElem)).collect(Collectors.toList());
        return collect;
    }

    /**
     * Retorna um Map imutável. É feito isso para garantir que nenhum modificação seja
     * feita diretamente na lista
     *
     * @return Map<String, Concern>
     */
    public List<Concern> getAllConcerns() {
        final List<Concern> concerns = new ArrayList<Concern>();
        for (Map.Entry<String, Concern> entry : ConcernHolder.INSTANCE.getConcerns().entrySet()) {
            concerns.add(entry.getValue());
        }
        return concerns;
    }

    /**
     * Retorna um Map imutável. É feito isso para garantir que nenhum modificação seja
     * feita diretamente na lista
     * <p>
     * Set<Package>
     *
     * @return Set<Package>
     */
    public Set<Package> getAllPackages() {
        return Collections.unmodifiableSet(this.packages);
    }

    /**
     * Retorna um Map mutável. É feito isso para permitir que modificação seja
     * feita diretamente na lista
     * <p>
     * Set<Package>
     *
     * @return Set<Package>
     */
    public Set<Package> getEditableListPackages(){
        return this.packages;
    }

    public List<Attribute> getAllAtributtes() {
        List<Attribute> attrs = new ArrayList<>();
        for (Class allClass : this.getAllClasses()) {
            attrs.addAll(allClass.getAllAttributes());
        }
        return attrs;
    }

    public List<Method> getAllMethods() {
        List<Method> attrs = new ArrayList<>();
        for (Class allClass : this.getAllClasses()) {
            attrs.addAll(allClass.getAllMethods());
        }
        for (Interface allClass : this.getAllInterfaces()) {
            attrs.addAll(allClass.getMethods());
        }
        return attrs;
    }

    public List<Package> getAllPackagesAllowedMofification() {
        return new ArrayList<>(this.packages);
    }

    /**
     * Retorna interfaces que não tem nenhum pacote.
     * <p>
     * Retorna um Set imutável. É feito isso para garantir que nenhum modificação seja
     * feita diretamente na lista.
     *
     * @return Set<Class>
     */
    public Set<Interface> getInterfaces() {
        return Collections.unmodifiableSet(this.interfaces);
    }

    public Set<Interface> getEditableListInterfaces() {
        return this.interfaces;
    }

    /**
     * Retorna todas as interfaces que existem na arquiteutra.
     * Este método faz um merge de todas as interfaces de todos os pacotes + as interfaces que não tem pacote
     *
     * @return
     */
    public Set<Interface> getAllInterfaces() {
        final Set<Interface> interfaces = new HashSet<Interface>();
        for (Package p : this.packages)
            interfaces.addAll(p.getAllInterfaces());

        interfaces.addAll(this.interfaces);
        return Collections.unmodifiableSet(interfaces);
    }

    /**
     * Retorna classes que não tem nenhum pacote.
     * <p>
     * Retorna um Set imutável. É feito isso para garantir que nenhum modificação seja
     * feita diretamente na lista
     *
     * @return Set<Class>
     */
    public Set<Class> getClasses() {
        return Collections.unmodifiableSet(this.classes);
    }

    public Set<Class> getEditableListClasses() {
        return this.classes;
    }

    /**
     * Retorna todas as classes que existem na arquiteutra.
     * Este método faz um merge de todas as classes de todos os pacotes + as classes que não tem pacote
     *
     * @return
     */
    public Set<Class> getAllClasses() {
        final Set<Class> klasses = new HashSet<Class>();
        for (Package p : this.packages)
            klasses.addAll(p.getAllClasses());

        klasses.addAll(this.classes);
        return Collections.unmodifiableSet(klasses);

    }

    /**
     * Busca elemento por nome.<br/>
     * <p>
     * No momento busca por class, interface ou package <br/>
     * <p>
     * <p>
     * TODO refatorar para buscar todo tipo de elemento
     *
     * @param name - Nome do elemento
     * @return
     * @parm type - tipo do elemento (class, interface ou package)
     */
    public Element findElementByName(String name, String type) {
        return ArchitectureFindElementControl.getInstance().findElement(this, name, type);
    }

    /**
     * Recupera uma classe por nome.
     *
     * @param className
     * @return {@link Class}
     */
    public List<Class> findClassByName(String className) {
        return ArchitectureFindElementControl.getInstance().findClassByName(this, className);
    }

    /**
     * Busca elemento por nome.
     *
     * @param elementName
     * @return - null se nao encontrar
     */
    public Element findElementByName(String elementName) {
        return ArchitectureFindElementControl.getInstance().findElementByName(this, elementName);
    }

    public Interface findInterfaceByName(String interfaceName) {
        return ArchitectureFindElementControl.getInstance().findInterfaceByName(this, interfaceName);
    }

    /**
     * Busca um pacote por nome.
     *
     * @param packageName
     * @return Package
     */
    public Package findPackageByName(String packageName) {
        return ArchitectureFindElementControl.getInstance().findPackageByName(this, packageName);
    }

    public void removeInterfaceByID(String id) {
        ArchitectureRemoveElementControl.getInstance().removeInterfaceByID(this, id);
    }


    public Package findPackageByID(String id) {
        return ArchitectureFindElementControl.getInstance().findPackageByID(this, id);
    }

    public Package createPackage(String packageName) {
        Package pkg = new Package(getRelationshipHolder(), packageName);
        this.packages.add(pkg);
        return pkg;
    }

    public Package createPackage(String packageName, String id) {
        Package pkg = new Package(getRelationshipHolder(), packageName, id);
        this.packages.add(pkg);
        return pkg;
    }

    public void removePackage(Package p) {
        ArchitectureRemoveElementControl.getInstance().removePackage(this, p);
    }

    public Interface createInterface(String interfaceName) {
        Interface interfacee = new Interface(getRelationshipHolder(), interfaceName);
        this.addExternalInterface(interfacee);
        return interfacee;
    }

    public Interface createInterface(String interfaceName, String id) {
        Interface interfacee = new Interface(getRelationshipHolder(), interfaceName, id);
        this.addExternalInterface(interfacee);
        return interfacee;
    }

    public Class createClass(String klassName, boolean isAbstract) {
        Class klass = new Class(getRelationshipHolder(), klassName, isAbstract);
        this.addExternalClass(klass);
        return klass;
    }

    public void removeInterface(Interface interfacee) {
        ArchitectureRemoveElementControl.getInstance().removeInterface(this, interfacee);
    }

    public void removeClass(Element klass) {
        ArchitectureRemoveElementControl.getInstance().removeClass(this, klass);
    }

    public List<VariationPoint> getAllVariationPoints() {
        if (isSMarty) {
            return lstVariationPoint;
        }
        return VariationPointFlyweight.getInstance().getVariationPoints();
    }

    public List<Variant> getAllVariants() {
        if (isSMarty) {
            return lstVariant;
        }
        return VariantFlyweight.getInstance().getVariants();
    }

    /**
     * return a list of variability. if input is smty, return a local list from architecture, else use flyweight
     * @return
     */
    public List<Variability> getAllVariabilities() {
        if (isSMarty) {
            return lstVariability;
        }
        return VariabilityFlyweight.getInstance().getVariabilities();
    }

    public Class findClassById(String idClass) throws ClassNotFound {
        return  ArchitectureFindElementControl.getInstance().findClassById(this, idClass);
    }

    public Interface findIntefaceById(String idClass) throws ClassNotFound {
        return ArchitectureFindElementControl.getInstance().findInterfaceById(this, idClass);
    }

    public void addExternalInterface(Interface interface_) {
        if (interfaces.add(interface_))
            LOGGER.info("Interface: " + interface_.getName() + " adicionada na arquiteutra");
        else
            LOGGER.info("TENTOU adicionar a interface : " + interface_.getName() + " na arquiteutra, porém não conseguiu");
    }

    /**
     * Retorna classe contendo método para manipular relacionamentos
     *
     * @return OperationsOverRelationships
     */
    public OperationsOverRelationships operationsOverRelationship() {
        return new OperationsOverRelationships(this);
    }

    public OperationsOverAssociation forAssociation() {
        return new OperationsOverAssociation(relationshipHolder);
    }

    public OperationsOverDependency forDependency() {
        return new OperationsOverDependency(relationshipHolder);
    }


    public void moveElementToPackage(Element klass, Package pkg) {
        if (pkg.getElements().contains(klass)) {
            return;
        }
        for (Class clazz : this.classes) {
            if (clazz.getId().equals(klass.getId()))
                return;
        }
        for (Interface clazz : this.interfaces) {
            if (clazz.getId().equals(klass.getId()))
                return;
        }
        String oldPackageName = UtilResources.extractPackageName(klass.getNamespace());
        if (this.packages.contains(pkg)) {
            if (oldPackageName.equals("model")) {
                addClassOrInterface(klass, pkg);
                this.removeOnlyElement(klass);
            } else {
                Package oldPackage = this.findPackageByName(oldPackageName);
                if (oldPackage != null) {
                    addClassOrInterface(klass, pkg);
                    oldPackage.removeOnlyElement(klass);
                }
            }
        }
        klass.setNamespace(ArchitectureHolder.getName() + "::" + pkg.getName());
    }

    /**
     * move a package to other package
     * @param packageID - id of package that will be moved
     * @param parentID - id of destiny package to be moved
     */
    public void movePackageToParent(String packageID, String parentID) {
        Package origin = findPackageByID(packageID);
        Package newParent = findPackageByID(parentID);
        for (Package pkg : this.packages) {
            if (packageID.equalsIgnoreCase(pkg.getId())) {
                this.packages.remove(origin);
                newParent.getNestedPackages().add(origin);
                return;
            }
            for (Package subP : pkg.getNestedPackages()) {
                if (packageID.equalsIgnoreCase(pkg.getId())) {
                    subP.getNestedPackages().remove(pkg);
                    newParent.getNestedPackages().add(origin);
                    return;
                }
                removeSubPackageByID(subP, packageID);
            }
        }
    }

    public void removeSubPackageByID(Package subPkg, String id) {
        ArchitectureRemoveElementControl.getInstance().removeSubPackageByID(subPkg, id);
    }

    private void addClassOrInterface(Element klass, Package pkg) {
        if (klass instanceof Class) {
            pkg.addExternalClass((Class) klass);
        } else if (klass instanceof Interface) {
            pkg.addExternalInterface((Interface) klass);
        }
    }

    public OperationsOverGeneralization forGeneralization() {
        return new OperationsOverGeneralization(this);
    }

    public OperationsOverAbstraction forAbstraction() {
        return new OperationsOverAbstraction(this);
    }

    public boolean removeRelationship(Relationship as) {
        return ArchitectureRemoveElementControl.getInstance().removeRelationship(this, as);
    }

    public OperationsOverUsage forUsage() {
        return new OperationsOverUsage(this);
    }

    /**
     * Create an exact copy of the <code>Architecture</code> object.
     *
     * @return An exact copy of the object.
     */
    public Variable deepCopy() {
        return this.deepClone();
    }

    //private static int count = 1;
    public Architecture deepClone() {
        if (cloner == null)
            cloner = new Cloner();
        Architecture newArchitecture = cloner.deepClone(this);
        return newArchitecture;
    }

    public boolean addImplementedInterface(Interface supplier, Class client) {
        if (!client.isTotalyFreezed()) {
            if (!haveRelationship(supplier, client)) {
                if (addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandonUUID()))) {
                    LOGGER.info("ImplementedInterface: " + supplier.getName() + " adicionada na classe: " + client.getName());
                    return true;
                } else {
                    LOGGER.info("Tentou adicionar a interface " + supplier.getName() + " como interface implementada pela classe: " + client.getName());
                    return false;
                }
            }
        }
        return false;
    }

    private boolean haveRelationship(Interface supplier, Element client) {
        for (Relationship r : relationshipHolder.getAllRelationships()) {
            if (r instanceof RealizationRelationship)
                if (((RealizationRelationship) r).getClient().equals(client) && ((RealizationRelationship) r).getSupplier().equals(supplier))
                    return true;
            if (r instanceof DependencyRelationship)
                if (((DependencyRelationship) r).getClient().equals(client) && ((DependencyRelationship) r).getSupplier().equals(supplier))
                    return true;
        }
        return false;
    }

    public boolean addImplementedInterface(Interface supplier, Package client) {
        if (!client.isTotalyFreezed()) {
            if (!haveRelationship(supplier, client)) {
                if (addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandonUUID()))) {
                    LOGGER.info("ImplementedInterface: " + supplier.getName() + " adicionada ao pacote: " + client.getName());
                    return true;
                } else {
                    LOGGER.info("Tentou adicionar a interface " + supplier.getName() + " como interface implementada no pacote: " + client.getName());
                    return false;
                }
            }
        }
        return false;
    }

    public void removeImplementedInterface(Interface inter, Package pacote) {
        ArchitectureRemoveElementControl.getInstance().removeImplementedInterface(this, inter, pacote);
    }

    public void removeImplementedInterface(Class foo, Interface inter) {
        ArchitectureRemoveElementControl.getInstance().removeImplementedInterface(this, foo,  inter);
    }

    public void addRequiredInterface(Interface supplier, Class client) {
        if (!haveRelationship(supplier, client)) {
            if (addRelationship(new DependencyRelationship(supplier, client, "", UtilResources.getRandonUUID())))
                LOGGER.info("RequiredInterface: " + supplier.getName() + " adicionada a: " + client.getName());
            else
                LOGGER.info("TENTOU adicionar RequiredInterface: " + supplier.getName() + " a : " + client.getName() + " porém não consegiu");
        }
    }

    public void addRequiredInterface(Interface supplier, Package client) {
        if (!haveRelationship(supplier, client)) {
            if (addRelationship(new DependencyRelationship(supplier, client, "", UtilResources.getRandonUUID())))
                LOGGER.info("RequiredInterface: " + supplier.getName() + " adicionada a: " + client.getName());
            else
                LOGGER.info("TENTOU adicionar RequiredInterface: " + supplier.getName() + " a : " + client.getName() + " porém não consegiu");
        }
    }

    public void deleteClassRelationships(Class class_) {
        Collection<Relationship> relationships = new ArrayList<Relationship>(class_.getRelationships());
        if (relationships != null) {
            for (Relationship relationship : relationships) {
                this.removeRelationship(relationship);
            }
        }
    }

    public boolean addRelationship(Relationship relationship) {
        if (!relationshipHolder.haveRelationship(relationship)) {
            if (relationshipHolder.addRelationship(relationship)) {
                LOGGER.info("Relacionamento: " + relationship.getType() + " adicionado na br.ufpr.dinf.gres.arquitetura.(" + UtilResources.detailLogRelationship(relationship) + ")");
                return true;
            } else {
                LOGGER.info("TENTOU adicionar Relacionamento: " + relationship.getType() + " na br.ufpr.dinf.gres.arquitetura porém não consegiu");
                return false;
            }
        }
        return false;
    }

    public Package findPackageOfClass(Class targetClass) {
        return ArchitectureFindElementControl.getInstance().findPackageOfClass(this, targetClass);
    }

    public Package findPackageOfElement(String id) {
        return ArchitectureFindElementControl.getInstance().findPackageOfElement(this, id);
    }

    /**
     * save an architecture to output
     * if toSMarty is true, save to .smty format, else save to .uml
     * @param architecture - target architecture
     * @param pathToSave - name of the output file
     * @param i -
     */
    public void save(Architecture architecture, String pathToSave, String i) {
        if (this.toSMarty) {
            GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
            generate.generate(architecture, pathToSave + architecture.getName() + i);
        } else {
            GenerateArchitecture generate = new GenerateArchitecture();
            generate.generate(architecture, pathToSave + architecture.getName() + i);
        }
    }

    /**
     * save an architecture in .smty format without consider the format of input architecture
     * @param architecture - target architecture
     * @param pathToSave - name of output file
     */
    public void saveToSMarty(Architecture architecture, String pathToSave) {
        GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
        generate.generate(architecture, pathToSave);
    }

    // open a temporary PLA in execution. Stop execution of OPLA-Tool while PLA is open in SMarty Modeling
    public void openTempArchitecture() {
        SaveStringToFile.getInstance().createTempDir();
        saveToSMarty(this, "TEMP/TEMP");
        System.out.println("Saved");
        try {
            System.out.println(ApplicationFileConfigThreadScope.getDirectoryToExportModels() + System.getProperty("file.separator") + "TEMP" + System.getProperty("file.separator") + "TEMP.smty");
            Process proc = Runtime.getRuntime().exec("java -jar SMartyModeling.jar " + ApplicationFileConfigThreadScope.getDirectoryToExportModels() + System.getProperty("file.separator") + "TEMP" + System.getProperty("file.separator") + "TEMP.smty");
            //Process proc = Runtime.getRuntime().exec("java -jar SMartyModeling.jar");
            proc.waitFor();
        } catch (Exception ex) {
            System.out.println("Exception Open");
            System.out.println(ex.getStackTrace());

        }
    }

    /**
     * Procura um elemento por ID.<br>
     * Este método busca por elementos diretamente no primeiro nível da arquitetura (Ex: classes que não possuem pacotes)
     * , e também em pacotes.<br/><br/>
     *
     * @param xmiId
     * @return
     */
    public Element findElementById(String xmiId) {
        return ArchitectureFindElementControl.getInstance().findElementById(this, xmiId);
    }

    /**
     * Procura um Method por ID.<br>
     * Este método busca por elementos diretamente no primeiro nível da arquitetura (Ex: classes que não possuem pacotes)
     * , e também em pacotes.<br/><br/>
     *
     * @param xmiId
     * @return
     */
    public Element findMethodById(String xmiId) {
        return ArchitectureFindElementControl.getInstance().findMethodById(this, xmiId);
    }


    public Element findAttributeById(String xmiId) {
        return ArchitectureFindElementControl.getInstance().findAttributeById(this, xmiId);
    }

    /**
     * Adiciona um pacote na lista de pacotes
     *
     * @param {@link Package}
     */
    public void addPackage(br.ufpr.dinf.gres.architecture.representation.Package p) {
        if (this.packages.add(p))
            LOGGER.info("Pacote: " + p.getName() + " adicionado na br.ufpr.dinf.gres.arquitetura");
        else
            LOGGER.info("TENTOU adicionar o Pacote: " + p.getName() + " na br.ufpr.dinf.gres.arquitetura porém não consegiu");
    }

    /**
     * Adiciona uma classe na lista de classes.
     *
     * @param {@link Class}
     */
    public void addExternalClass(Class klass) {
        if (this.classes.add(klass))
            LOGGER.info("Classe: " + klass.getName() + " adicionado na br.ufpr.dinf.gres.arquitetura");
        else
            LOGGER.info("TENTOU adicionar a Classe: " + klass.getName() + " na br.ufpr.dinf.gres.arquitetura porém não consegiu");
    }

    public void removeRequiredInterface(Interface supplier, Package client) {
        ArchitectureRemoveElementControl.getInstance().removeRequiredInterface(this, supplier, client);
    }

    public void removeRequiredInterface(Interface supplier, Class client) {
        ArchitectureRemoveElementControl.getInstance().removeRequiredInterface(this, supplier, client);
    }

    public boolean removeOnlyElement(Element element) {
        return ArchitectureRemoveElementControl.getInstance().removeOnlyElement(this, element);
    }

    public void setCloner(Cloner cloner) {
        this.cloner = cloner;
    }

    public RelationshipsHolder getRelationshipHolder() {
        return relationshipHolder;
    }

    public boolean isAppliedPatterns() {
        return appliedPatterns;
    }

    public void setAppliedPatterns(boolean b) {
        // TODO Auto-generated method stub
        this.appliedPatterns = b;
    }

    public void addAllClasses(Set<Class> classes) {
        this.classes.clear();
        this.classes.addAll(classes);
    }

    public void addAllPackages(Set<Package> packages) {
        this.packages.clear();
        this.packages.addAll(packages);
    }

    public void addAllInterfaces(Set<Interface> interfaces) {
        this.interfaces.clear();
        this.interfaces.addAll(interfaces);
    }

    @Override
    public String toString() {
        return "Architecture{" +
                "packages=" + packages +
                ", classes=" + classes +
                ", interfaces=" + interfaces +
                ", name='" + name + '\'' +
                ", appliedPatterns=" + appliedPatterns +
                ", relationshipHolder=" + relationshipHolder +
                '}';
    }

    public static Double median(List<Integer> values) {
        List<Integer> sortedValues = values.stream().sorted().collect(Collectors.toList());
        return sortedValues.size() % 2 == 0 ? (sortedValues.get((sortedValues.size() / 2) - 1) + sortedValues.get((sortedValues.size() / 2))) / 2 : Double.valueOf(sortedValues.get((int) Math.floor(sortedValues.size() / 2)));
    }

    public static Double mean(List<Integer> values) {
        return (double) (values.stream().mapToInt(Integer::intValue).sum() / values.size());
    }

    public static Double sum(List<Integer> values) {
        return (double) (values.stream().mapToInt(Integer::intValue).sum());
    }

    public String getDetailedString() {
        return this.getDetailedString(true);
    }

    public String getDetailedString(boolean withAttrs) {
        List<Integer> qtdAtributosPorClasse = new ArrayList<>();
        List<Integer> qtdMetodosPorClasse = new ArrayList<>();
        int qtdClassesSemAttr = 0;

        for (Class aClass : getAllClasses()) {
            if (aClass.getAllAttributes().size() != 0)
                qtdAtributosPorClasse.add(aClass.getAllAttributes().size());
            else
                qtdClassesSemAttr++;
            if (aClass.getAllMethods().size() != 0)
                qtdMetodosPorClasse.add(aClass.getAllMethods().size());
        }
        List<Element> freezedElements = getFreezedElements();
        StringBuilder str = new StringBuilder();
        str.append("Packages: " + getAllPackages() +
                ", qtdPackages: " + getAllPackages().size() +
                ", qtdClasses: " + getAllClasses().size() +
                ", qtdInterfaces: " + getAllInterfaces().size() +
                ", qtdClassesSemAttr: " + qtdClassesSemAttr +
                ", qtdFreezedElements: " + freezedElements.size() +
                ", \nfreezedElements: " + freezedElements.stream().map(s -> s.getName() + ":" + s.getTypeElement()).collect(Collectors.toList()) +
                ", \nqtdAggregation: " + getRelationshipHolder().getAllAgragations().size() +
                ", \ngetAllCompositions: " + getRelationshipHolder().getAllCompositions().size() +
                ", \ngetAllDependencies: " + getRelationshipHolder().getAllDependencies().size() +
                ", \ngetAllAssociations: " + getRelationshipHolder().getAllAssociations().size() +
                ", \ngetAllGeneralizations: " + getRelationshipHolder().getAllGeneralizations().size() +
                ", \ngetAllRelationships: " + getRelationshipHolder().getAllRelationships().size() +
                "\n");
        if (withAttrs) {
            str.append("    Classes: \n");
            str.append(getAllClasses().stream().map(clazz -> {
                return "       " + clazz.getName() +
                        " \n           qtdAttributes: " + clazz.getAllAttributes().size() +
                        " \n           qtdAbstractsMethods: " + clazz.getAllAbstractMethods().size() +
                        " \n           qtdConcerns: " + clazz.getAllConcerns().size() +
                        " \n           qtdAssociations: " + clazz.getAllAssociationClass().size() +
                        " \n           qtdMethods: " + clazz.getAllMethods().size() +
                        " \n           attributes: " + clazz.getAllAttributes().toString() +
                        " \n           methods: " + clazz.getAllMethods().toString() + "\n";
            }).collect(Collectors.joining()));
        }
        return str.toString();
    }

    public List<Element> getFreezedElements() {
        return getElementsWithPackages().stream().filter(Element::isFreezeByDM).collect(Collectors.toList());
    }

    public String toStringFreezedElements() {
        return getFreezedElements().stream().map(e -> e.getName() + ":" + e.getTypeElement()).collect(Collectors.toList()).toString();
    }

    public void addElement(Element element) {
        if (element instanceof Class) {
            addExternalClass((Class) element);
        } else if (element instanceof Interface) {
            addExternalInterface((Interface) element);
        } else if (element instanceof Package) {
            addPackage((Package) element);
        }
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }
}