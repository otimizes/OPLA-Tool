package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.helpers.ModelHelper;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.domain.config.FileConstants;
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

/**
 * This class generate an instance of Architecture using a input pla path
 */
public class ArchitectureBuilderSMarty implements IArchitectureBuilder {

    private static final Logger LOGGER = Logger.getLogger(ArchitectureBuilderPapyrus.class);

    private Package model;

    private ModelHelper modelHelper;

    private Document document;
    private String expression;
    private XPath xPath;
    private NodeList nodeList;

    /**
     * create a instance of this class
     */
    public ArchitectureBuilderSMarty() {
        // RelationshipHolder.clearLists();
        LOGGER.info("Clean Relationships");
        ConcernHolder.INSTANCE.clear();
        LOGGER.info("Model Helper");
        //modelHelper = ModelHelperFactory.getModelHelper();
    }

    /**
     * Create a Architecture using an input PLA. First load a PLA from path (file .smty),
     * after this, use xPath and nodeList to create a node of input file
     * using this node, get all information need to create a Architecture:
     * 1 - Project info
     * 2 - list os concerns (Stereotype)
     * 3 - list of types
     * 4 - diagram
     * The diagram is compound of:
     * a - package
     * b - class
     * c - interface
     * d - relationship
     * e - reference of subpackage with their parent
     * f - variability
     * g - link of element and stereotype
     * using this information, create an Architecture
     * set variable isSMarty from Architecture to true to tell the input is from .smty format
     * set variable toSMarty from Architecture to true to tell to decode to .smty format
     *
     * @param xmiFilePath - PLA file (.smty)
     * @return {@link Architecture} - the Architecture generated from input file
     */
    public Architecture create(String xmiFilePath) {
        try {
            modelHelper = null;
            model = null;
            File file = new File(xmiFilePath);
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            document.getDocumentElement().normalize();
            expression = "/project";
            xPath = XPathFactory.newInstance().newXPath();
            nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
            int tam = xmiFilePath.split(FileConstants.FILE_SEPARATOR.replace("\\", "\\\\")).length;
            String arquitectureName = xmiFilePath.split(FileConstants.FILE_SEPARATOR.replace("\\", "\\\\"))[tam - 1].replace(".smty", "");
            Architecture architecture = new Architecture(arquitectureName);
            architecture.setSMarty(true);
            architecture.setToSMarty(true);
            Element element = (Element) this.nodeList.item(0);
            architecture.setProjectID(element.getAttribute("id"));
            architecture.setProjectName(element.getAttribute("name"));
            architecture.setProjectVersion(element.getAttribute("version"));
            architecture.setConcerns(importStereotypesSMarty());
            architecture.setTypes(importTypesSMarty());
            importDiagrams(architecture);
            importLinkStereotypesSMarty(architecture);
            for (Class clazz : architecture.getAllClasses()) {
                clazz.setRelationshipHolder(architecture.getRelationshipHolder());
            }
            for (Interface clazz : architecture.getAllInterfaces()) {
                clazz.setRelationshipHolder(architecture.getRelationshipHolder());
            }
            for (br.otimizes.oplatool.architecture.representation.Package clazz : architecture.getAllPackages()) {
                clazz.setRelationshipHolder(architecture.getRelationshipHolder());
            }
            Cloner cloner = new Cloner();
            architecture.setCloner(cloner);
            ArchitectureHolder.setName(architecture.getName());
            return architecture;
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * import the link between elements of architecture and their respective stereotypes
     * this function correct the variant type if need
     *
     * @param architecture - architecture to insert the link of stereotypes
     */
    private void importLinkStereotypesSMarty(Architecture architecture) throws XPathExpressionException {
        ArrayList<Concern> lstConcern = architecture.getConcerns();
        this.expression = "/project/links/link";
        this.nodeList = (NodeList) this.xPath.compile(this.expression).evaluate(this.document, XPathConstants.NODESET);
        for (int i = 0; i < this.nodeList.getLength(); i++) {
            Element element = (Element) this.nodeList.item(i);
            String id_element = element.getAttribute("element");
            String id_stereotype = element.getAttribute("stereotype");
            if (id_element.contains("ATTRIBUTE")) {
                br.otimizes.oplatool.architecture.representation.Element target = architecture.findAttributeById(id_element);
                for (Concern c1 : lstConcern) {
                    if (c1.getId().equals(id_stereotype) && !c1.getPrimitive()) {
                        target.addExternalConcern(c1);
                    }
                }
                continue;
            }
            if (id_element.contains("METHOD")) {
                br.otimizes.oplatool.architecture.representation.Element target = architecture.findMethodById(id_element);
                for (Concern c1 : lstConcern) {
                    if (c1.getId().equals(id_stereotype) && !c1.getPrimitive()) {
                        target.addExternalConcern(c1);
                    }
                }
                continue;
            }
            br.otimizes.oplatool.architecture.representation.Element target = architecture.findElementById(id_element);
            for (Concern c1 : lstConcern) {
                if (c1.getId().equals(id_stereotype) && !c1.getPrimitive()) {
                    try {
                        target.addExternalConcern(c1);
                    } catch (NullPointerException ex) {
                        System.out.println("Impossible to add the concern " + c1 + " on " + id_element);
                    }
                } else {
                    if (c1.getId().equals(id_stereotype)) {
                        for (Variant variant : architecture.getAllVariants()) {
                            if (variant.getVariantElement().getId().equals(target.getId())) {
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

    /**
     * import the base list of concern (stereotype) from file
     *
     * @return ArrayList<Concern> - the list of stereotypes in the file
     */
    private ArrayList<Concern> importStereotypesSMarty() throws XPathExpressionException {
        ArrayList<Concern> listConcern = new ArrayList<>();
        this.expression = "/project/stereotypes/stereotype";
        this.nodeList = (NodeList) this.xPath.compile(this.expression).evaluate(this.document, XPathConstants.NODESET);
        for (int i = 0; i < this.nodeList.getLength(); i++) {
            Element element = (Element) this.nodeList.item(i);
            Concern newConcern = new Concern();
            newConcern.setId(element.getAttribute("id"));
            newConcern.setName(element.getAttribute("name"));
            newConcern.setPrimitive(element.getAttribute("primitive").equals("true"));
            listConcern.add(newConcern);
        }
        return listConcern;
    }

    /**
     * import the base list of types existing in the PLA file
     *
     * @return ArrayList<TypeSmarty> - the list of types
     */
    private ArrayList<TypeSmarty> importTypesSMarty() throws XPathExpressionException {
        ArrayList<TypeSmarty> listTypes = new ArrayList<>();
        this.expression = "/project/types/type";
        this.nodeList = (NodeList) this.xPath.compile(this.expression).evaluate(this.document, XPathConstants.NODESET);
        for (int i = 0; i < this.nodeList.getLength(); i++) {
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


    /**
     * import the class diagram from the file (call function to import all elements in the file)
     *
     * @param architecture - the architecture to be saved
     */
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

    /**
     * import comment of element as description from file
     *
     * @param node
     * @param element
     */
    private void importComments(Element node, br.otimizes.oplatool.architecture.representation.Element element) {
        NodeList aClass = node.getElementsByTagName("description");
        if (aClass.getLength() > 0)
            element.setComments(aClass.item(0).getTextContent());
    }

    /**
     * import all packages from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importPackage(Element node, Architecture architecture) {
        NodeList aClass = node.getElementsByTagName("package");
        for (int i = 0; i < aClass.getLength(); i++) {
            Element current = (Element) aClass.item(i);
            br.otimizes.oplatool.architecture.representation.Package package_ = new br.otimizes.oplatool.architecture.representation.Package(null, current.getAttribute("name"), null, "model", current.getAttribute("id"));
            package_.setPosX(current.getAttribute("x"));
            package_.setPosX(current.getAttribute("y"));
            package_.setWidth(current.getAttribute("width"));
            package_.setHeight(current.getAttribute("height"));
            package_.setMandatory(current.getAttribute("mandatory").equals("true"));
            this.importComments(current, package_);
            architecture.addPackage(package_);
        }
    }

    /**
     * import all Class from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
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
            this.importComments(current, class_);
            if (current.getAttribute("parent").equals("")) {
                architecture.addExternalClass(class_);
            } else {
                architecture.findPackageByID(current.getAttribute("parent")).addExternalClass(class_);
                class_.setNamespace("model::" + architecture.findPackageByID(current.getAttribute("parent")).getName());
            }
        }
    }

    /**
     * import all attributes of an class from the file
     *
     * @param node         - node generated from file
     * @param class_       - class that has the attributes to be imported
     * @param architecture - the architecture to be saved
     */
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
     * import all methods of an class from the file
     *
     * @param node         - node generated from file
     * @param class_       - class that has the method to be imported
     * @param architecture - the architecture to be saved
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

    /**
     * import all parameters of the method of an class from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     * @return ArrayList<ParameterMethod> - list of parameters of the method
     */
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

    /**
     * import all interfaces from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
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
            this.importComments(current, newInterface);
            if (current.getAttribute("parent").equals("")) {
                architecture.addExternalInterface(newInterface);
            } else {
                architecture.findPackageByID(current.getAttribute("parent")).addExternalInterface(newInterface);
                newInterface.setNamespace("model::" + architecture.findPackageByID(current.getAttribute("parent")).getName());
            }
        }
    }

    /**
     * import all methods of an method from the file
     *
     * @param node         - node generated from file
     * @param interface_   - interface that has the method to be imported
     * @param architecture - the architecture to be saved
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


    /**
     * import all relationships from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importRelationship(Element node, Architecture architecture) {
        importAbstractionRelationship(node, architecture);
        importAssociationRelationship(node, architecture);
        importDependencyRelationship(node, architecture);
        importGeneralizationRelationship(node, architecture);
        importMutexRelationship(node, architecture);
        importRealizationRelationship(node, architecture);
        importRequiresRelationship(node, architecture);
        importUsageRelationship(node, architecture);
    }

    /**
     * import all generalization relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importGeneralizationRelationship(Element node, Architecture architecture) {
        NodeList generalizations = node.getElementsByTagName("generalization");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);
            br.otimizes.oplatool.architecture.representation.Element child = architecture.findElementById(current.getAttribute("source"));
            br.otimizes.oplatool.architecture.representation.Element parent = architecture.findElementById(current.getAttribute("target"));
            if (parent != null && child != null) {
                GeneralizationRelationship newRelation = new GeneralizationRelationship(parent, child, architecture.getRelationshipHolder(), current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
            }
        }
    }

    /**
     * import all realization relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importRealizationRelationship(Element node, Architecture architecture) {
        NodeList generalizations = node.getElementsByTagName("realization");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);
            br.otimizes.oplatool.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("interface"));
            br.otimizes.oplatool.architecture.representation.Element client = architecture.findElementById(current.getAttribute("class"));
            if (client != null && supplier != null) {
                RealizationRelationship newRelation = new RealizationRelationship(client, supplier, "", current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
                if ((client instanceof Class) && (supplier instanceof Interface)) {
                    ((Class) client).addImplementedInterface((Interface) supplier);
                }
                if ((client instanceof br.otimizes.oplatool.architecture.representation.Package) && (supplier instanceof Interface)) {
                    ((br.otimizes.oplatool.architecture.representation.Package) client).addImplementedInterface((Interface) supplier);
                }
            }
        }
    }

    /**
     * import all association relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importAssociationRelationship(Element node, Architecture architecture) {
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

    /**
     * import source element of an association from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     * @param ae1          - associationEnd that will get the source
     * @param direction    - direction of association
     * @param type         - type of association
     * @return ae1 - associationEnd created
     */
    private AssociationEnd importAssociationSource(Element node, Architecture architecture, AssociationEnd ae1, boolean direction, String type) {
        NodeList generalizations = node.getElementsByTagName("source");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);
            Multiplicity mult1 = new Multiplicity(current.getAttribute("sourceMin"), current.getAttribute("sourceMax"));
            br.otimizes.oplatool.architecture.representation.Element part1 = architecture.findElementById(current.getAttribute("entity"));
            ae1 = new AssociationEnd(part1, direction, type, mult1, current.getAttribute("sourceName"));
            ae1.setPosX(current.getAttribute("sourceX"));
            ae1.setPosY(current.getAttribute("sourceY"));
            ae1.setVisibility(current.getAttribute("sourceVisibility"));
        }
        return ae1;
    }

    /**
     * import target element of an association from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     * @param ae2          - associationEnd that will get the source
     * @param direction    - direction of association
     * @param type         - type of association
     * @return ae2 - associationEnd created
     */
    private AssociationEnd importAssociationTarget(Element node, Architecture architecture, AssociationEnd ae2, boolean direction, String type) {
        NodeList generalizations = node.getElementsByTagName("target");
        for (int i = 0; i < generalizations.getLength(); i++) {
            Element current = (Element) generalizations.item(i);

            Multiplicity mult1 = new Multiplicity(current.getAttribute("targetMin"), current.getAttribute("targetMax"));
            br.otimizes.oplatool.architecture.representation.Element part1 = architecture.findElementById(current.getAttribute("entity"));

            ae2 = new AssociationEnd(part1, direction, type, mult1, current.getAttribute("targetName"));
            ae2.setPosX(current.getAttribute("targetX"));
            ae2.setPosY(current.getAttribute("targeteY"));
            ae2.setVisibility(current.getAttribute("targetVisibility"));

        }
        return ae2;
    }

    /**
     * import all dependency relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importDependencyRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("dependency");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.otimizes.oplatool.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.otimizes.oplatool.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));
            if (supplier != null && client != null) {
                DependencyRelationship newRelation = new DependencyRelationship(supplier, client, "", current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
                if ((client instanceof Class) && (supplier instanceof Interface))
                    ((Class) client).addRequiredInterface((Interface) supplier);
                if ((client instanceof Package) && (supplier instanceof Interface))
                    ((br.otimizes.oplatool.architecture.representation.Package) client).addRequiredInterface((Interface) supplier);
            }
        }
    }

    /**
     * import all mutex relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importMutexRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("mutex");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.otimizes.oplatool.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.otimizes.oplatool.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));
            if (supplier != null && client != null) {
                MutexRelationship newRelation = new MutexRelationship(supplier, client, "", current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
            }
        }
    }

    /**
     * import all requires relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importRequiresRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("requires");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.otimizes.oplatool.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.otimizes.oplatool.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));
            if (supplier != null && client != null) {
                RequiresRelationship newRelation = new RequiresRelationship(supplier, client, "", current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
            }
        }

    }

    /**
     * import all abstraction relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importAbstractionRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("abstraction");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.otimizes.oplatool.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.otimizes.oplatool.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));
            if (supplier != null && client != null) {
                //System.out.println("Create dependency");
                AbstractionRelationship newRelation = new AbstractionRelationship(client, supplier, current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
            }
        }

    }

    /**
     * import all usage relationship from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importUsageRelationship(Element node, Architecture architecture) {
        NodeList relations = node.getElementsByTagName("usage");
        for (int i = 0; i < relations.getLength(); i++) {
            Element current = (Element) relations.item(i);
            br.otimizes.oplatool.architecture.representation.Element client = architecture.findElementById(current.getAttribute("source"));
            br.otimizes.oplatool.architecture.representation.Element supplier = architecture.findElementById(current.getAttribute("target"));

            if (supplier != null && client != null) {
                UsageRelationship newRelation = new UsageRelationship("", supplier, client, current.getAttribute("id"));
                architecture.getRelationshipHolder().addRelationship(newRelation);
            }
        }
    }

    /**
     * import all reference of package to parent package from file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importReferencePackage(Element node, Architecture architecture) {
        NodeList aClass = node.getElementsByTagName("reference");
        for (int i = 0; i < aClass.getLength(); i++) {
            Element current = (Element) aClass.item(i);
            String package_ = current.getAttribute("package");
            String parent_ = current.getAttribute("parent");
            architecture.movePackageToParent(package_, parent_);
        }
    }

    /**
     * import all variability from the file
     *
     * @param node         - node generated from file
     * @param architecture - the architecture to be saved
     */
    private void importVariability(Element node, Architecture architecture) {
        NodeList variabilityList = node.getElementsByTagName("variability");
        for (int i = 0; i < variabilityList.getLength(); i++) {
            try {
                Element current = (Element) variabilityList.item(i);
                Variability variability = new Variability(current.getAttribute("name"), current.getAttribute("min"), current.getAttribute("max"), current.getAttribute("bindingTime"), current.getAttribute("allowsBindingVar").equals("true"), current.getAttribute("variationPoint"), "");
                variability.setId(current.getAttribute("id"));
                variability.setConstraint(current.getAttribute("constraint"));
                br.otimizes.oplatool.architecture.representation.Element el = architecture.findElementById(variability.getOwnerClass());
                br.otimizes.oplatool.architecture.representation.Package pkg = architecture.findPackageOfElementID(el.getId());
                if (pkg != null)
                    variability.setIdPackageOwner(pkg.getId());
                ArrayList<String> variants = getVariants(current);
                List<Variant> variantsList = new ArrayList<>();
                for (String id : variants) {
                    Variant vt = new Variant();
                    br.otimizes.oplatool.architecture.representation.Element ve = architecture.findElementById(id);
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

    /**
     * import id of all variants element of an specific variability from file
     *
     * @param node - node generated from file
     * @return ArrayList<String> - id of elements that compund the variants
     */
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