package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;


/**
 * Aggregation operations class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AggregationOperations {

    private static final String SHARED = "shared";
    private final DocumentManager doc;
    private AssociationEnd client;
    private AssociationEnd target;
    private String name;
    private String multiplicityClassTarget;
    private String multiplicityClassClient;

    public AggregationOperations(DocumentManager doc, String multiplicityClassClient, String multiplicityClassTarget) {
        this.doc = doc;
        this.multiplicityClassClient = multiplicityClassClient;
        this.multiplicityClassTarget = multiplicityClassTarget;
    }

    public AggregationOperations(DocumentManager doc, Architecture a) {
        this.doc = doc;
    }

    public AggregationOperations between(AssociationEnd element) {
        this.client = element;
        return this;
    }

    public AggregationOperations and(AssociationEnd element) {
        this.target = element;
        return this;
    }

    public String build() {
        final AssociationNode associationNode = new AssociationNode(doc, null);
        Document.executeTransformation(doc, () -> associationNode.createAssociation(client, target, name, SHARED));
        return "";
    }

    public AggregationOperations createRelation() {
        return new AggregationOperations(this.doc, multiplicityClassClient, multiplicityClassTarget);
    }

    public AggregationOperations withName(String relationshipName) {
        if (relationshipName == null)
            this.name = "";
        else
            this.name = relationshipName;
        return this;
    }
}