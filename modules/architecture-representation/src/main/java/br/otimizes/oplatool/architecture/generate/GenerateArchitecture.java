package br.otimizes.oplatool.architecture.generate;

import br.otimizes.oplatool.architecture.exceptions.*;
import br.otimizes.oplatool.architecture.helpers.Strings;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.papyrus.GuiLogs;
import br.otimizes.oplatool.architecture.papyrus.touml.Method;
import br.otimizes.oplatool.architecture.papyrus.touml.*;
import br.otimizes.oplatool.architecture.representation.Attribute;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.LogLog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Papyrus architecture generator
 */
public class GenerateArchitecture extends ArchitectureBase {

    static Logger LOGGER = LogManager.getLogger(GenerateArchitecture.class.getName());

    private LogLog logger;
    private final List<String> packageCreated = new ArrayList<>();

    private static void generateAggregation(Operations op, AssociationRelationship associationRelationship) {
        try {
            AssociationEnd p1 = associationRelationship.getParticipants().get(0);
            AssociationEnd p2 = associationRelationship.getParticipants().get(1);

            if (p1.isAggregation()) {
                op.forAggregation().createRelation().withName(associationRelationship.getName()).between(p1).and(p2).build();
            } else if (p2.isAggregation()) {
                op.forAggregation().createRelation().withName(associationRelationship.getName()).between(p2).and(p1).build();
            }
        } catch (Exception e) {
            LOGGER.info("Aggregation not created");
        }
    }

    private static void generateSimpleAssociation(Operations op, AssociationRelationship associationRelationship) {
        try {
            AssociationEnd p1 = associationRelationship.getParticipants().get(0);
            AssociationEnd p2 = associationRelationship.getParticipants().get(1);

            if (p1.getAggregation().equalsIgnoreCase("none") && (p2.getAggregation().equalsIgnoreCase("none"))) {
                op.forAssociation().createAssociation().withName(associationRelationship.getName()).betweenClass(p1).andClass(p2).build();
            }
        } catch (Exception e) {
            LOGGER.info("Association not created");
        }
    }

    private static void generateComposition(Operations op, AssociationRelationship associationRelationship) {
        try {
            AssociationEnd p1 = associationRelationship.getParticipants().get(0);
            AssociationEnd p2 = associationRelationship.getParticipants().get(1);

            if (p1.isComposite()) {
                op.forComposition().createComposition().withName(associationRelationship.getName()).between(p1).and(p2).build();
            } else if (p2.isComposite()) {
                op.forComposition().createComposition().withName(associationRelationship.getName()).between(p2).and(p1).build();
            }
        } catch (Exception e) {
            LOGGER.info("Composition not created");
        }
    }

    private static Set<Method> createMethods(Element klass) {
        Set<br.otimizes.oplatool.architecture.papyrus.touml.Method> methods = new HashSet<>();
        Set<br.otimizes.oplatool.architecture.representation.Method> methodsClass;

        if (klass instanceof Class) {
            methodsClass = ((Class) klass).getAllMethods();
        } else {
            methodsClass = ((Interface) klass).getMethods();
        }
        for (br.otimizes.oplatool.architecture.representation.Method method : methodsClass) {
            List<ParameterMethod> paramsMethod = method.getParameters();
            List<Argument> currentMethodParams = new ArrayList<>();

            createAndAddNewMethodsWithParams(methods, method, paramsMethod, currentMethodParams);
        }

        return methods;
    }

    public static void createAndAddNewMethodsWithParams(Set<Method> methods, br.otimizes.oplatool.architecture.representation.Method method,
                                                        List<ParameterMethod> paramsMethod, List<Argument> currentMethodParams) {
        for (ParameterMethod param : paramsMethod) {
            currentMethodParams.add(Argument.create(param.getName(), Types.getByName(param.getType()),
                    param.getDirection()));
        }

        Method m;
        if (method.isAbstract()) {
            m = Method.create().withId(method.getId())
                    .withName(method.getName()).abstractMethod().withArguments(currentMethodParams)
                    .withConcerns(method.getOwnConcerns()).withReturn(Types.getByName(method.getReturnType()))
                    .build();
        } else {
            m = Method.create().withId(method.getId())
                    .withName(method.getName()).withArguments(currentMethodParams)
                    .withConcerns(method.getOwnConcerns()).withReturn(Types.getByName(method.getReturnType()))
                    .build();
        }
        methods.add(m);
    }

    private static List<br.otimizes.oplatool.architecture.papyrus.touml.Attribute> createAttributes(Class klass) {
        List<br.otimizes.oplatool.architecture.papyrus.touml.Attribute> attributes = new ArrayList<>();

        for (Attribute attribute : klass.getAllAttributes()) {
            br.otimizes.oplatool.architecture.papyrus.touml.Attribute attr = br.otimizes.oplatool.architecture.papyrus
                    .touml.Attribute.create().withName(attribute.getName())
                    .getGraphics(attribute.isGeneratVisualAttribute()).withConcerns(attribute.getOwnConcerns())
                    .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
                    .withType(Types.getByName(attribute.getType()));

            attributes.add(attr);
        }
        return attributes;
    }

    public void generate(Architecture a, String output) {
        ClassNotation.clearConfigurations();
        getLogLog();
        UtilResources.clearConsole();
        DocumentManager doc = null;
        try {
            doc = givenADocument(output);
        } catch (ModelNotFoundException e1) {
            LOGGER.warn("Cannot find model: " + e1.getMessage());
        } catch (ModelIncompleteException e1) {
            LOGGER.warn("Model Incomplete" + e1.getMessage());
        } catch (SMartyProfileNotAppliedToModelException e1) {
            LOGGER.warn("Smarty Profile note Applied: " + e1.getMessage());
        }
        try {
            Operations op = new Operations(doc, a);

            Set<Package> packages = a.getAllPackages();

            for (Class klass : a.getAllClasses().stream().sorted(Comparator.comparing(Element::getNamespace)).collect(Collectors.toList())) {
                setAttributesOnClass(a, op, klass);
            }

            for (Interface _interface : a.getAllInterfaces().stream().sorted(Comparator.comparing(Element::getNamespace)).collect(Collectors.toList())) {
                setVariationsPointOnInterface(a, op, _interface);
            }

            for (Interface inter : a.getAllInterfaces()) {
                generateConcerns(op, inter);
            }

            if (!packages.isEmpty())
                buildPackages(op, packages);

            for (AssociationRelationship r : a.getRelationshipHolder().getAllAssociationsRelationships())
                generateSimpleAssociation(op, r);

            for (AssociationRelationship r : a.getRelationshipHolder().getAllCompositions())
                generateComposition(op, r);

            for (AssociationRelationship r : a.getRelationshipHolder().getAllAgragations())
                generateAggregation(op, r);

            for (GeneralizationRelationship g : a.getRelationshipHolder().getAllGeneralizations()) {
                generateGeneralizations(op, g);
            }

            for (DependencyRelationship d : a.getRelationshipHolder().getAllDependencies()) {
                generateDependencies(op, d);
            }
            for (RealizationRelationship r : a.getRelationshipHolder().getAllRealizations()) {
                generateRealizations(op, r);
            }

            for (AbstractionRelationship r : a.getRelationshipHolder().getAllAbstractions()) {
                generateAbstractions(op, r);
            }

            for (UsageRelationship u : a.getRelationshipHolder().getAllUsage()) {
                generateUsages(op, u);
            }

            for (AssociationClassRelationship asr : a.getRelationshipHolder().getAllAssociationsClass()) {
                generateAssociationClasses(op, asr);
            }
            setVariabilities(a, op);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        String newModelName = (doc != null) ? doc.getNewModelName() : null;
        LOGGER.info("\n\n\nDone. Architecture save into: " + ApplicationFileConfigThreadScope.getDirectoryToExportModels()
                + FileConstants.FILE_SEPARATOR + newModelName + "\n\n\n\n");
        if (this.logger != null)
            this.logger.putLog("Done. Architecture save into: " + ApplicationFileConfigThreadScope.getDirectoryToExportModels()
                    + FileConstants.FILE_SEPARATOR + newModelName, Level.INFO);

    }

    private void generateAssociationClasses(Operations op, AssociationClassRelationship asr) {
        try {
            op.forAssociationClass().createAssociationClass(asr).build();
            op.forPackage().withId(asr.getPackageOwner()).add(asr.getId());
        } catch (Exception e) {
            LOGGER.info("AssociationClass not created");
        }
    }

    private void generateUsages(Operations op, UsageRelationship u) {
        try {
            op.forUsage().createRelation("").between(u.getClient().getId()).and(u.getSupplier().getId())
                    .build();
        } catch (Exception e) {
            LOGGER.info("Usage not created");
        }
    }

    private void generateAbstractions(Operations op, AbstractionRelationship r) {
        try {
            op.forAbstraction().createRelation().withName(r.getName()).between(r.getClient().getId())
                    .and(r.getSupplier().getId()).build();
        } catch (Exception e) {
            LOGGER.info("Abstraction not created");
        }
    }

    private void generateRealizations(Operations op, RealizationRelationship r) {
        try {
            op.forRealization().createRelation().withName(r.getName()).between(r.getClient().getId())
                    .and(r.getSupplier().getId()).build();
        } catch (Exception e) {
            LOGGER.info("Realization not created");
        }
    }

    private void generateDependencies(Operations op, DependencyRelationship d) {
        try {
            op.forDependency().createRelation().withName(d.getName()).withStereotypes(d.getStereotypes())
                    .between(d.getClient().getId()).and(d.getSupplier().getId()).build();
        } catch (Exception e) {
            LOGGER.info("Dependence not created");
        }
    }

    private void generateGeneralizations(Operations op, GeneralizationRelationship g) {
        try {
            op.forGeneralization().createRelation().between(g.getChild().getId()).and(g.getParent().getId())
                    .build();
        } catch (Exception e) {
            LOGGER.info("Generalization not created");
        }
    }

    private void generateConcerns(Operations op, Interface inter) {
        for (br.otimizes.oplatool.architecture.representation.Method operation : inter.getMethods()) {
            op.forConcerns().withStereotypes(operation.getOwnConcerns(), operation.getId());
        }
        op.forConcerns().withStereotypes(inter.getOwnConcerns(), inter.getId());
        op.forConcerns().withPatternsStereotype(inter);
    }

    private void setVariationsPointOnInterface(Architecture a, Operations op, Interface _interface) {
        VariationPoint variationPoint = _interface.getVariationPoint();
        String variants = "";
        String variabilities = "";

        if (variationPoint != null) {
            variants = Strings.splitVariants(variationPoint.getVariants());
            variabilities = Strings.splitVariabilities(variationPoint.getVariabilities());
        }

        Set<Method> methodsForClass = createMethods(_interface);
        op.forClass().createClass(_interface).withMethods(methodsForClass)
                .isVariationPoint(variants, variabilities, BindingTime.DESIGN_TIME).asInterface().build();

        Variant variant = _interface.getVariant();
        if (variant != null) {
            try {
                String rootVp = getRootVariationPoint(a, variant);
                Variant v = Variant.createVariant().withName(variant.getVariantName()).andRootVp(rootVp)
                        .wihtVariabilities(variant.getVariabilities())
                        .withVariantType(variant.getVariantType()).build();
                if (v != null) {
                    op.forClass().addStereotype(_interface.getId(), v);
                }

            } catch (Exception e) {
                if (this.logger != null)
                    this.logger.putLog("Error when try create Variant." + e.getMessage(), Level.FATAL);
                System.exit(0);
            }
        }
    }

    private void setAttributesOnClass(Architecture a, Operations op, Class klass) {
        List<br.otimizes.oplatool.architecture.papyrus.touml.Attribute> attributesForClass = createAttributes(klass);

        Set<Method> methodsForClass = createMethods(klass);
        VariationPoint variationPoint = klass.getVariationPoint();
        String variants = "";
        String variabilities = "";

        if (variationPoint != null) {
            variants = Strings.splitVariants(variationPoint.getVariants());
            variabilities = Strings.splitVariabilities(variationPoint.getVariabilities());
        }
        if (attributesForClass.isEmpty())
            op.forClass().createClass(klass).withMethods(methodsForClass)
                    .isVariationPoint(variants, variabilities, BindingTime.DESIGN_TIME).build();
        else {
            op.forClass().createClass(klass).withMethods(methodsForClass).withAttribute(attributesForClass)
                    .isVariationPoint(variants, variabilities, BindingTime.DESIGN_TIME).build();
        }
        op.forConcerns().withStereotypes(klass.getOwnConcerns(), klass.getId());
        op.forConcerns().withPatternsStereotype(klass);
        for (br.otimizes.oplatool.architecture.papyrus.touml.Attribute attr : attributesForClass) {
            op.forConcerns().withStereotypes(attr.getConcerns(), attr.getId());
        }
        for (Method m : methodsForClass) {
            op.forConcerns().withStereotypes(m.getConcerns(), m.getId());
        }

        attributesForClass.clear();
        methodsForClass.clear();
        setVariant(a, op, klass);
    }

    private void setVariant(Architecture a, Operations op, Class klass) {
        Variant variant = klass.getVariant();
        if (variant != null) {
            try {
                String rootVp = getRootVariationPoint(a, variant);
                Variant v = Variant.createVariant().withName(variant.getVariantName()).andRootVp(rootVp)
                        .wihtVariabilities(variant.getVariabilities())
                        .withVariantType(variant.getVariantType()).build();
                if (v != null) {
                    op.forClass().addStereotype(klass.getId(), v);
                }
            } catch (Exception e) {
                System.out.println("Error when try create Variant." + e.getMessage());
                System.exit(0);
            }
        }
    }

    private void setVariabilities(Architecture a, Operations op) {
        List<Variability> variabilities = a.getAllVariabilities();
        String idOwner = "";
        for (Variability variability : variabilities) {
            try {
                VariationPoint variationPointForVariability = variability.getVariationPoint();
                /*
                 * A Variability can be linked to a class that does not
                 * is a variation point, in this case the method call
                 * above will return null. When this happens, the
                 * getOwnerClass() method that returns the class that owns the
                 * variability.
                 */
                if (variationPointForVariability == null) {
                    idOwner = a.findClassByName(variability.getOwnerClass()).get(0).getId();
                } else {
                    idOwner = variationPointForVariability.getVariationPointElement().getId();
                }

                String idNote = op.forNote().createNote(variationPointForVariability).build();
                VariabilityStereotype var = new VariabilityStereotype(variability);
                op.forNote().addVariability(idNote, var).build();
                op.forClass().withId(idOwner).linkToNote(idNote);
            } catch (Exception e) {
                LOGGER.info("Could not create variationPoint");
            }

        }
        variabilities.clear();
    }

    private String getRootVariationPoint(Architecture a, Variant variant) {
        Element elementRootVp = a.findElementByName(variant.getRootVP(), "class");
        if (elementRootVp == null)
            elementRootVp = a.findElementByName(variant.getRootVP(), "interface");
        String rootVp = null;

        if (elementRootVp != null)
            rootVp = elementRootVp.getName();
        else
            rootVp = "";
        return rootVp;
    }

    private void buildPackages(Operations op, Set<Package> packages) throws CustonTypeNotFound, NodeNotFound,
            InvalidMultiplicityForAssociationException {

        buildPackage(op, packages.iterator().next());

        for (Package p : packages) {
            op.forPackage().createPacakge(p).withClass(getOnlyInterfacesAndClasses(p), p).build();
        }
    }

    private void buildPackage(Operations op, Package pack) throws CustonTypeNotFound, NodeNotFound,
            InvalidMultiplicityForAssociationException {
        List<String> nestedIds = new ArrayList<>();
        for (Package p : pack.getNestedPackages()) {
            nestedIds.add(p.getId());
            if (!p.getNestedPackages().isEmpty())
                buildPackage(op, p);
            if (!packageCreated.contains(p.getId())) {
                op.forPackage().createPacakge(p).withClass(getOnlyInterfacesAndClasses(p), p).build();
                packageCreated.add(p.getId());
            }
        }
        nestedIds.clear();
    }

    private List<String> getOnlyInterfacesAndClasses(Package package1) {
        List<String> elements = new ArrayList<>();
        for (Element element : package1.getElements()) {
            if (!(element instanceof Package)) {
                elements.add(element.getId());
            }
        }
        return elements;
    }

    private void getLogLog() {
        LogLog ll = GuiLogs.getLogger();
        if (ll != null)
            this.logger = ll;
    }
}
