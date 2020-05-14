package br.ufpr.dinf.gres.architecture.touml;

import br.ufpr.dinf.gres.architecture.representation.Attribute;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationClassRelationship;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Association class operations
 */
public class AssociationKlassOperations {

    private DocumentManager documentManager;
    private String ownedEnd;
    private String associationEnd2;
    private String id;
    private ElementXmiGenerator elementXmiGenerator;
    private Set<br.ufpr.dinf.gres.architecture.touml.Attribute> attrs;
    private Set<br.ufpr.dinf.gres.architecture.touml.Method> methods;
    private Architecture architecture;
    private Class associationClass;

    public AssociationKlassOperations(DocumentManager doc, Architecture a) {
        this.documentManager = doc;
        this.architecture = a;
    }

    //TODO refatorar método do main - mover
    private static Set<br.ufpr.dinf.gres.architecture.touml.Method> createMethods(AssociationClassRelationship klass) {
        Set<br.ufpr.dinf.gres.architecture.touml.Method> methods = new HashSet<br.ufpr.dinf.gres.architecture.touml.Method>();
        Set<br.ufpr.dinf.gres.architecture.representation.Method> methodsClass = new HashSet<br.ufpr.dinf.gres.architecture.representation.Method>();

        methodsClass = klass.getAllMethods();
        for (br.ufpr.dinf.gres.architecture.representation.Method method : methodsClass) {

            List<ParameterMethod> paramsMethod = method.getParameters();
            List<Argument> currentMethodParams = new ArrayList<Argument>();

            for (ParameterMethod param : paramsMethod) {
                currentMethodParams.add(Argument.create(param.getName(), Types.getByName(param.getType()), param.getDirection()));
            }

            if (method.isAbstract()) {
                br.ufpr.dinf.gres.architecture.touml.Method m = br.ufpr.dinf.gres.architecture.touml.Method.create()
                        .withId(method.getId())
                        .withName(method.getName()).abstractMethod()
                        .withArguments(currentMethodParams)
                        .withConcerns(method.getOwnConcerns())
                        .withReturn(Types.getByName(method.getReturnType())).build();
                methods.add(m);
            } else {
                br.ufpr.dinf.gres.architecture.touml.Method m = br.ufpr.dinf.gres.architecture.touml.Method.create()
                        .withId(method.getId())
                        .withName(method.getName())
                        .withArguments(currentMethodParams)
                        .withConcerns(method.getOwnConcerns())
                        .withReturn(Types.getByName(method.getReturnType())).build();
                methods.add(m);
            }

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

    private Set<br.ufpr.dinf.gres.architecture.touml.Attribute> buildAttributes(AssociationClassRelationship asr) {
        Set<br.ufpr.dinf.gres.architecture.touml.Attribute> attrs = new HashSet<br.ufpr.dinf.gres.architecture.touml.Attribute>();
        for (Attribute attribute : asr.getAssociationClass().getAllAttributes()) {
            br.ufpr.dinf.gres.architecture.touml.Attribute attr = br.ufpr.dinf.gres.architecture.touml.Attribute.create()
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

        br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                associationClassNode.createAssociationClass(id, ownedEnd, associationEnd2, associationClass.getName());
            }
        });

        for (final br.ufpr.dinf.gres.architecture.touml.Attribute attribute : this.attrs) {
            br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(documentManager, new Transformation() {
                public void useTransformation() {
                    elementXmiGenerator.generateAttribute(attribute, id);
                }
            });
        }

        for (final br.ufpr.dinf.gres.architecture.touml.Method method : this.methods) {
            br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(documentManager, new Transformation() {
                public void useTransformation() {
                    elementXmiGenerator.generateMethod(method, id);
                }
            });
        }

        //Adiciona Interesses nos atributos
        for (br.ufpr.dinf.gres.architecture.touml.Attribute attribute : attrs) {
            for (Concern c : attribute.getConcerns()) {
                elementXmiGenerator.generateConcern(c.getName(), attribute.getId(), "concerns");
            }
        }

        //Adiciona Interesses nos métodos
        for (br.ufpr.dinf.gres.architecture.touml.Method method : methods) {
            for (Concern c : method.getConcerns()) {
                elementXmiGenerator.generateConcern(c.getName(), method.getId(), "concerns");
            }
        }

        //Adiciona Interesses nos métodos
        for (br.ufpr.dinf.gres.architecture.touml.Method method : methods) {
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
