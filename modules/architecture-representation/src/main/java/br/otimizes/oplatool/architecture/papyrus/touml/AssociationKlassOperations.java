package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.generate.GenerateArchitecture;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Attribute;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationClassRelationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Association class operations
 */
public class AssociationKlassOperations {

    private final DocumentManager documentManager;
    private String ownedEnd;
    private String associationEnd2;
    private String id;
    private ElementXmiGenerator elementXmiGenerator;
    private Set<br.otimizes.oplatool.architecture.papyrus.touml.Attribute> attrs;
    private Set<br.otimizes.oplatool.architecture.papyrus.touml.Method> methods;
    private final Architecture architecture;
    private Class associationClass;

    public AssociationKlassOperations(DocumentManager doc, Architecture a) {
        this.documentManager = doc;
        this.architecture = a;
    }

    //TODO refatorar método do main - mover
    private static Set<br.otimizes.oplatool.architecture.papyrus.touml.Method> createMethods(AssociationClassRelationship klass) {
        Set<br.otimizes.oplatool.architecture.papyrus.touml.Method> methods = new HashSet<br.otimizes.oplatool.architecture.papyrus.touml.Method>();
        Set<Method> methodsClass = new HashSet<Method>();

        methodsClass = klass.getAllMethods();
        for (Method method : methodsClass) {

            List<ParameterMethod> paramsMethod = method.getParameters();
            List<Argument> currentMethodParams = new ArrayList<Argument>();

            GenerateArchitecture.createAndAddNewMethodsWithParams(methods, method, paramsMethod, currentMethodParams);

        }

        return methods;
    }

    public AssociationKlassOperations createAssociationClass(AssociationClassRelationship asr) {
        this.id = asr.getId();
        this.ownedEnd = asr.getMemebersEnd().get(0).getType().getId();
        this.associationEnd2 = asr.getMemebersEnd().get(1).getType().getId();
        this.elementXmiGenerator = new ElementXmiGenerator(documentManager, this.architecture);
        this.attrs = buildAttributes(asr);
        this.methods = createMethods(asr);
        this.associationClass = asr.getAssociationClass();
        return this;
    }

    private Set<br.otimizes.oplatool.architecture.papyrus.touml.Attribute> buildAttributes(AssociationClassRelationship asr) {
        Set<br.otimizes.oplatool.architecture.papyrus.touml.Attribute> attrs = new HashSet<br.otimizes.oplatool.architecture.papyrus.touml.Attribute>();
        for (Attribute attribute : asr.getAssociationClass().getAllAttributes()) {
            br.otimizes.oplatool.architecture.papyrus.touml.Attribute attr = br.otimizes.oplatool.architecture.papyrus.touml.Attribute.create()
                    .withName(attribute.getName())
                    .grafics(attribute.isGeneratVisualAttribute())
                    .withConcerns(attribute.getOwnConcerns())
                    .withVisibility(VisibilityKind.getByName(attribute.getVisibility()))
                    .withType(Types.getByName(attribute.getType()));

            attrs.add(attr);
        }

        return attrs;
    }

    public void build() {
        final AssociationClassNode associationClassNode = new AssociationClassNode(this.documentManager, null);

        Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                associationClassNode.createAssociationClass(id, ownedEnd, associationEnd2, associationClass.getName());
            }
        });

        for (final br.otimizes.oplatool.architecture.papyrus.touml.Attribute attribute : this.attrs) {
            Document.executeTransformation(documentManager, new Transformation() {
                public void useTransformation() {
                    elementXmiGenerator.generateAttribute(attribute, id);
                }
            });
        }

        for (final br.otimizes.oplatool.architecture.papyrus.touml.Method method : this.methods) {
            Document.executeTransformation(documentManager, new Transformation() {
                public void useTransformation() {
                    elementXmiGenerator.generateMethod(method, id);
                }
            });
        }

        //Adiciona Interesses nos atributos
        for (br.otimizes.oplatool.architecture.papyrus.touml.Attribute attribute : attrs) {
            for (Concern c : attribute.getConcerns()) {
                elementXmiGenerator.generateConcern(c.getName(), attribute.getId(), "concerns");
            }
        }

        //Adiciona Interesses nos métodos
        for (br.otimizes.oplatool.architecture.papyrus.touml.Method method : methods) {
            for (Concern c : method.getConcerns()) {
                elementXmiGenerator.generateConcern(c.getName(), method.getId(), "concerns");
            }
        }

        //Adiciona Interesses nos métodos
        for (br.otimizes.oplatool.architecture.papyrus.touml.Method method : methods) {
            for (Concern c : method.getConcerns()) {
                elementXmiGenerator.generateConcern(c.getName(), method.getId(), "concerns");
            }
        }

        //Adiciona Interesses na associationClass
        for (Concern c : associationClass.getOwnConcerns()) {
            elementXmiGenerator.generateConcern(c.getName(), associationClass.getId(), "concerns");
        }

    }

}
