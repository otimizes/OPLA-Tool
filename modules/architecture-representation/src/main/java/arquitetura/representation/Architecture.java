package arquitetura.representation;

import arquitetura.exceptions.ClassNotFound;
import arquitetura.flyweights.VariabilityFlyweight;
import arquitetura.flyweights.VariantFlyweight;
import arquitetura.flyweights.VariationPointFlyweight;
import arquitetura.helpers.UtilResources;
import arquitetura.io.ReaderConfig;
import arquitetura.main.GenerateArchitecture;
import arquitetura.main.GenerateArchitectureSMarty;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.RealizationRelationship;
import arquitetura.representation.relationship.Relationship;
import com.rits.cloning.Cloner;
import jmetal4.core.Variable;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author edipofederle<edipofederle               @               gmail.com>
 */
public class Architecture extends Variable {

    private static Logger LOGGER = Logger.getLogger(Architecture.class);
    private static final long serialVersionUID = -7764906574709840088L;
    public static String ARCHITECTURE_TYPE = "arquitetura.representation.Architecture";
    private Cloner cloner;
    private Set<Package> packages = new HashSet<Package>(); // pacotes (pacote tem classes e interfaces)
    private Set<Class> classes = new HashSet<Class>();  /// classes fora de pacote
    private Set<Interface> interfaces = new HashSet<Interface>();  // interface fora de pacote
    private String name;
    private boolean appliedPatterns;

    private RelationshipsHolder relationshipHolder = new RelationshipsHolder();


    // MAMORU
    private ArrayList<Concern> lstConcerns = new ArrayList<>();
    private ArrayList<TypeSmarty> lstTypes= new ArrayList<>();
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
        for(Concern c : this.lstConcerns) {
            if(!c.getPrimitive()) {
                ConcernHolder.INSTANCE.getConcerns().put(c.getName(), c);
                ConcernHolder.INSTANCE.allowedConcerns().add(c);
            }
        }
    }

    public Concern findConcernByName(String name){
        for(Concern c : lstConcerns){
            if(c.getName().equalsIgnoreCase(name)){
                return c;
            }
        }return null;
    }

    public boolean hasDuplicateInterface(){
        ArrayList<String> interfacesID = new ArrayList<>();
        /*
        for(Interface inter : getAllInterfaces()){
            if(interfaces.contains(inter.getId())){
                System.out.println("Interface Duplicada: "+inter.getId()+" - "+inter.getName());
                return  true;
            }
            interfacesID.add((inter.getId()));
        }
        */
        for(Interface inter : getInterfaces()){
            if(interfacesID.contains(inter.getId())){
                System.out.println("Interface Duplicada: "+inter.getId()+" - "+inter.getName());
                return  true;
            }
            interfacesID.add((inter.getId()));
        }
        for(Package pkg : getAllPackages()){
            for(Interface inter : pkg.getAllInterfaces()){
                if(interfacesID.contains(inter.getId())){
                    System.out.println("Interface Duplicada: "+inter.getId()+" - "+inter.getName());
                    return  true;
                }
                interfacesID.add((inter.getId()));
            }
        }
        //System.out.println(interfacesID);
        return  false;
    }

    public ArrayList<Interface> getDuplicateInterface(){
        ArrayList<String> interfacesID = new ArrayList<>();
        ArrayList<Interface> interfacesDup = new ArrayList<>();

        for(Interface inter : getInterfaces()){
            if(interfacesID.contains(inter.getId())){
                System.out.println("Interface Duplicada: "+inter.getId()+" - "+inter.getName());
                interfacesDup.add(inter);
            }
            interfacesID.add((inter.getId()));
        }
        for(Package pkg : getAllPackages()){
            for(Interface inter : pkg.getAllInterfaces()){
                if(interfacesID.contains(inter.getId())){
                    System.out.println("Interface Duplicada: "+inter.getId()+" - "+inter.getName());
                    interfacesDup.add(inter);
                }
                interfacesID.add((inter.getId()));
            }
        }
        //System.out.println(interfacesID);
        return  interfacesDup;
    }

    public ArrayList<TypeSmarty> getLstTypes() {
        return lstTypes;
    }

    public void setLstTypes(ArrayList<TypeSmarty> lstTypes) {
        this.lstTypes = lstTypes;
    }

    public TypeSmarty findTypeSMartyByID(String id){
        for(TypeSmarty typeSmarty : lstTypes){
            if(typeSmarty.getId().equals(id))
                return typeSmarty;
        }
        return null;
    }

    public TypeSmarty findTypeSMartyByName(String name){
        for(TypeSmarty typeSmarty : lstTypes){
            //System.out.println(typeSmarty.getName());
            if(typeSmarty.getName().equals(name))
                return typeSmarty;
        }
        //System.out.println("tipo: "+name+" não encontrado. Retornando tipo Object");
        return  findObjectType();
        //return null;
        //return findObjectType();
    }

    public TypeSmarty findReturnTypeSMartyByName(String name){
        if(name == null){
            return findVoidType();
        }
        if(name.length() == 0){
            return  findVoidType();
        }
        for(TypeSmarty typeSmarty : lstTypes){
            //System.out.println(typeSmarty.getName());
            if(typeSmarty.getName().equals(name))
                return typeSmarty;
        }
        //System.out.println("tipo: "+name+" não encontrado. Retornando tipo Object");
        return  findObjectType();
    }

    public TypeSmarty findObjectType(){
        for(TypeSmarty typeSmarty : lstTypes){
            if(typeSmarty.getName().equals("Object"))
                return typeSmarty;
        }
        return null;
    }

    public TypeSmarty findVoidType(){
        for(TypeSmarty typeSmarty : lstTypes){
            if(typeSmarty.getName().equals("void"))
                return typeSmarty;
        }
        return null;
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

    public void clearArchitecture(){
        this.getClasses().clear();
        this.interfaces.clear();
        this.packages.clear();;
        this.relationshipHolder.getAllRelationships().clear();
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

    /**
     * Retorna um Map imutável. É feito isso para garantir que nenhum modificação seja
     * feita diretamente na lista
     *
     * @return Map<String               ,                               Concern>
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
        return findElement(name, type);
    }

    private Element findElement(String name, String type) {
        if (type.equalsIgnoreCase("class")) {
            for (Element element : getClasses()) {
                if (element.getName().equalsIgnoreCase(name))
                    return element;
            }
            for (Package p : getAllPackages()) {
                for (Element element : p.getElements()) {
                    if (element.getName().equalsIgnoreCase(name))
                        return element;
                }
            }
        }

        if (type.equalsIgnoreCase("interface")) {
            for (Element element : getInterfaces()) {
                if (element.getName().equalsIgnoreCase(name))
                    return element;
            }

            for (Package p : getAllPackages()) {
                for (Element element : p.getElements()) {
                    if (element.getName().equalsIgnoreCase(name))
                        return element;
                }
            }
        }

        if (type.equalsIgnoreCase("package")) {
            for (Element element : getAllPackages())
                if (element.getName().equalsIgnoreCase(name))
                    return element;
        }
        return null;
    }


    /**
     * Recupera uma classe por nome.
     *
     * @param className
     * @return {@link Class}
     */
    public List<Class> findClassByName(String className) {
        List<Class> classesFound = new ArrayList<Class>();
        for (Class klass : getClasses())
            if (className.trim().equalsIgnoreCase(klass.getName().trim()))
                classesFound.add(klass);

        for (Package p : this.packages)
            for (Class klass : p.getAllClasses())
                if (className.trim().equalsIgnoreCase(klass.getName().trim()))
                    classesFound.add(klass);

        if (classesFound.isEmpty())
            return null;
        return classesFound;
    }

    /**
     * Busca elemento por nome.
     *
     * @param elementName
     * @return - null se nao encontrar
     */
    public Element findElementByName(String elementName) {
        Element element = searchRecursivellyInPackage(this.packages, elementName);
        if (element == null) {
            for (Class klass : this.classes)
                if (klass.getName().equals(elementName))
                    return klass;
            for (Interface inter : this.interfaces)
                if (inter.getName().equals(elementName))
                    return inter;
        }
        if (element == null)
            LOGGER.info("No element called: " + elementName + " found");
        return element;
    }

    public Element findElementByName2(String elementName) {

        //System.out.println("Element:"+elementName);
        for(Class clazz_ : this.classes){
            if(clazz_.getName().equals(elementName))
                return  clazz_;
        }
        for(Interface clazz_ : this.interfaces){
            if(clazz_.getName().equals(elementName))
                return  clazz_;
        }

        for(Package pkg : this.packages){
            for(Class clazz_ : pkg.getAllClasses()){
                if(clazz_.getName().equals(elementName))
                    return  clazz_;
            }
            for(Interface clazz_ : pkg.getAllInterfaces()){
                if(clazz_.getName().equals(elementName))
                    return  clazz_;
            }
            Element e1 = findElementByName2InSubPackage(pkg, elementName);
            if(e1 != null)
                return e1;
        }
        return  null;
    }

    public Element findElementByName2InSubPackage(Package pkg1, String elementName) {



        for(Package pkg : pkg1.getNestedPackages()){
            for(Class clazz_ : pkg.getAllClasses()){
                if(clazz_.getName().equals(elementName))
                    return  clazz_;
            }
            for(Interface clazz_ : pkg.getAllInterfaces()){
                if(clazz_.getName().equals(elementName))
                    return  clazz_;
            }
            Element e1 = findElementByName2InSubPackage(pkg, elementName);
            if(e1 != null)
                return e1;
        }
        return  null;
    }


    private Element searchRecursivellyInPackage(Set<Package> packages, String elementName) {
        for (Package p : packages) {
            for (Element element : p.getElements()) {
                if (element.getName().equals(elementName))
                    return element;
                searchRecursivellyInPackage(p.getNestedPackages(), elementName);
            }

            if (p.getName().equals(elementName))
                return p;
            searchRecursivellyInPackage(p.getNestedPackages(), elementName);
        }

        return null;
    }

    public Interface findInterfaceByName(String interfaceName) {
        for (Interface interfacee : getInterfaces())
            if (interfaceName.equalsIgnoreCase(interfacee.getName()))
                return interfacee;
        for (Package p : this.packages)
            for (Interface interfacee : p.getAllInterfaces())
                if (interfaceName.equalsIgnoreCase(interfacee.getName()))
                    return interfacee;

        return null;
    }

    /**
     * Busca um pacote por nome.
     *
     * @param packageName
     * @return Package
     * @throws return null if package not exists.
     */
    public Package findPackageByName(String packageName) {
        for (Package pkg : getAllPackages())
            if (packageName.equalsIgnoreCase(pkg.getName()))
                return pkg;

        return null;
    }

    public Package findPackageByID(String id) {
        for (Package pkg : getAllPackages()) {
            if (id.equalsIgnoreCase(pkg.getId()))
                return pkg;

            for (Package subP : pkg.getNestedPackages()){
                Package subPkg = findSubPackageByID(subP, id);
                if(subPkg != null)
                    return  subPkg;
            }

        }
        return null;
    }

    public Package findSubPackageByID(Package subPkg, String id) {
        if(subPkg.getId().equals(id))
            return subPkg;
        for (Package pkg : subPkg.getNestedPackages()) {
            if (id.equalsIgnoreCase(pkg.getId()))
                return pkg;
            Package subP1 = findSubPackageByID(pkg, id);
            if(subP1 != null)
                return  subP1;
        }
        return null;
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
        /**
         * Remove qualquer relacionamento que os elementos do pacote
         * que esta sendo deletado possa ter.
         */
        for (Element element : p.getElements()) {
            relationshipHolder.removeRelatedRelationships(element);
        }
        //Remove os relacionamentos que o pacote possa pertencer
        relationshipHolder.removeRelatedRelationships(p);

        this.packages.remove(p);
        LOGGER.info("Pacote:" + p.getName() + "removido");
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
        interfacee.removeInterfaceFromRequiredOrImplemented();
        relationshipHolder.removeRelatedRelationships(interfacee);
        if (removeInterfaceFromArch(interfacee)) {
            LOGGER.info("Interface:" + interfacee.getName() + " removida da arquitetura");
        }
    }

    public void removeInterfaceByID(String id) {

        Set<Interface> newHash = new HashSet<>();

        for(Interface i: getInterfaces()){
            if(!i.getId().equals(id)){
                newHash.add(i);
            }
            else{
                relationshipHolder.removeRelatedRelationships(i);
            }
        }
        this.interfaces.clear();
        this.interfaces.addAll(newHash);
        for(Package pkg : this.packages){
            pkg.removeInterfaceByID(id);
        }

    }


    private boolean removeInterfaceFromArch(Interface interfacee) {
        if (this.interfaces.remove(interfacee))
            return true;
        for (Package p : this.packages) {
            if (p.removeInterface(interfacee))
                return true;
        }
        return false;
    }

    public void removeClass(Element klass) {
        relationshipHolder.removeRelatedRelationships(klass);
        if (this.classes.remove(klass))
            LOGGER.info("Classe " + klass.getName() + "(" + klass.getId() + ") removida da arquitetura");

        for (Package pkg : this.getAllPackages()) {
            if (pkg.getAllClasses().contains(klass)) {
                if (pkg.removeClass(klass))
                    LOGGER.info("Classe " + klass.getName() + "(" + klass.getId() + ") removida da arquitetura. Pacote(" + pkg.getName() + ")");
            }
        }
    }

    public List<VariationPoint> getAllVariationPoints() {
        if(isSMarty){
            return lstVariationPoint;
        }
        return VariationPointFlyweight.getInstance().getVariationPoints();
    }

    public List<Variant> getAllVariants() {
        if(isSMarty){
            return lstVariant;
        }
        return VariantFlyweight.getInstance().getVariants();
    }

    public List<Variability> getAllVariabilities() {
        //System.out.println("Is SMarty: "+isSMarty());
        if(isSMarty){
            //System.out.println(lstVariability);
            return lstVariability;
        }
        return VariabilityFlyweight.getInstance().getVariabilities();
    }

    public Class findClassById(String idClass) throws ClassNotFound {
        for (Class klass : getClasses())
            if (idClass.equalsIgnoreCase(klass.getId().trim()))
                return klass;

        for (Package p : getAllPackages())
            for (Class klass : p.getAllClasses())
                if (idClass.equalsIgnoreCase(klass.getId().trim()))
                    return klass;

        throw new ClassNotFound("Class " + idClass + " can not found.\n");
    }

    public Interface findIntefaceById(String idClass) throws ClassNotFound {
        for (Interface klass : getInterfaces())
            if (idClass.equalsIgnoreCase(klass.getId().trim()))
                return klass;

        throw new ClassNotFound("Class " + idClass + " can not found.\n");
    }

    public void addExternalInterface(Interface interface_) {
        boolean exist = false;
        for(Interface i : interfaces){
            if(i.getId().equals(interface_.getId()))
                exist = true;
        }
        if(exist)
            return;
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
        for(Class clazz : this.classes){
            if(clazz.getId().equals(klass.getId()))
                return;
        }
        for(Interface clazz : this.interfaces){
            if(clazz.getId().equals(klass.getId()))
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



    public void movePackageToParent(String packageID, String parentID) {


        Package origin = findPackageByID(packageID);
        Package originParent;
        Package newParent = findPackageByID(parentID);

        for(Package pkg : this.packages){
            if (packageID.equalsIgnoreCase(pkg.getId())) {
                this.packages.remove(origin);
                newParent.getNestedPackages().add(origin);
                return;
            }
            for (Package subP : pkg.getNestedPackages()){
                if (packageID.equalsIgnoreCase(pkg.getId())){
                    subP.getNestedPackages().remove(pkg);
                    newParent.getNestedPackages().add(origin);
                    return;
                }
                removeSubPackageByID(subP, packageID);
            }

        }

    }

    public void removeSubPackageByID(Package subPkg, String id) {

        for (Package pkg : subPkg.getNestedPackages()) {
            if (id.equalsIgnoreCase(pkg.getId())){
                subPkg.getNestedPackages().remove(pkg);
                return;
            }
            removeSubPackageByID(pkg, id);
        }
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
        LOGGER.info("removeRelationship()");
        if (as == null) return false;
        if (relationshipHolder.removeRelationship(as)) {
            LOGGER.info("Relacionamento : " + as.getType() + " removido da arquitetura");
            return true;
        } else {
            LOGGER.info("TENTOU remover Relacionamento : " + as.getType() + " da arquitetura porém não consegiu");
            return false;
        }
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
        //System.out.println("VA:"+getAllVariabilities());
        //System.out.println("VN:"+newArchitecture.getAllVariabilities());
        return newArchitecture;
    }


    public boolean addImplementedInterface(Interface supplier, Class client) {
        if (!haveRelationship(supplier, client)) {
            if (addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandonUUID()))) {
                LOGGER.info("ImplementedInterface: " + supplier.getName() + " adicionada na classe: " + client.getName());
                return true;
            } else {
                LOGGER.info("Tentou adicionar a interface " + supplier.getName() + " como interface implementada pela classe: " + client.getName());
                return false;
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
        if (!haveRelationship(supplier, client)) {
            if (addRelationship(new RealizationRelationship(client, supplier, "", UtilResources.getRandonUUID()))) {
                LOGGER.info("ImplementedInterface: " + supplier.getName() + " adicionada ao pacote: " + client.getName());
                return true;
            } else {
                LOGGER.info("Tentou adicionar a interface " + supplier.getName() + " como interface implementada no pacote: " + client.getName());
                return false;
            }
        }
        return false;
    }

    public void removeImplementedInterface(Interface inter, Package pacote) {
        pacote.removeImplementedInterface(inter);
        relationshipHolder.removeRelatedRelationships(inter);
    }

    public void removeImplementedInterface(Class foo, Interface inter) {
        foo.removeImplementedInterface(inter);
        relationshipHolder.removeRelatedRelationships(inter);
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
                LOGGER.info("Relacionamento: " + relationship.getType() + " adicionado na arquitetura.(" + UtilResources.detailLogRelationship(relationship) + ")");
                return true;
            } else {
                LOGGER.info("TENTOU adicionar Relacionamento: " + relationship.getType() + " na arquitetura porém não consegiu");
                return false;
            }
        }
        return false;
    }

    public Package findPackageOfClass(Class targetClass) {
        String packageName = UtilResources.extractPackageName(targetClass.getNamespace());
        return findPackageByName(packageName);
    }

    public Package findPackageOfElement(String id){
        for(Class c1 : this.classes){
            if(c1.getId().equals(id))
                return null;
        }
        for(Interface c1 : this.interfaces){
            if(c1.getId().equals(id))
                return null;
        }
        for(Package pkg : this.getAllPackages()){
            for(Class clazz : pkg.getAllClasses()){
                if(clazz.getId().equals(id))
                    return pkg;
            }
            for(Interface c1 : pkg.getAllInterfaces()){
                if(c1.getId().equals(id))
                    return null;
            }
        }
        return null;
    }




    public void save(Architecture architecture, String pathToSave, String i) {
        //GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
        //generate.generate(architecture, pathToSave + architecture.getName() + i);

        if(this.toSMarty){
            GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
            generate.generate(architecture, pathToSave + architecture.getName() + i);

        }else {
            GenerateArchitecture generate = new GenerateArchitecture();
            generate.generate(architecture, pathToSave + architecture.getName() + i);
        }


    }

    public void save2(Architecture architecture, String pathToSave) {
        GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
        generate.generate(architecture, pathToSave);
        /*
        if(this.toSMarty){
            GenerateArchitectureSMarty generate = new GenerateArchitectureSMarty();
            generate.generate(architecture, pathToSave);

        }else {
            GenerateArchitecture generate = new GenerateArchitecture();
            generate.generate(architecture, pathToSave);
        }
        */
    }

    private void createTempDir(){
        String directory = ReaderConfig.getDirExportTarget() + "/TEMP";
        File file = new File(directory);
        file.mkdir();
    }


    // open a temporary PLA in execution. Stop execution of OPLA-Tool while PLA is open in SMarty Modeling
    public void openTempArch(){
        createTempDir();
        save2(this, "TEMP/TEMP");
        System.out.println("Saved");
        try {
            System.out.println(ReaderConfig.getDirExportTarget() +System.getProperty("file.separator") + "TEMP" + System.getProperty("file.separator") + "TEMP.smty");
            Process proc = Runtime.getRuntime().exec("java -jar SMartyModeling.jar " + ReaderConfig.getDirExportTarget() + System.getProperty("file.separator") + "TEMP" + System.getProperty("file.separator") + "TEMP.smty");
            //Process proc = Runtime.getRuntime().exec("java -jar SMartyModeling.jar");
            proc.waitFor();
        }catch (Exception ex){
            System.out.println("Exception Open");
            System.out.println(ex.getStackTrace());

        }
    }

    public static void deleteTempFolder() {
        String directory = ReaderConfig.getDirExportTarget() + "/TEMP";
        File folder = new File(directory);
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                f.delete();
            }
        }
        folder.delete();
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
        for (Class element : this.classes) {
            if (element.getId().equals(xmiId))
                return element;
            for(Method m : element.getAllMethods()){
                if(m.getId().equals(xmiId))
                    return m;
            }
            for(Attribute m : element.getAllAttributes()){
                if(m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Interface element : this.interfaces) {
            if (element.getId().equals(xmiId))
                return element;
            for(Method m : element.getMethods()){
                if(m.getId().equals(xmiId))
                    return m;
            }
            for(Attribute m : element.getAllAttributes()){
                if(m.getId().equals(xmiId))
                    return m;
            }
        }

        for (Package p : getAllPackages()) {
            if (p.getId().equalsIgnoreCase(xmiId))
                return p;
            for (Class element : p.getAllClasses()) {
                if (element.getId().equals(xmiId))
                    return element;
                for(Method m : element.getAllMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
                for(Attribute m : element.getAllAttributes()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            for (Interface element : p.getAllInterfaces()) {
                if (element.getId().equals(xmiId))
                    return element;
                for(Method m : element.getMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
                for(Attribute m : element.getAllAttributes()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            Element e1 = findElementInSubPackageById(p, xmiId);
            if(e1 != null)
                return  e1;
        }

        return null;
    }

    public Element findElementInSubPackageById(Package pkg, String xmiId) {

        if(pkg.getId().equals(xmiId)){
            return pkg;
        }

        for (Package p : pkg.getNestedPackages()) {
            if (p.getId().equalsIgnoreCase(xmiId))
                return p;
            for (Class element : p.getAllClasses()) {
                if (element.getId().equals(xmiId))
                    return element;
                for(Method m : element.getAllMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
                for(Attribute m : element.getAllAttributes()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            for (Interface element : p.getAllInterfaces()) {
                if (element.getId().equals(xmiId))
                    return element;
                for(Method m : element.getMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
                for(Attribute m : element.getAllAttributes()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            Element e1 = findElementInSubPackageById(p, xmiId);
            if(e1 != null)
                return  e1;

        }

        return null;
    }

    /*
    public Element findElementById(String xmiId) {
        for (Class element : this.classes) {
            if (element.getId().equals(xmiId))
                return element;
        }
        for (Interface element : this.interfaces) {
            if (element.getId().equals(xmiId))
                return element;
        }
        for (Package p : getAllPackages()) {
            for (Element element : p.getElements()) {
                if (element.getId().equalsIgnoreCase(xmiId))
                    return element;
            }
        }

        for (Package p : getAllPackages()) {
            if (p.getId().equalsIgnoreCase(xmiId))
                return p;
        }

        return null;
    }

     */

    /**
     * Procura um Method por ID.<br>
     * Este método busca por elementos diretamente no primeiro nível da arquitetura (Ex: classes que não possuem pacotes)
     * , e também em pacotes.<br/><br/>
     *
     * @param xmiId
     * @return
     */
    public Element findMethodInSubPackageById(Package pkg, String xmiId) {

        for (Package p : pkg.getNestedPackages()) {
            for (Class element : p.getAllClasses()) {
                for(Method m : element.getAllMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            for (Interface element : p.getAllInterfaces()) {
                //System.out.println("Interface:"+element.getName());
                for(Method m : element.getMethods()){
                    //System.out.println("method:"+m.getId()+"-"+m.getName());
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            Element mx = findMethodInSubPackageById(p, xmiId);
            if(mx != null)
                return  mx;
        }
        return null;
    }


    public Element findMethodById(String xmiId) {
        for (Class element : this.classes) {
            for(Method m : element.getAllMethods()){
                if(m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Interface element : this.interfaces) {
            for(Method m : element.getMethods()){
                if(m.getId().equals(xmiId))
                    return m;
            }
        }
        for (Package p : getAllPackages()) {
            for (Class element : p.getAllClasses()) {
                for(Method m : element.getAllMethods()){
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            for (Interface element : p.getAllInterfaces()) {
                //System.out.println("Interface:"+element.getName());
                for(Method m : element.getMethods()){
                    //System.out.println("method:"+m.getId()+"-"+m.getName());
                    if(m.getId().equals(xmiId))
                        return m;
                }
            }
            Element mx = findMethodInSubPackageById(p, xmiId);
            if(mx != null)
                return  mx;
        }

        return null;
    }



    /**
     * Procura um Method por ID.<br>
     * Este método busca por elementos diretamente no primeiro nível da arquitetura (Ex: classes que não possuem pacotes)
     * , e também em pacotes.<br/><br/>
     *
     * @param xmiId
     * @return
     */
    public Element findAttributeById(String xmiId) {
        for (Class element : this.classes) {
            for(Attribute a : element.getAllAttributes()){
                if(a.getId().equals(xmiId))
                    return a;
            }
        }
        for (Interface element : this.interfaces) {
            for(Attribute a : element.getAllAttributes()){
                if(a.getId().equals(xmiId))
                    return a;
            }
        }
        for (Package p : getAllPackages()) {
            for (Class element : p.getAllClasses()) {
                for(Attribute a : element.getAllAttributes()){
                    if(a.getId().equals(xmiId))
                        return a;
                }
            }
            for (Interface element : p.getAllInterfaces()) {
                for(Attribute a : element.getAllAttributes()){
                    if(a.getId().equals(xmiId))
                        return a;
                }
            }
            Element e1 = findAttributeInSubpackageById(p,xmiId);
            if(e1 != null)
                return e1;
        }

        return null;
    }

    public Element findAttributeInSubpackageById(Package pkg, String xmiId) {

        for (Package p : pkg.getNestedPackages()) {
            for (Class element : p.getAllClasses()) {
                for(Attribute a : element.getAllAttributes()){
                    if(a.getId().equals(xmiId))
                        return a;
                }
            }
            for (Interface element : p.getAllInterfaces()) {
                for(Attribute a : element.getAllAttributes()){
                    if(a.getId().equals(xmiId))
                        return a;
                }
            }
            Element e1 = findAttributeInSubpackageById(p,xmiId);
            if(e1 != null)
                return e1;
        }

        return null;
    }

    /**
     * Adiciona um pacote na lista de pacotes
     *
     * @param {@link Package}
     */
    public void addPackage(Package p) {
        if (this.packages.add(p))
            LOGGER.info("Pacote: " + p.getName() + " adicionado na arquitetura");
        else
            LOGGER.info("TENTOU adicionar o Pacote: " + p.getName() + " na arquitetura porém não consegiu");
    }

    /**
     * Adiciona uma classe na lista de classes.
     *
     * @param {@link Class}
     */
    public void addExternalClass(Class klass) {
        if (this.classes.add(klass))
            LOGGER.info("Classe: " + klass.getName() + " adicionado na arquitetura");
        else
            LOGGER.info("TENTOU adicionar a Classe: " + klass.getName() + " na arquitetura porém não consegiu");
    }

    public void removeRequiredInterface(Interface supplier, Package client) {
        if (!client.removeRequiredInterface(supplier)) ;
        relationshipHolder.removeRelatedRelationships(supplier);
    }

    public void removeRequiredInterface(Interface supplier, Class client) {
        if (!client.removeRequiredInterface(supplier)) ;
        relationshipHolder.removeRelatedRelationships(supplier);
    }

    public boolean removeOnlyElement(Element element) {
        if (element instanceof Class) {
            if (this.classes.remove(element)) {
                LOGGER.info("Classe: " + element.getName() + " removida do pacote: " + this.getName());
                return true;
            }
        } else if (element instanceof Interface) {
            if (this.interfaces.remove(element)) {
                LOGGER.info("Interface: " + element.getName() + " removida do pacote: " + this.getName());
                return true;
            }
        }

        return false;
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


    public String toDetailedString(boolean withAttrs) {


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


        StringBuilder str = new StringBuilder();
        str.append("Packages: " + getAllPackages() +
                ", qtdPackages: " + getAllPackages().size() +
                ", qtdClasses: " + getAllClasses().size() +
                ", qtdInterfaces: " + getAllInterfaces().size() +
                ", qtdA: " + qtdClassesSemAttr +
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
}