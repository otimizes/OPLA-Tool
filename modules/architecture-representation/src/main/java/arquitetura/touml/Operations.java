package arquitetura.touml;

import arquitetura.representation.Architecture;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class Operations {

    private DocumentManager doc;
    private ClassOperations classOperation;
    private AssociationOperations associationOperation;
    private PackageOperations packageOperaiton;
    private DependencyOperations dependencyOperation;
    private UsageOperations usageOperation;
    private GeneralizationOperations generalizationOperations;
    private CompositionOperations compositionOperations;
    private AggregationOperations aggregationOperations;
    private NoteOperations noteOperations;
    private StereotypeOperations concernOperation;
    private RealizationsOperations realizationOperations;
    private AssociationKlassOperations associationKlassOperations;
    private AbstractionOperations abstractionOperations;

    public Operations(DocumentManager doc2, Architecture a) {
        this.doc = doc2;
        createClassOperation(a);
        createAssociationOperations();
        createPackageOperations();
        createDependencyOperations(a);
        createUsageOperations(a);
        createGeneralizationOperations();
        createCompositionOperations();
        createAggrationOperations(a);
        createNoteOperations(a);
        createConcernOperation();
        createRealaizationsOperations();
        createAssociationKlassOperations(a);
        createAbstractionOperations(a);
    }

    private void createAbstractionOperations(Architecture a) {
        this.abstractionOperations = new AbstractionOperations(doc);
    }

    private void createAssociationKlassOperations(Architecture a) {
        this.associationKlassOperations = new AssociationKlassOperations(doc, a);
    }

    private void createRealaizationsOperations() {
        this.realizationOperations = new RealizationsOperations(doc);
    }

    private void createConcernOperation() {
        this.concernOperation = new StereotypeOperations(doc);
    }

    private void createNoteOperations(Architecture a) {
        this.noteOperations = new NoteOperations(doc, a);
    }

    private void createAggrationOperations(Architecture a) {
        this.aggregationOperations = new AggregationOperations(doc, a);
    }

    private void createCompositionOperations() {
        this.compositionOperations = new CompositionOperations(doc);
    }

    private void createGeneralizationOperations() {
        this.generalizationOperations = new GeneralizationOperations(doc);
    }

    private void createUsageOperations(Architecture a) {
        this.usageOperation = new UsageOperations(doc, a);
    }

    private void createDependencyOperations(Architecture a) {
        this.dependencyOperation = new DependencyOperations(doc, a);
    }

    private void createPackageOperations() {
        this.packageOperaiton = new PackageOperations(doc);
    }

    private void createAssociationOperations() {
        this.associationOperation = new AssociationOperations(doc);
    }

    private void createClassOperation(Architecture a) {
        this.classOperation = new ClassOperations(doc, a);
    }

    public ClassOperations forClass() {
        return classOperation;
    }

    public StereotypeOperations forConcerns() {
        return concernOperation;
    }

    public AssociationOperations forAssociation() {
        return associationOperation;
    }

    public PackageOperations forPackage() {
        return packageOperaiton;
    }

    public DependencyOperations forDependency() {
        return dependencyOperation;
    }

    public UsageOperations forUsage() {
        return usageOperation;
    }

    ;

    public GeneralizationOperations forGeneralization() {
        return generalizationOperations;
    }

    public CompositionOperations forComposition() {
        return compositionOperations;
    }

    public AggregationOperations forAggregation() {
        return aggregationOperations;
    }

    public NoteOperations forNote() {
        return noteOperations;
    }

    public RealizationsOperations forRealization() {
        return realizationOperations;
    }

    public AssociationKlassOperations forAssociationClass() {
        return associationKlassOperations;
    }

    public AbstractionOperations forAbstraction() {
        return abstractionOperations;
    }

}