package arquitetura.touml;

import arquitetura.helpers.UtilResources;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class RelationshipsXMI {

//	<eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_mEg_AImZEeOgtrpexJcSmg" source="Stereotype_Annotation">
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueMImbEeOgtrpexJcSmg" key="StereotypeWithQualifiedNameList" value=""/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueMYmbEeOgtrpexJcSmg" key="StereotypeList" value=",profile::Pattern1"/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueMombEeOgtrpexJcSmg" key="Stereotype_Presentation_Kind" value="HorizontalStereo"/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueM4mbEeOgtrpexJcSmg" key="PropStereoDisplay" value=""/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueNImbEeOgtrpexJcSmg" key="StereotypePropertyLocation" value="With brace"/>
//	</eAnnotations>

    public static Element enableVisibleStereotypesWithBraces(Document docNotation, List<String> stereotypes) {
        Element eAnnotations = docNotation.createElement("eAnnotations");

        String strStereotypes = stereotypesToString(stereotypes);

        eAnnotations.setAttribute("xmi:type", "ecore:EAnnotation");
        eAnnotations.setAttribute("xmi:id", UtilResources.getRandonUUID());
        eAnnotations.setAttribute("source", "Stereotype_Annotation");

        eAnnotations.appendChild(createDetailsTag(docNotation, "StereotypeWithQualifiedNameList", ""));
        eAnnotations.appendChild(createDetailsTag(docNotation, "StereotypeList", strStereotypes));
        eAnnotations.appendChild(createDetailsTag(docNotation, "Stereotype_Presentation_Kind", "HorizontalStereo"));
        eAnnotations.appendChild(createDetailsTag(docNotation, "PropStereoDisplay", ""));
        eAnnotations.appendChild(createDetailsTag(docNotation, "StereotypePropertyLocation", "With brace"));

        return eAnnotations;
    }

//    <children xmi:type="notation:DecorationNode" xmi:id="_2TQ2sI2YEeOq_-0kO17OHQ" type="6026">
//    <layoutConstraint xmi:type="notation:Location" xmi:id="_2TQ2sY2YEeOq_-0kO17OHQ" y="40"/>
//  </children>
//  <children xmi:type="notation:DecorationNode" xmi:id="_2TQ2so2YEeOq_-0kO17OHQ" type="6027">
//    <layoutConstraint xmi:type="notation:Location" xmi:id="_2TQ2s42YEeOq_-0kO17OHQ" x="-48" y="90"/>
//  </children>

    /**
     * para poder movimentar o estereotipo visualmente
     */
    public static void layoutConstraint(Document docNotation, Element edges) {
        Element children1 = createChildren(docNotation, "6026");
        Element layoutConstraint = docNotation.createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Location");
        layoutConstraint.setAttribute("y", "40");
        children1.appendChild(layoutConstraint);

        Element children2 = createChildren(docNotation, "6027");
        Element layoutConstraint2 = docNotation.createElement("layoutConstraint");
        layoutConstraint2.setAttribute("xmi:type", "notation:Location");
        layoutConstraint2.setAttribute("y", "40");
        children2.appendChild(layoutConstraint2);

        edges.appendChild(children1);
        edges.appendChild(children2);

    }

    private static Element createChildren(Document docNotation, String type) {
        Element children = docNotation.createElement("children");
        children.setAttribute("xmi:type", "notation:DecorationNode");
        children.setAttribute("xmi:id", UtilResources.getRandonUUID());
        children.setAttribute("type", type);
        return children;
    }

    /**
     * @param docUml    - documento uml
     * @param name      - nome do estereotipo
     * @param idElement - id do relacionamento
     * @return {@link Element}
     */
    public static void createStereotypeTagInUmlFile(Document docUml, String name, String idElement) {
        Node nodeXmi = docUml.getElementsByTagName("uml:Model").item(0);

        Element relationships = docUml.createElement("relationships:" + name);
        relationships.setAttribute("xmi:id", UtilResources.getRandonUUID());
        relationships.setAttribute("base_Relationship", idElement);
        nodeXmi.getParentNode().appendChild(relationships);
    }

    private static String stereotypesToString(List<String> stereotypes) {
        StringBuffer str = new StringBuffer();
        for (String ste : stereotypes)
            str.append("relationships::").append(ste).append(",");

        if (str.charAt(str.length() - 1) == ',')
            return str.substring(0, str.length() - 1);

        return str.toString();

    }

    private static Element createDetailsTag(Document docNotation, String key, String value) {
        String type = "ecore:EStringToStringMapEntry";
        Element details = docNotation.createElement("details");
        details.setAttribute("xmi:type", type);
        details.setAttribute("xmi:id", UtilResources.getRandonUUID());
        details.setAttribute("key", key);
        details.setAttribute("value", value);

        return details;
    }

}
