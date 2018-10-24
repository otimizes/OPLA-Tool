package arquitetura.main;

import arquitetura.exceptions.*;
import arquitetura.helpers.Strings;
import arquitetura.helpers.UtilResources;
import arquitetura.io.ReaderConfig;
import arquitetura.representation.*;
import arquitetura.representation.Attribute;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.*;
import arquitetura.touml.*;
import arquitetura.touml.Method;
import br.ufpr.dinf.gres.loglog.Level;
import br.ufpr.dinf.gres.loglog.LogLog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenerateArchitecture extends ArchitectureBase {

    static Logger LOGGER = LogManager.getLogger(GenerateArchitecture.class.getName());

    private LogLog logger; //para poder capturar logs na GUI.
    private List<String> packageCreated = new ArrayList<String>();

    private static void generateAggregation(Operations op, AssociationRelationship r) throws NotSuppportedOperation {
        try {
            AssociationEnd p1 = r.getParticipants().get(0);
            AssociationEnd p2 = r.getParticipants().get(1);

            if (p1.isAggregation()) {
                op.forAggregation().createRelation().withName(r.getName()).between(p1).and(p2).build();
            } else if (p2.isAggregation()) {
                op.forAggregation().createRelation().withName(r.getName()).between(p2).and(p1).build();
            }
        } catch (Exception e) {
            LOGGER.info("Associacao agregacao nao criada");
        }
    }

    private static void generateSimpleAssociation(Operations op, AssociationRelationship r) {

        try {
            AssociationEnd p1 = r.getParticipants().get(0);
            AssociationEnd p2 = r.getParticipants().get(1);

            if (p1.getAggregation().equalsIgnoreCase("none") && (p2.getAggregation().equalsIgnoreCase("none"))) {
                op.forAssociation().createAssociation().withName(r.getName()).betweenClass(p1).andClass(p2).build();
            }
        } catch (Exception e) {
            LOGGER.info("Relacionamento associacao não criado");
        }
    }

    private static void generateComposition(Operations op, AssociationRelationship r) throws CustonTypeNotFound,
            NodeNotFound, InvalidMultiplictyForAssociationException {
        try {
            AssociationEnd p1 = r.getParticipants().get(0);
            AssociationEnd p2 = r.getParticipants().get(1);

            if (p1.isComposite()) {
                op.forComposition().createComposition().withName(r.getName()).between(p1).and(p2).build();
            } else if (p2.isComposite()) {
                op.forComposition().createComposition().withName(r.getName()).between(p2).and(p1).build();
            }
        } catch (Exception e) {
            LOGGER.info("Associacao Composicao não criada");
        }
    }

    private static Set<Method> createMethods(Element klass) {
        Set<arquitetura.touml.Method> methods = new HashSet<arquitetura.touml.Method>();
        Set<arquitetura.representation.Method> methodsClass = new HashSet<arquitetura.representation.Method>();

        if (klass instanceof Class) {
            methodsClass = ((Class) klass).getAllMethods();
        } else {
            methodsClass = ((Interface) klass).getOperations();
        }
        for (arquitetura.representation.Method method : methodsClass) {

            List<ParameterMethod> paramsMethod = method.getParameters();
            List<Argument> currentMethodParams = new ArrayList<Argument>();

            for (ParameterMethod param : paramsMethod) {
                currentMethodParams.add(Argument.create(param.getName(), Types.getByName(param.getType()),
                        param.getDirection()));
            }

            if (method.isAbstract()) {
                arquitetura.touml.Method m = arquitetura.touml.Method.create().withId(method.getId())
                        .withName(method.getName()).abstractMethod().withArguments(currentMethodParams)
                        .withConcerns(method.getOwnConcerns()).withReturn(Types.getByName(method.getReturnType()))
                        .build();
                methods.add(m);
            } else {
                arquitetura.touml.Method m = arquitetura.touml.Method.create().withId(method.getId())
                        .withName(method.getName()).withArguments(currentMethodParams)
                        .withConcerns(method.getOwnConcerns()).withReturn(Types.getByName(method.getReturnType()))
                        .build();
                methods.add(m);
            }
        }

        return methods;
    }

    private static List<arquitetura.touml.Attribute> createAttributes(Operations op, Class klass) {
        List<arquitetura.touml.Attribute> attributes = new ArrayList<arquitetura.touml.Attribute>();

        for (Attribute attribute : klass.getAllAttributes()) {
            arquitetura.touml.Attribute attr = arquitetura.touml.Attribute.create().withName(attribute.getName())
                    .grafics(attribute.isGeneratVisualAttribute()).withConcerns(attribute.getOwnConcerns())
                    .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
                    .withType(Types.getByName(attribute.getType()));

            attributes.add(attr);
        }

        return attributes;
    }

    public void generate(Architecture a, String output) {

        getLogLog(); //para poder capturar logs na GUI.

        UtilResources.clearConsole();

        DocumentManager doc = null;
        try {
            doc = givenADocument(output);
        } catch (ModelNotFoundException e1) {
            LOGGER.warn("Cannot find model: " + e1.getMessage());
        } catch (ModelIncompleteException e1) {
            LOGGER.warn("Model Incomplete" + e1.getMessage());
        } catch (SMartyProfileNotAppliedToModelExcepetion e1) {
            LOGGER.warn("Smarty Profile note Applied: " + e1.getMessage());
        }

        Operations op = null;

        try {
            op = new Operations(doc, a);

            Set<Package> packages = a.getAllPackages();

            for (Class klass : a.getAllClasses()) {

                List<arquitetura.touml.Attribute> attributesForClass = createAttributes(op, klass);

                Set<Method> methodsForClass = createMethods(klass);

                // Variation Point
                VariationPoint variationPoint = klass.getVariationPoint();
                String variants = "";
                String variabilities = "";

                if (variationPoint != null) {
                    variants = Strings.spliterVariants(variationPoint.getVariants());
                    variabilities = Strings.spliterVariabilities(variationPoint.getVariabilities());
                }
                // Variation Point
                if (attributesForClass.isEmpty())
                    op.forClass().createClass(klass).withMethods(methodsForClass)
                            .isVariationPoint(variants, variabilities, BindingTime.DESIGN_TIME).build();
                else {
                    op.forClass().createClass(klass).withMethods(methodsForClass).withAttribute(attributesForClass)
                            .isVariationPoint(variants, variabilities, BindingTime.DESIGN_TIME).build();
                }

                // Adiciona Interesses nas classes
                op.forConcerns().withStereotypes(klass.getOwnConcerns(), klass.getId());

                op.forConcerns().withPatternsStereotype(klass);

                // Adiciona Interesses nos atributos
                for (arquitetura.touml.Attribute attr : attributesForClass) {
                    op.forConcerns().withStereotypes(attr.getConcerns(), attr.getId());
                }
                // Adiciona Interesses nos métodos
                for (Method m : methodsForClass) {
                    op.forConcerns().withStereotypes(m.getConcerns(), m.getId());
                }

                attributesForClass.clear();
                methodsForClass.clear();
                // Adiciona Interesses nas classes

                // Variant Type

                Variant v = null;

                Variant variant = klass.getVariant();
                if (variant != null) {
                    try {
                        Element elementRootVp = null;
                        elementRootVp = a.findElementByName(variant.getRootVP(), "class");
                        if (elementRootVp == null)
                            elementRootVp = a.findElementByName(variant.getRootVP(), "interface");
                        String rootVp = null;

                        if (elementRootVp != null)
                            rootVp = elementRootVp.getName();
                        else
                            rootVp = "";
                        v = Variant.createVariant().withName(variant.getVariantName()).andRootVp(rootVp)
                                .wihtVariabilities(variant.getVariabilities())
                                .withVariantType(variant.getVariantType()).build();

                        // Se tem variant adicionar na classe
                        if (v != null) {
                            op.forClass().addStereotype(klass.getId(), v);
                        }

                    } catch (Exception e) {
                        System.out.println("Error when try create Variant." + e.getMessage());
                        System.exit(0);
                    }
                }
                // Variant Type
            }

            for (Interface _interface : a.getAllInterfaces()) {
                // Variation Point
                VariationPoint variationPoint = _interface.getVariationPoint();
                String variants = "";
                String variabilities = "";

                if (variationPoint != null) {
                    variants = Strings.spliterVariants(variationPoint.getVariants());
                    variabilities = Strings.spliterVariabilities(variationPoint.getVariabilities());
                }

                Set<Method> methodsForClass = createMethods(_interface);
                op.forClass().createClass(_interface).withMethods(methodsForClass)
                        .isVariationPoint(variants, variabilities, BindingTime.DESIGN_TIME).asInterface().build();

                // Variant Type

                Variant v = null;

                Variant variant = _interface.getVariant();
                if (variant != null) {
                    try {
                        Element elementRootVp = null;
                        elementRootVp = a.findElementByName(variant.getRootVP(), "class");
                        if (elementRootVp == null)
                            elementRootVp = a.findElementByName(variant.getRootVP(), "interface");
                        String rootVp = null;

                        if (elementRootVp != null)
                            rootVp = elementRootVp.getName();
                        else
                            rootVp = "";
                        v = Variant.createVariant().withName(variant.getVariantName()).andRootVp(rootVp)
                                .wihtVariabilities(variant.getVariabilities())
                                .withVariantType(variant.getVariantType()).build();

                        // Se tem variant adicionar na classe
                        if (v != null) {
                            op.forClass().addStereotype(_interface.getId(), v);
                        }

                    } catch (Exception e) {
                        if (this.logger != null)
                            this.logger.putLog("Error when try create Variant." + e.getMessage(), Level.FATAL);
                        System.exit(0);
                    }
                }
                // Variant Type

            }

            for (Interface inter : a.getAllInterfaces()) {
                // Adiciona Interesses nos métodos da interface
                for (arquitetura.representation.Method operation : inter.getOperations()) {
                    op.forConcerns().withStereotypes(operation.getOwnConcerns(), operation.getId());
                }
                op.forConcerns().withStereotypes(inter.getOwnConcerns(), inter.getId());
                op.forConcerns().withPatternsStereotype(inter);
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
                try {
                    op.forGeneralization().createRelation().between(g.getChild().getId()).and(g.getParent().getId())
                            .build();
                } catch (Exception e) {
                    LOGGER.info("Generalizacao nao criada");
                }
            }

            for (DependencyRelationship d : a.getRelationshipHolder().getAllDependencies()) {
                try {
                    op.forDependency().createRelation().withName(d.getName()).withStereotypes(d.getStereotypes())
                            .between(d.getClient().getId()).and(d.getSupplier().getId()).build();
                } catch (Exception e) {
                    LOGGER.info("Dependencia nao criada");
                }
            }
            for (RealizationRelationship r : a.getRelationshipHolder().getAllRealizations()) {
                try {
                    op.forRealization().createRelation().withName(r.getName()).between(r.getClient().getId())
                            .and(r.getSupplier().getId()).build();
                } catch (Exception e) {
                    LOGGER.info("Realizacao nao criada");
                }
            }

            for (AbstractionRelationship r : a.getRelationshipHolder().getAllAbstractions()) {
                try {
                    op.forAbstraction().createRelation().withName(r.getName()).between(r.getClient().getId())
                            .and(r.getSupplier().getId()).build();
                } catch (Exception e) {
                    LOGGER.info("Abstracao nao criada");
                }
            }

            for (UsageRelationship u : a.getRelationshipHolder().getAllUsage()) {
                try {
                    op.forUsage().createRelation("").between(u.getClient().getId()).and(u.getSupplier().getId())
                            .build();
                } catch (Exception e) {
                    LOGGER.info("Usage nao criada");
                }
            }

            for (AssociationClassRelationship asr : a.getRelationshipHolder().getAllAssociationsClass()) {
                try {
                    op.forAssociationClass().createAssociationClass(asr).build();
                    op.forPackage().withId(asr.getPackageOwner()).add(asr.getId());
                } catch (Exception e) {
                    LOGGER.info("AssociationClass nao criada");
                }
            }

            // Variabilidades - Notes
            List<Variability> variabilities = a.getAllVariabilities();
            String idOwner = "";
            for (Variability variability : variabilities) {
                try {
                    VariationPoint variationPointForVariability = variability.getVariationPoint();
            /*
             * Um Variabilidade pode estar ligada a uma classe que não
		     * seja um ponto de variação, neste caso a chama do método
		     * acima vai retornar null. Quando isso acontecer é usado o
		     * método getOwnerClass() que retorna a classe que é dona da
		     * variabilidade.
		     */
                    if (variationPointForVariability == null) {
                        idOwner = a.findClassByName(variability.getOwnerClass()).get(0).getId();
                    } else {
                        idOwner = variationPointForVariability.getVariationPointElement().getId();
                    }

                    String idNote = op.forNote().createNote().build();
                    VariabilityStereotype var = new VariabilityStereotype(variability);
                    op.forNote().addVariability(idNote, var).build();
                    op.forClass().withId(idOwner).linkToNote(idNote);
                } catch (Exception e) {
                    LOGGER.info("Nao conseguiu criar variationPoint");
                }

            }
            variabilities.clear();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        LOGGER.info("\n\n\nDone. Architecture save into: " + ReaderConfig.getDirExportTarget() + doc.getNewModelName()
                + "\n\n\n\n");
        if (this.logger != null)
            this.logger.putLog("Done. Architecture save into: " + ReaderConfig.getDirExportTarget()
                    + doc.getNewModelName(), Level.INFO);

    }

    private void buildPackages(Operations op, Set<Package> packages) throws CustonTypeNotFound, NodeNotFound,
            InvalidMultiplictyForAssociationException {

        buildPackage(op, packages.iterator().next());

        for (Package p : packages) {
            op.forPackage().createPacakge(p).withClass(getOnlyInterfacesAndClasses(p)).build();
        }
    }

    private void buildPackage(Operations op, Package pack) throws CustonTypeNotFound, NodeNotFound,
            InvalidMultiplictyForAssociationException {
        List<String> nestedIds = new ArrayList<String>();
        for (Package p : pack.getNestedPackages()) {
            nestedIds.add(p.getId());
            if (!p.getNestedPackages().isEmpty())
                buildPackage(op, p);
            if (!packageCreated.contains(p.getId())) {
                op.forPackage().createPacakge(p).withClass(getOnlyInterfacesAndClasses(p)).build();
                packageCreated.add(p.getId());
            }
        }
        nestedIds.clear();
    }

    private List<String> getOnlyInterfacesAndClasses(Package package1) {
        List<String> elements = new ArrayList<String>();
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
