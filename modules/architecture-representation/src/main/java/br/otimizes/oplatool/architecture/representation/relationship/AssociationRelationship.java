package br.otimizes.oplatool.architecture.representation.relationship;

import br.otimizes.oplatool.architecture.helpers.ElementsTypes;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.Element;

import java.util.ArrayList;
import java.util.List;


/**
 * Association relationship class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationRelationship extends Relationship {

    private final List<AssociationEnd> participants = new ArrayList<AssociationEnd>();

    public AssociationRelationship(String id) {
        setId(id);
    }

    public AssociationRelationship(Element class1, Element class2) {
        setId(UtilResources.getRandomUUID());
        Multiplicity mul1 = new Multiplicity("1", "1");
        Multiplicity mul2 = new Multiplicity("1", "1");
        getParticipants().add(new AssociationEnd(class1, false, "association", mul1, ""));
        getParticipants().add(new AssociationEnd(class2, false, "association", mul2, ""));

        super.setType(ElementsTypes.ASSOCIATION);

    }

    public AssociationRelationship() {
    }

    public void SetAssociationEnd(AssociationEnd ae) {
        participants.add(ae);
    }

    public List<AssociationEnd> getParticipants() {
        return participants;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((participants == null) ? 0 : participants.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        AssociationRelationship other = (AssociationRelationship) obj;
        if (participants == null) {
            return other.participants == null;
        } else return participants.containsAll(other.participants);
    }

}