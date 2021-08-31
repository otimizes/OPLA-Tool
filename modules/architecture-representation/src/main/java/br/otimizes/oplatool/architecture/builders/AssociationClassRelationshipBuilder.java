package br.otimizes.oplatool.architecture.builders;

import br.otimizes.oplatool.architecture.base.ArchitectureHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationClassRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.MemberEnd;
import org.eclipse.uml2.uml.AssociationClass;
import org.eclipse.uml2.uml.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains association class methods
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationClassRelationshipBuilder extends ArchitectureHelper {

    private final Architecture architecture;
    private final ClassBuilder classBuilder;

    public AssociationClassRelationshipBuilder(Architecture architecture) {
        this.architecture = architecture;
        classBuilder = new ClassBuilder(architecture);
    }

    /**
     * Create association class
     *
     * @param associationClass association class
     * @return association class
     */
    public AssociationClassRelationship create(AssociationClass associationClass) {
        List<MemberEnd> membersEnd = new ArrayList<MemberEnd>();

        Class classAssociation = classBuilder.create(associationClass);

        for (Type t : associationClass.getEndTypes()) {
            membersEnd.add(new MemberEnd("none", null, "public", architecture.findElementById(getModelHelper().getXmiId(t))));
        }

        Type ownedEnd = associationClass.getOwnedEnds().get(0).getType();
        Element owned = architecture.findElementById(getModelHelper().getXmiId(ownedEnd));
        String idOwner = null;
        if (!associationClass.getPackage().getName().equalsIgnoreCase("model"))
            idOwner = getModelHelper().getXmiId(associationClass.getOwner());

        AssociationClassRelationship ascc = new AssociationClassRelationship(associationClass.getName(),
                membersEnd,
                owned,
                getModelHelper().getXmiId(associationClass),
                idOwner,
                classAssociation);
        return ascc;
    }

}