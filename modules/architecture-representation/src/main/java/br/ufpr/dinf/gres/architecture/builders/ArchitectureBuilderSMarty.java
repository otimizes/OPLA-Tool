package br.ufpr.dinf.gres.architecture.builders;

import br.ufpr.dinf.gres.architecture.helpers.ModelHelper;
import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.relationship.*;
import com.rits.cloning.Cloner;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Package;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArchitectureBuilderSMarty {

    private static final Logger LOGGER = Logger.getLogger(ArchitectureBuilder.class);

    private Package model;

    private ModelHelper modelHelper;

    private Document document;
    private String expression;
    private XPath xPath;
    private NodeList nodeList;

    /**
     *
     */
    public ArchitectureBuilderSMarty() {
        System.out.println("FUUUUUUUUUUUUUUUUUUIIIIIIIIIII");
        // RelationshipHolder.clearLists();
        LOGGER.info("Clean Relationships");
        ConcernHolder.INSTANCE.clear();

        // Load configure file. Call this method only once
        LOGGER.info("Load Configs");
        ReaderConfig.load();

        LOGGER.info("Model Helper");
        //modelHelper = ModelHelperFactory.getModelHelper();
    }

    /**
     * Cria a arquitetura. Primeiramente é carregado o model (arquivo .uml),
     * após isso é instanciado o objeto {@link Architecture}. <br/>
     * Feito isso, é chamado método "initialize", neste método é crializada a
     * criação dos Builders. <br/>
     * <p>
     * Em seguida, é carregado os pacotes e suas classes. Também é carregada as
     * classes que não pertencem a pacotes. <br/>
     * Após isso são carregadas as variabilidade. <br/>
     * <br/>
     * <p>
     * InterClassRelationships </br>
     * <p>
     * <li>loadGeneralizations</li>
     * <li>loadAssociations</li>
     * <li>loadInterClassDependencies</li>
     * <li>loadRealizations</li> <br/>
     * <p>
     * InterElementRelationships </br>
     * <li>loadInterElementDependencies</li>
     * <li>loadAbstractions</li>
     * <p>
     * <br>
     * <br/>
     *
     * @param xmiFilePath - arquivo da arquitetura (.uml)
     * @return {@link Architecture}
     * @throws Exception
     */
    public Architecture create(String xmiFilePath) throws Exception {

        try {
            System.out.println("Criando Architecture SMarty");
            System.out.println(xmiFilePath);

            modelHelper = null;
            model = null;

            // udado para SMarty
            File file = new File(xmiFilePath);
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            document.getDocumentElement().normalize();

            expression = "/project";
            xPath = XPathFactory.newInstance().newXPath();
            nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

            int tam = xmiFilePath.split("/").length;
            String arquitectureName = xmiFilePath.split("/")[tam - 1].replace(".smty", "");

            Architecture architecture = new Architecture(arquitectureName);

            architecture.setSMarty(true);
            architecture.setToSMarty(true);

            Element element = (Element) this.nodeList.item(0);
            architecture.setProjectID(element.getAttribute("id"));
            architecture.setProjectName(element.getAttribute("name"));
            architecture.setProjectVersion(element.getAttribute("version"));

            ArrayList<Concern> lstConcerns = importStereotypesSMarty();
            ArrayList<TypeSmarty> lstTypes = importTypesSMarty();

            architecture.setLstConcerns(importStereotypesSMarty());
            architecture.setLstTypes(importTypesSMarty());

            importDiagrams(architecture);

            importLinkStereotypesSMarty(architecture.getLstConcerns(), architecture);

            //VariabilityFlyweight.getInstance().getVariabilities().clear();
            //VariantFlyweight.getInstance().getVariants().clear();
            //VariationPointFlyweight.getInstance().getVariationPoints().clear();

            for (Class clazz : architecture.getAllClasses()) {
                clazz.setRelationshipHolder(architecture.getRelationshipHolder());
            }
            for (Interface clazz : architecture.getAllInterfaces()) {
                clazz.setRelationshipHolder(architecture.getRelationshipHolder());
            }
            for (br.ufpr.dinf.gres.architecture.representation.Package clazz : architecture.getAllPackages()) {
                clazz.setRelationshipHolder(architecture.getRelationshipHolder());
            }

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

    private void importLinkStereotypesSMarty(ArrayList<Concern> lstConcern, Architecture architecture) throws XPathExpressionException {

        this.expression = "/project/links/link";
        this.nodeList = (NodeList) this.xPath.compile(this.expression).evaluate(this.document, XPathConstants.NODESET);
        for (int i = 0; i < this.nodeList.getLength(); i++) {

            Element element = (Element) this.nodeList.item(i);

            String id_element = element.getAttribute("element");
            String id_stereotype = element.getAttribute("stereotype");
            if (id_element.contains("ATTRIBUTE")) {
                br.ufpr.dinf.gres.architecture.representation.Element target = architecture.findAttributeById(id_element);
                for (Concern c1 : lstConcern) {
                    if (c1.getId().equals(id_stereotype) && !c1.getPrimitive()) {
                        target.addExternalConcern(c1);
                    }
                }
                continue;
            }
            if (id_element.contains("METHOD")) {
                br.ufpr.dinf.gres.architecture.representation.Element target = architecture.findMethodById(id_element);
                for (Concern c1 : lstConcern) {
                    if (c1.getId().equals(id_stereotype) && !c1.getPrimitive()) {
                        target.addExternalConcern(c1);
                    }
                }
                continue;
            }
            br.ufpr.dinf.gres.architecture.representation.Element target = architecture.findElementById(id_element);
            for (Concern c1 : lstConcern) {
                if (c1.getId().equals(id_stereotype) && !c1.getPrimitive()) {
                    try {
                        target.addExternalConcern(c1);
                    } catch (NullPointerException ex) {
                        System.out.println("Impossible to add the concern " + c1 + " on " + id_element);
                    }
                } else {
                    if (c1.getId().equals(id_stereotype)) {
                        // fix variant point ou variant
                        for (Variant variant : architecture.getAllVariants()) {
                            if (variant.getVariantElement().getId().equals(target.getId())) {
                                // is variant point
                                System.out.println("Is Variant" + target.getId());
                                //variant.setVariantType(c1.getName());
                                if (c1.getName().equals("alternative_OR")) {
                                    variant.setVariantType("alternative_OR");
                                }
                                if (c1.getName().equals("alternative_XOR")) {
                                    variant.setVariantType("alternative_XOR");
                                }
                                if (c1.getName().equals("mandatory")) {
                                    variant.setVariantType("mandatory");

                                }
                                if (c1.getName().equals("optional")) {
                                    variant.setVariantType("optional");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private ArrayList<Concern> importStereotypesSMarty() throws XPathExpressionException {
        ArrayList<Concern> listConcern = new ArrayList<>();
        this.expression = "/project/stereotypes/stereotype";
        this.nodeList = (NodeList) this.xPath.compile(this.expression).evaluate(this.document, XPathConstants.NODESET);
        for (int i = 0; i < this.nodeList.getLength(); i++) {
            //System.out.println(this.nodeList.item(i).toString());
            Element element = (Element) this.nodeList.item(i);

            Concern newConcern = new Concern();
            newConcern.setId(element.getAttribute("id"));
            newConcern.setName(element.getAttribute("name"));
            newConcern.setPrimitive(element.getAttribute("primitive").equals("true"));

            listConcern.add(newConcern);
        }
        return listConcern;
    }

    private ArrayList<TypeSmarty> importTypesSMarty() throws XPathExpressionException {
        ArrayList<TypeSmarty> listTypes = new ArrayList<>();
        this.expression = "/project/types/type";
        this.nodeList = (NodeList) this.xPath.compile(this.expression).evaluate(this.document, XPathConstants.NODESET);
        for (int i = 0; i < this.nodeList.getLength(); i++) {
            //System.out.println(this.nodeList.item(i).toString());
            Element element = (Element) this.nodeList.item(i);

            TypeSmarty newType = new TypeSmarty();
            newType.setId(element.getAttribute("id"));
            newType.setName(element.getAttribute("name"));
            newType.setValue(element.getAttribute("value"));
            newType.setPath(element.getAttribute("path"));
            newType.setPrimitive(element.getAttribute("primitive").equals("true"));
            newType.setStandard(element.getAttribute("standard").equals("true"));
            listTypes.add(newType);
        }
        return listTypes;
    }


    private void importDiagrams(Architecture architecture) throws XPathExpressionException {
        String[] types = {"Class"};
        for (String type : types) {
            this.expression = "/project/diagram";
            String filter = this.expression + "[@type='" + type + "']";
            this.nodeList = (NodeList) this.xPath.compile(filter).evaluate(this.document, XPathConstants.NODESET);

            if (nodeList.getLength() == 1) {
                Element element = (Element) nodeList.item(0);

                architecture.setDiagramID(element.getAttribute("id"));
                architecture.setDiagramName(element.getAttribute("name"));

                this.importPackage(element, architecture);
                this.importClass(element, architecture);
                this.importInterface(element, architecture);
                this.importRelationship(element, architecture);
                this.importReferencePackage(element, architecture);
                this.importVariability(element, architecture);
            }
        }
    }

    private void importPackage(Element node, Architecture architecture) {

        NodeList aClass = node.getElementsByTagName("package");

        for (int i = 0; i < aClass.getLength(); i++) {
            Element current = (Element) aClass.item(i);
            br.ufpr.dinf.gres.architecture.representation.Package package_ = new br.ufpr.dinf.gres.architecture.representation.Package(null, current.getAttribute("name"), null, "model", current.getAttribute("id"));
            package_.setPosX(current.getAttribute("x"));
            package_.setPosX(current.getAttribute("y"));
            package_.setWidth(current.getAttribute("width"));
            package_.setHeight(current.getAttribute("height"));
            package_.setMandatory(current.getAttribute("mandatory").equals("true"));
            architecture.addPackage(package_);

        }

    }

    private void importClass(Element node, Architecture architecture) {

        NodeList aClass = node.getElementsByTagName("class");

        for (int i = 0; i < aClass.getLength(); i++) {
            Element current = (Element) aClass.item(i);
            Class class_ = new Class(null, current.getAttribute("name"), null, current.getAttribute("abstract").equals("true"), "model", current.getAttribute("id"));

            class_.setFinal(current.getAttribute("final").equals("true"));

            class_.setPosX(current.getAttribute("x"));
            class_.setPosY(current.getAttribute("y"));
            class_.setWidth(current.getAttribute("width"));
            class_.setHeight(current.getAttribute("height"));
            class_.setMandatory(current.getAttribute("mandatory").equals("true"));

            class_.setFinal(current.getAttribute("final").equals("true"));
            this.importAttributesClass(current, class_, architecture);
            this.importMethods(current, class_, architecture);
            if (current.getAttribute("parent").equals("")) {
                architecture.addExternalClass(class_);
            } else {
                architecture.findPackageByID(current.getAttribute("parent")).addExternalClass(class_);
                class_.setNamespace("model::" + architecture.findPackageByID(current.getAttribute("parent")).getName());
            }
        }
    }


    private void importAttributesClass(Element node, Class class_, Architecture architecture) {
        NodeList attributes = node.getElementsByTagName("attribute");
        for (int i = 0; i < attributes.getLength(); i++) {
            Element current = (Element) attributes.item(i);
            String type = architecture.findTypeSMartyByID(current.getAttribute("type")).getName();
            Attribute attribute = new Attribute(current.getAttribute("name"), current.getAttribute("visibility"), null, type, class_.getNamespace() + "::" + class_.getName(), current.getAttribute("id"), false);

            attribute.setStatic(current.getAttribute("static").equals("true"));
            attribute.setFinal(current.getAttribute("final").equals("true"));

            class_.addExternalAttribute(attribute);
        }
    }

    /**
     * Method responsible for importing the Methods.
     *
     * @param node   W3C Element.
     * @param class_ Class.
     */

    private void importMethods(Element node, Class class_, Architecture architecture) {
        NodeList methods = node.getElementsByTagName("method");
        for (int i = 0; i < methods.getLength(); i++) {
            Element current = (Element) methods.item(i);

            ArrayList<ParameterMethod> parameterMethodArrayList = importParameterMethods(current, architecture);

            String type = architecture.findTypeSMartyByID(current.getAttribute("return")).getName();
            Method method = new Method(current.getAttribute("name"), false, null, type, current.getAttribute("abstract").equals("true"), parameterMethodArrayList, class_.getNamespace() + "::" + class_.getName(), current.getAttribute("id"));
            method.setStatic(current.getAttribute("static").equals("true"));
            method.setFinal(current.getAttribute("final").equals("true"));
            method.setConstructor(current.getAttribute("constructor").equals("true"));
            method.setVisibility(current.getAttribute("visibility"));

            class_.addExternalMethod(method);
        }
    }


    private ArrayList<ParameterMethod> importParameterMethods(Element node, Architecture architecture) {
        ArrayList<ParameterMethod> parameterMethodArrayList = new ArrayList<>();
        NodeList methods = node.getElementsByTagName("parameter");
        //System.out.println("Parameters");
        for (int i = 0; i < methods.getLength(); i++) {
            Element current = (Element) methods.item(i);

            String type = architecture.findTypeSMartyByID(current.getAttribute("type")).getName();
            ParameterMethod parameterMethod = new ParameterMethod(current.getAttribute("name"), type, "in");
            parameterMethodArrayList.add(parameterMethod);
        }
        return parameterMethodArrayList;
    }


    private void importInterface(Element node, Architecture architecture) {

        NodeList aClass = node.getElementsByTagName("interface");

        for (int i = 0; i < aClass.getLength(); i++) {
            Element current = (Element) aClass.item(i);

            Interface newInterface = new Interface(null, current.getAttribute("name"), null, "model", current.getAttribute("id"));

            newInterface.setPosX(current.getAttribute("x"));
            newInterface.setPosY(current.getAttribute("y"));
            newInterface.setWidth(current.getAttribute("width"));
            newInterface.setHeight(current.getAttribute("height"));
            newInterface.setMandatory(current.getAttribute("mandatory").equals("true"));

            newInterface.setFinal(current.getAttribute("final").equals("true"));

            Set<String> stereotypes = new HashSet<>();
            newInterface.setPatternOperations(new PatternsOperations(stereotypes));

            this.importMethodsInterface(current, newInterface, architecture);
            if (current.getAttribute("parent").equals("")) {
                architecture.addExternalInterface(newInterface);
            } else {
                architecture.findPackageByID(current.getAttribute("parent")).addExternalInterface(newInterface);
                newInterface.setNamespace("model::" + architecture.findPackageByID(current.getAttribute("parent")).getName());
            }
        }
    }

    /**
     * Method responsible for importing the Methods.
     *
     * @param node       W3C Element.
     * @param interface_ Interface.
     */
    private void importMethodsInterface(Element node, Interface interface_, Architecture architecture) {
        NodeList methods = node.getElementsByTagName("method");
        for (int i = 0; i < methods.getLength(); i++) {
            Element current = (Element) methods.item(i);

            ArrayList<ParameterMethod> parameterMethodArrayList = importParameterMethods(current, architecture);

            String type = architecture.findTypeSMartyByID(current.getAttribute("return")).getName();
            Method method = new Method(current.getAttribute("name"), false, null, type, current.getAttribute("abstract").equals("true"), parameterMethodArrayList, interface_.getNamespace() + "::" + interface_.getName(), current.getAttribute("id"));
            method.setStatic(current.getAttribute("static").equals("true"));
            method.setFinal(current.getAttribute("final").equals("true"));
            method.setConstructor(current.getAttribute("constructor").equals("true"));
            method.setVisibility(current.getAttribute("visibility"));

            interface_.addExternalMethod(method);
        }
    }


    private void importRelationship(Element node, Architecture architecture) {
        importAssociationRelationship(node, architecture);
        importDependencyRelationship(node, architecture);
        importGeneralizationRelationship(node, architecture);
        importRealizationRelationship(node, architecture);
        importRequiresRelationship(node, architecture);
        importAbstractionRelationship(node, architecture);
        importUsageRelationship(node, architecture);
    }

    private void importGeneralizationRelationship(Element node, Architecture architecture) {
        NodeList generalizations = node.getElementsByTagName("generalization");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);
            br.ufpr.dinf.gres.architecture.representation.Element child = architecture.findElementById(current.getAttribute("source"));
            br.ufpr.dinf.gres.architecture.representation.Element parent = architecture.findElementById(current.getAttribute("target"));
            if (parent != null && child != null) {
                //System.out.println("Create generalization");
                GeneralizationRelationship newRelation = new GeneralizationRelationship(parent, child, architecture.getRelationshipHolder(), current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
            }
        }
    }


    private void importRealizationRelationship(Element node, Architecture architecture) {
        NodeList generalizations = node.getElementsByTagName("realization");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);
            br.ufpr.dinf.gres.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("interface"));
            br.ufpr.dinf.gres.architecture.representation.Element client = architecture.findElementById(current.getAttribute("class"));
            if (client != null && supplier != null) {
                //System.out.println("Create realization");
                RealizationRelationship newRelation = new RealizationRelationship(client, supplier, "", current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
                if ((client instanceof Class) && (supplier instanceof Interface)) {
                    ((Class) client).addImplementedInterface((Interface) supplier);
                }
                if ((client instanceof br.ufpr.dinf.gres.architecture.representation.Package) && (supplier instanceof Interface)) {
                    ((br.ufpr.dinf.gres.architecture.representation.Package) client).addImplementedInterface((Interface) supplier);
                }
            } else {
                System.out.println("null");
            }
        }
    }


    private void importAssociationRelationship(Element node, Architecture architecture) {
        // <association id="ASSOCIATION#2" name="" category="composition" direction="true">
        //     <source entity="CLASS#28" sourceVisibility="private" sourceName="BrickPile" sourceMin="1" sourceMax="1" sourceX="1093" sourceY="1037"/>
        //     <target entity="CLASS#29" targetVisibility="private" targetName="Brick" targetMin="1" targetMax="1" targetX="1143" targetY="699"/>
        // </association>
        NodeList generalizations = node.getElementsByTagName("association");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);

            AssociationRelationship ar = new AssociationRelationship("");
            ar.setName(current.getAttribute("name"));

            String type = "none";
            if (current.getAttribute("category").equals("aggregation"))
                type = "shared";
            if (current.getAttribute("category").equals("composition"))
                type = "composite";

            AssociationEnd ae1 = null;
            ae1 = importAssociationSource(current, architecture, ae1, current.getAttribute("direction").equals("true"), type);

            AssociationEnd ae2 = null;
            ae2 = importAssociationTarget(current, architecture, ae2, current.getAttribute("direction").equals("true"), type);

            ar.SetAssociationEnd(ae1);
            ar.SetAssociationEnd(ae2);
            ar.setId(current.getAttribute("id"));
            architecture.getRelationshipHolder().addRelationship(ar);
        }

    }

    private AssociationEnd importAssociationSource(Element node, Architecture architecture, AssociationEnd ae1, boolean direction, String type) {
        NodeList generalizations = node.getElementsByTagName("source");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);

            Multiplicity mult1 = new Multiplicity(current.getAttribute("sourceMin"), current.getAttribute("sourceMax"));
            br.ufpr.dinf.gres.architecture.representation.Element part1 = architecture.findElementById(current.getAttribute("entity"));

            ae1 = new AssociationEnd(part1, direction, type, mult1, current.getAttribute("sourceName"));
            ae1.setPosX(current.getAttribute("sourceX"));
            ae1.setPosY(current.getAttribute("sourceY"));
            ae1.setVisibility(current.getAttribute("sourceVisibility"));

        }
        return ae1;

    }

    private AssociationEnd importAssociationTarget(Element node, Architecture architecture, AssociationEnd ae2, boolean direction, String type) {
        NodeList generalizations = node.getElementsByTagName("target");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);

            Multiplicity mult1 = new Multiplicity(current.getAttribute("targetMin"), current.getAttribute("targetMax"));
            br.ufpr.dinf.gres.architecture.representation.Element part1 = architecture.findElementById(current.getAttribute("entity"));

            ae2 = new AssociationEnd(part1, direction, type, mult1, current.getAttribute("targetName"));
            ae2.setPosX(current.getAttribute("targetX"));
            ae2.setPosY(current.getAttribute("targeteY"));
            ae2.setVisibility(current.getAttribute("targetVisibility"));

        }
        return ae2;

    }

    private void importAssociationClassRelationship(Element node, Architecture architecture) {
        // não implementado no SMarty // arrumar código depois4
        /*
        NodeList generalizations = node.getElementsByTagName("association");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element      current   = (Element) generalizations.item(i);
            arquitetura.representation.Element part1 = architecture.findElementById(current.getAttribute("source"));
            arquitetura.representation.Element part2 = architecture.findElementById(current.getAttribute("target"));
            if(part1 != null && part2 != null) {
                AssociationRelationship ar = new AssociationRelationship("");
                Multiplicity mult1 = new Multiplicity(current.getAttribute("sourceMin"), current.getAttribute("sourceMax"));
                String type = "none";
                if(current.getAttribute("category").equals("agregation"))
                    type = "shared";
                if(current.getAttribute("category").equals("composition"))
                    type = "composite";
                AssociationEnd ae1 = new AssociationEnd(part1, current.getAttribute("direction").equals("true"), type, mult1,current.getAttribute("sourceName"));
                ae1.setPosX(current.getAttribute("sourceX"));
                ae1.setPosY(current.getAttribute("sourceY"));


                Multiplicity mult2 = new Multiplicity(current.getAttribute("targetMin"), current.getAttribute("targetMax"));

                AssociationEnd ae2 = new AssociationEnd(part2, current.getAttribute("direction").equals("true"), type, mult2,current.getAttribute("targetName"));
                ae2.setPosX(current.getAttribute("targetX"));
                ae2.setPosY(current.getAttribute("targetY"));

                ar.SetAssociationEnd(ae1);
                ar.SetAssociationEnd(ae2);
                architecture.getRelationshipHolder().addRelationship(ar);
                System.out.println("Create Association");

            }
        }
         */

    }

    private void importDependencyRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("dependency");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.ufpr.dinf.gres.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.ufpr.dinf.gres.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));
            if (supplier != null && client != null) {
                //System.out.println("Create dependency");
                DependencyRelationship newRelation = new DependencyRelationship(supplier, client, "", current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
                if ((client instanceof Class) && (supplier instanceof Interface))
                    ((Class) client).addRequiredInterface((Interface) supplier);
                if ((client instanceof Package) && (supplier instanceof Interface))
                    ((br.ufpr.dinf.gres.architecture.representation.Package) client).addRequiredInterface((Interface) supplier);
            } else {
                System.out.println("null");
            }
        }

    }

    private void importRequiresRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("requires");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.ufpr.dinf.gres.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.ufpr.dinf.gres.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));
            if (supplier != null && client != null) {
                RequiresRelationship newRelation = new RequiresRelationship(supplier, client, "", current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
                if ((client instanceof Class) && (supplier instanceof Interface))
                    ((Class) client).addRequiredInterface((Interface) supplier);
                if ((client instanceof Package) && (supplier instanceof Interface))
                    ((br.ufpr.dinf.gres.architecture.representation.Package) client).addRequiredInterface((Interface) supplier);
            } else {
                System.out.println("null");
            }
        }

    }


    private void importAbstractionRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("abstraction");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.ufpr.dinf.gres.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.ufpr.dinf.gres.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));
            if (supplier != null && client != null) {
                //System.out.println("Create dependency");
                AbstractionRelationship newRelation = new AbstractionRelationship(client, supplier, current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
                if ((client instanceof Class) && (supplier instanceof Interface))
                    ((Class) client).addRequiredInterface((Interface) supplier);
                if ((client instanceof Package) && (supplier instanceof Interface))
                    ((br.ufpr.dinf.gres.architecture.representation.Package) client).addRequiredInterface((Interface) supplier);
            } else {
                System.out.println("null");
            }
        }

    }

    private void importUsageRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("usage");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.ufpr.dinf.gres.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.ufpr.dinf.gres.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));

            if (supplier != null && client != null) {
                UsageRelationship newRelation = new UsageRelationship("", supplier, client, current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
                if ((client instanceof Class) && (supplier instanceof Interface))
                    ((Class) client).addRequiredInterface((Interface) supplier);
                if ((client instanceof Package) && (supplier instanceof Interface))
                    ((br.ufpr.dinf.gres.architecture.representation.Package) client).addRequiredInterface((Interface) supplier);
            } else {
                System.out.println("null");
            }
        }

    }

    private void importReferencePackage(Element node, Architecture architecture) {

        NodeList aClass = node.getElementsByTagName("reference");

        for (int i = 0; i < aClass.getLength(); i++) {
            Element current = (Element) aClass.item(i);
            String package_ = current.getAttribute("package");
            String parent_ = current.getAttribute("parent");
            architecture.movePackageToParent(package_, parent_);
        }
    }

    private void importVariability(Element node, Architecture architecture) {
        NodeList variabilityList = node.getElementsByTagName("variability");
        for (int i = 0; i < variabilityList.getLength(); i++) {
            try {
                Element current = (Element) variabilityList.item(i);
                //String name, String minSelection, String maxSelection, String bindingTime, boolean allowsAddingVar, String ownerClass, String idPackageOwner
                //<variability id="VARIABILITY#1" name="Variability Name" variationPoint="CLASS#6" constraint="Exclusive" bindingTime="DESIGN_TIME" allowsBindingVar="true" min="1" max="1">

                Variability variability = new Variability(current.getAttribute("name"), current.getAttribute("min"), current.getAttribute("max"), current.getAttribute("bindingTime"), current.getAttribute("allowsBindingVar").equals("true"), current.getAttribute("variationPoint"), "");
                variability.setId(current.getAttribute("id"));
                variability.setConstraint(current.getAttribute("constraint"));
                br.ufpr.dinf.gres.architecture.representation.Element el = architecture.findElementById(variability.getOwnerClass());
                br.ufpr.dinf.gres.architecture.representation.Package pkg = architecture.findPackageOfElement(el.getId());
                if (pkg != null)
                    variability.setIdPackageOwner(pkg.getId());
                ArrayList<String> variants = getVariants(current);
                List<Variant> variantsList = new ArrayList<>();
                for (String id : variants) {
                    Variant vt = new Variant();
                    br.ufpr.dinf.gres.architecture.representation.Element ve = architecture.findElementById(id);
                    vt.setVariantElement(ve);
                    vt.setName(ve.getName());
                    vt.andRootVp(el.getId());
                    variantsList.add(vt);
                }

                VariationPoint vp = new VariationPoint(el, variantsList, current.getAttribute("bindingTime"));
                variability.setVariationPoint(vp);


                for (Variant variant : variantsList) {
                    variability.addVariant(variant);
                    variant.addVariability(variability);
                    variant.addVariationPoint(vp);
                    // set variant to element
                    variant.getVariantElement().setVariant(variant);
                }

                architecture.getAllVariabilities().add(variability);
                architecture.getAllVariationPoints().add(vp);

                vp.getVariationPointElement().setVariationPoint(vp);

                for (Variant variant : variantsList) {
                    architecture.getAllVariants().add(variant);
                    variant.getVariantElement().setVariant(variant);
                }

                Variant vpVariant = new Variant();
                vpVariant.setVariantElement(vp.getVariationPointElement());
                vpVariant.setName(vp.getVariationPointElement().getName());
                vpVariant.setRootVP(vp.getVariationPointElement().getName());
                vpVariant.addVariability(variability);
                vpVariant.setVariantType("variationPoint");

                vp.getVariationPointElement().setVariant(vpVariant);
            } catch (Exception ex) {
                System.out.println(ex);

            }
        }
    }

    private ArrayList<String> getVariants(Element node) {
        ArrayList<String> lstVariant = new ArrayList<>();
        NodeList generalizations = node.getElementsByTagName("variant");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);
            lstVariant.add(current.getAttribute("id"));
        }
        return lstVariant;
    }

    public Package getModel() {
        return this.model;
    }

}