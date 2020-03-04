package arquitetura.representation.relationship;

import arquitetura.helpers.ElementsTypes;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Element;

import java.util.ArrayList;
import java.util.List;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AssociationRelationship extends Relationship {

    private final List<AssociationEnd> participants = new ArrayList<AssociationEnd>();

    public AssociationRelationship(String id) {
        setId(id);
    }

    public AssociationRelationship(Element class1, Element class2) {
        setId(UtilResources.getRandonUUID());

        Multiplicity multi1 = new Multiplicity("1","1");
        Multiplicity multi2 = new Multiplicity("1","1");
        getParticipants().add(new AssociationEnd(class1, false, "association", multi1, ""));
        getParticipants().add(new AssociationEnd(class2, false, "association", multi2, ""));

        super.setType(ElementsTypes.ASSOCIATION);

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
            if (other.participants != null)
                return false;
        } else if (!participants.containsAll(other.participants))
            return false;
        return true;
    }

}