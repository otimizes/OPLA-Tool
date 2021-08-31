package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.exceptions.ModelIncompleteException;
import br.otimizes.oplatool.architecture.exceptions.ModelNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.SMartyProfileNotAppliedToModelException;
import br.otimizes.oplatool.architecture.exceptions.VariationPointElementTypeErrorException;
import br.otimizes.oplatool.architecture.flyweights.VariabilityFlyweight;
import br.otimizes.oplatool.architecture.flyweights.VariantFlyweight;
import br.otimizes.oplatool.architecture.flyweights.VariationPointFlyweight;
import br.otimizes.oplatool.architecture.helpers.ModelElementHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelperFactory;
import br.otimizes.oplatool.architecture.helpers.StereotypeHelper;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationClassRelationship;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import com.rits.cloning.Cloner;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder responsible to create the papyrus architecture
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ArchitectureBuilderPapyrus implements IArchitectureBuilder {

    private static final Logger LOGGER = Logger.getLogger(ArchitectureBuilderPapyrus.class);

    private Package model;
    private PackageBuilder packageBuilder;
    private ClassBuilder classBuilder;
    private InterfaceBuilder interfaceBuilder;
    private VariabilityBuilder variabilityBuilder;
    private AssociationRelationshipBuilder associationRelationshipBuilder;
    private AssociationClassRelationshipBuilder associationClassRelationshipBuilder;
    private GeneralizationRelationshipBuilder generalizationRelationshipBuilder;
    private DependencyRelationshipBuilder dependencyRelationshipBuilder;
    private RealizationRelationshipBuilder realizationRelationshipBuilder;
    private AbstractionRelationshipBuilder abstractionRelationshipBuilder;
    private UsageRelationshipBuilder usageRelationshipBuilder;
    private final ModelHelper modelHelper;

    /**
     *
     */
    public ArchitectureBuilderPapyrus() {
        // RelationshipHolder.clearLists();
        LOGGER.info("Clean Relationships");
        ConcernHolder.INSTANCE.clear();
        LOGGER.info("Model Helper");
        modelHelper = ModelHelperFactory.getModelHelper();
    }

    /**
     * Creates architecture First the model (.uml file) is loaded,
     * after that the {@link Architecture} object is instantiated. <br/>
     * Once this is done, it is called the "initialize" method, in this method the
     * Builders creation. <br/>
     * <p>
     * The packages and their classes are then loaded. It is also loaded at
     * classes that do not belong to packages. <br/>
     * After that, the variability is loaded. <br/>
     *<br/>
     * <p>
     * InterClassRelationships </br>
     * <p>
     * <li>loadGeneralizations</li>
     * <li>loadAssociations</li>
     * <li>loadInterClassDependencies</li>
     * <li>loadRealizations</li> <br/>
     * <p>
     * InterElementRelationships</br>
     * <li>loadInterElementDependencies</li>
     * <li>loadAbstractions</li>
     * <p>
     * <br>
     *<br/>
     *
     * @param xmiFilePath - file from br.optimizes.oplatool.arquitetura (.uml)
     * @return {@link Architecture}
     */
    public Architecture create(String xmiFilePath) {
        try {
            LOGGER.info("Criando Architecture");
            model = modelHelper.getModel(xmiFilePath);
            VariationPointFlyweight.getInstance().addModel(model);
            VariabilityFlyweight.getInstance().addModel(model);

            Architecture architecture = new Architecture(modelHelper.getName(xmiFilePath));

            LOGGER.info("Inicializando");
            initialize(architecture);

            initializeConcerns();
            loadElements(architecture);

            Cloner cloner = new Cloner();
            architecture.setCloner(cloner);
            LOGGER.info("Set name");
            ArchitectureHolder.setName(architecture.getName());
            return architecture;

        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void initializeConcerns() {
        if (ApplicationFileConfigThreadScope.hasConcernsProfile()) {
            LOGGER.info("Config Concerns");
            Package concerns = modelHelper.loadConcernProfile();
            EList<Stereotype> concernsAllowed = concerns.getOwnedStereotypes();
            for (Stereotype stereotype : concernsAllowed)
                ConcernHolder.INSTANCE.allowedConcerns().add(new Concern(stereotype.getName()));
        }
    }

    private void loadElements(Architecture architecture) throws ModelNotFoundException, ModelIncompleteException, SMartyProfileNotAppliedToModelException, VariationPointElementTypeErrorException {
        LOGGER.info("Loading Packages");
        for (br.otimizes.oplatool.architecture.representation.Package p : loadPackages())
            architecture.addPackage(p);
        LOGGER.info("Classes");
        for (Class klass : loadClasses())
            architecture.addExternalClass(klass);
        LOGGER.info("Load Interfaces");
        for (Interface inter : loadInterfaces())
            architecture.addExternalInterface(inter);
        LOGGER.info("get Variabilities");
        architecture.getAllVariabilities().addAll(loadVariability());
        LOGGER.info("Load Inter Classes Relationship");
        for (br.otimizes.oplatool.architecture.representation.relationship.Relationship r : loadInterClassRelationships())
            architecture.addRelationship(r);
        LOGGER.info("Load Associations");
        for (br.otimizes.oplatool.architecture.representation.relationship.Relationship as : loadAssociationClassAssociation())
            architecture.addRelationship(as);
    }

    /**
     * Load abstractions
     *
     * @return abstractions
     */
    private List<? extends br.otimizes.oplatool.architecture.representation.relationship.Relationship> loadAbstractions() {
        List<Abstraction> abstractions = modelHelper.getAllAbstractions(model);
        List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> relations = new ArrayList<>();
        List<Package> packages = modelHelper.getAllPackages(model);

        for (Package package1 : packages) {
            List<Abstraction> abs = modelHelper.getAllAbstractions(package1);
            for (Abstraction abstraction : abs)
                relations.add(abstractionRelationshipBuilder.create(abstraction));
        }

        for (Abstraction abstraction : abstractions)
            relations.add(abstractionRelationshipBuilder.create(abstraction));

        if (relations.isEmpty())
            return Collections.emptyList();
        return relations;
    }

    /**
     * Load internal class relationships
     *
     * @return relationships
     */
    private List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> loadInterClassRelationships() {
        List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> relationships = new ArrayList<>();
        relationships.addAll(loadGeneralizations());
        relationships.addAll(loadAssociations());
        relationships.addAll(loadDependencies());
        relationships.addAll(loadRealizations());
        relationships.addAll(loadUsageInterClass());
        relationships.addAll(loadAbstractions());

        if (relationships.isEmpty())
            return Collections.emptyList();
        return relationships;
    }

    private List<? extends br.otimizes.oplatool.architecture.representation.relationship.Relationship> loadUsageInterClass() {
        List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> usageClass = new ArrayList<>();
        List<Usage> usages = modelHelper.getAllUsage(model);

        List<Package> packages = modelHelper.getAllPackages(model);
        for (Package package1 : packages)
            for (Usage u : modelHelper.getAllUsage(package1))
                usageClass.add(usageRelationshipBuilder.create(u));

        for (Usage usage : usages)
            usageClass.add(usageRelationshipBuilder.create(usage));

        if (usageClass.isEmpty())
            return Collections.emptyList();
        return usageClass;
    }

    /**
     * Load associations
     *
     * @return associations
     */
    private List<AssociationClassRelationship> loadAssociationClassAssociation() {
        List<AssociationClassRelationship> associationClasses = new ArrayList<>();
        List<AssociationClass> associationsClass = modelHelper.getAllAssociationsClass(model);

        for (AssociationClass associationClass : associationsClass) {
            associationClasses.add(associationClassRelationshipBuilder.create(associationClass));
        }

        if (associationClasses.isEmpty())
            return Collections.emptyList();
        return associationClasses;
    }

    /**
     * Load realizations
     *
     * @return realizations
     */
    private List<? extends br.otimizes.oplatool.architecture.representation.relationship.Relationship> loadRealizations() {
        List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> relationships = new ArrayList<>();
        List<Realization> realizations = modelHelper.getAllRealizations(model);

        List<Dependency> dependencies = modelHelper.getAllDependencies(model);

        for (Dependency dependency : dependencies)
            if (dependency instanceof Realization)
                relationships.add(realizationRelationshipBuilder.create((Realization) dependency));

        for (Realization realization : realizations)
            relationships.add(realizationRelationshipBuilder.create(realization));

        if (relationships.isEmpty())
            return Collections.emptyList();
        return relationships;
    }

    /**
     * Load dependencies
     *
     * @return dependencies
     */
    private List<? extends br.otimizes.oplatool.architecture.representation.relationship.Relationship> loadDependencies() {
        List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> relationships = new ArrayList<>();
        List<Dependency> dependencies = modelHelper.getAllDependencies(model);

        for (Dependency dependency : dependencies)
            if (!(dependency instanceof Usage) && (!(dependency instanceof Realization)))
                relationships.add(dependencyRelationshipBuilder.create(dependency));

        if (relationships.isEmpty())
            return Collections.emptyList();
        return relationships;
    }

    /**
     * Load generalizations
     *
     * @return generations
     */
    private List<? extends br.otimizes.oplatool.architecture.representation.relationship.Relationship> loadGeneralizations() {
        List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> relationships = new ArrayList<>();
        List<EList<Generalization>> generalizations = modelHelper.getAllGeneralizations(model);

        for (EList<Generalization> eList : generalizations)
            for (Generalization generalization : eList)
                relationships.add(generalizationRelationshipBuilder.create(generalization));

        if (relationships.isEmpty())
            return Collections.emptyList();
        return relationships;
    }

    /**
     * Load associations
     *
     * @return relationships
     */
    private List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> loadAssociations() {
        List<br.otimizes.oplatool.architecture.representation.relationship.Relationship> relationships = new ArrayList<>();
        List<Association> associations = modelHelper.getAllAssociations(model);

        for (Association association : associations)
            relationships.add(associationRelationshipBuilder.create(association));

        if (!relationships.isEmpty())
            return relationships;
        return relationships;
    }

    /**
     * Load variabilities
     *
     * @return variabilities
     * @throws ModelNotFoundException                  not found model
     * @throws ModelIncompleteException                incomplete model
     * @throws SMartyProfileNotAppliedToModelException not applied profile
     * @throws VariationPointElementTypeErrorException element variation point type error
     */
    private List<Variability> loadVariability() throws ModelNotFoundException, ModelIncompleteException,
            SMartyProfileNotAppliedToModelException, VariationPointElementTypeErrorException {
        List<org.eclipse.uml2.uml.Class> allClasses = modelHelper.getAllClasses(model);


        for (Classifier classifier : allClasses) {
            if (StereotypeHelper.isVariability(classifier))
                variabilityBuilder.create(classifier);
        }

        VariabilityFlyweight.getInstance().createVariants();
        List<Variability> variabilities = new ArrayList<>(VariabilityFlyweight.getInstance().getVariabilities());

        if (!variabilities.isEmpty())
            return variabilities;
        return Collections.emptyList();
    }

    /**
     * Load classes
     *
     * @return classes
     */
    private List<Class> loadClasses() {
        List<Class> listOfClasses = new ArrayList<>();
        List<org.eclipse.uml2.uml.Class> classes = modelHelper.getClasses(model);

        for (NamedElement element : classes)
            if (!ModelElementHelper.isInterface(element))
                listOfClasses.add(classBuilder.create(element));

        return listOfClasses;
    }

    /**
     * Load interfaces
     *
     * @return interfaces
     */
    private List<Interface> loadInterfaces() {
        List<Interface> listOfInterfaces = new ArrayList<>();
        List<org.eclipse.uml2.uml.Class> classes = modelHelper.getClasses(model);

        for (org.eclipse.uml2.uml.Class class1 : classes)
            if (ModelElementHelper.isInterface(class1))
                listOfInterfaces.add(interfaceBuilder.create(class1));

        return listOfInterfaces;
    }

    /**
     * Returns all packages
     *
     * @return {@link Package}
     */
    private List<br.otimizes.oplatool.architecture.representation.Package> loadPackages() {
        List<br.otimizes.oplatool.architecture.representation.Package> packages = new ArrayList<>();
        List<Package> allPackages = modelHelper.getAllPackages(model);
        for (NamedElement pkg : allPackages)
            packages.add(packageBuilder.create(pkg));
        return packages;
    }

    /**
     * Initializes the elements of architecture Instantiating builder classes with their dependencies.
     *
     * @param architecture architecture
     * @throws ModelIncompleteException incomplete model
     * @throws ModelNotFoundException   not found model
     */
    private void initialize(Architecture architecture) throws ModelNotFoundException, ModelIncompleteException {
        classBuilder = new ClassBuilder(architecture);
        interfaceBuilder = new InterfaceBuilder(architecture);
        packageBuilder = new PackageBuilder(architecture, classBuilder, interfaceBuilder);
        variabilityBuilder = new VariabilityBuilder(architecture);

        associationRelationshipBuilder = new AssociationRelationshipBuilder(architecture);
        generalizationRelationshipBuilder = new GeneralizationRelationshipBuilder(architecture);
        dependencyRelationshipBuilder = new DependencyRelationshipBuilder(architecture);
        realizationRelationshipBuilder = new RealizationRelationshipBuilder(architecture);
        abstractionRelationshipBuilder = new AbstractionRelationshipBuilder(architecture);
        associationClassRelationshipBuilder = new AssociationClassRelationshipBuilder(architecture);
        usageRelationshipBuilder = new UsageRelationshipBuilder(architecture);

        VariabilityFlyweight.getInstance().resetVariabilities();
        VariationPointFlyweight.getInstance().resetVariationPoints();
        VariantFlyweight.getInstance().resetVariants();
    }

    public Package getModel() {
        return this.model;
    }
}