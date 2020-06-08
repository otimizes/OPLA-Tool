package br.otimizes.oplatool.patterns.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import br.otimizes.oplatool.patterns.repositories.ArchitectureRepository;
import br.otimizes.oplatool.architecture.exceptions.AttributeNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.MethodNotFoundException;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.papyrus.touml.Types;
import br.otimizes.oplatool.architecture.papyrus.touml.VisibilityKind;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.ParameterMethod;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.representation.relationship.UsageRelationship;

/**
 * The Class MediatorUtil.
 */
public class MediatorUtil {

    /**
     * Instantiates a new mediator util.
     */
    private MediatorUtil() {
    }

    /**
     * Gets the or create event of interest class.
     *
     * @return the event of interest class
     */
    public static Class getOrCreateEventOfInterestClass() {
        Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

        List<Class> eventOfInterestList = architecture.findClassByName("EventOfInterest");
        Class eventOfInterest;
        if (eventOfInterestList == null || eventOfInterestList.isEmpty()) {
            eventOfInterest = architecture.createClass("EventOfInterest", false);

            //Creating attributes
            eventOfInterest.createAttribute("invoker", Types.custom("Object"), VisibilityKind.PRIVATE_LITERAL);
            eventOfInterest.createAttribute("action", Types.STRING, VisibilityKind.PRIVATE_LITERAL);
            eventOfInterest.createAttribute("parameters", Types.custom("Object"), VisibilityKind.PRIVATE_LITERAL);

            //Creating methods
            eventOfInterest.createMethod("getInvoker", "Object", false, null);
            eventOfInterest.createMethod("getAction", "String", false, null);
            eventOfInterest.createMethod("getParameters", "Object", false, null);

            eventOfInterest.createMethod("setInvoker", "void", false, Arrays.asList(new ParameterMethod("invoker", "Object", "in")));
            eventOfInterest.createMethod("setAction", "void", false, Arrays.asList(new ParameterMethod("action", "String", "in")));
            eventOfInterest.createMethod("setParameters", "void", false, Arrays.asList(new ParameterMethod("parameters", "Object", "in")));
        } else {
            eventOfInterest = eventOfInterestList.get(0);

            try {
                eventOfInterest.findAttributeByName("invoker");
            } catch (AttributeNotFoundException ex) {
                eventOfInterest.createAttribute("invoker", Types.custom("Object"), VisibilityKind.PRIVATE_LITERAL);
            }
            try {
                eventOfInterest.findAttributeByName("action");
            } catch (AttributeNotFoundException ex) {
                eventOfInterest.createAttribute("action", Types.STRING, VisibilityKind.PRIVATE_LITERAL);
            }
            try {
                eventOfInterest.findAttributeByName("parameters");
            } catch (AttributeNotFoundException ex) {
                eventOfInterest.createAttribute("parameters", Types.custom("Object"), VisibilityKind.PRIVATE_LITERAL);
            }

            try {
                eventOfInterest.findMethodByName("getInvoker");
            } catch (MethodNotFoundException ex) {
                eventOfInterest.createMethod("getInvoker", "Object", false, null);
            }
            try {
                eventOfInterest.findMethodByName("getAction");

            } catch (MethodNotFoundException ex) {
                eventOfInterest.createMethod("getAction", "String", false, null);
            }
            try {
                eventOfInterest.findMethodByName("getParameters");
            } catch (MethodNotFoundException ex) {
                eventOfInterest.createMethod("getParameters", "Object", false, null);
            }

            try {
                eventOfInterest.findMethodByName("setInvoker");
            } catch (MethodNotFoundException ex) {
                eventOfInterest.createMethod("setInvoker", "void", false, Arrays.asList(new ParameterMethod("invoker", "Object", "in")));
            }
            try {
                eventOfInterest.findMethodByName("setAction");
            } catch (MethodNotFoundException ex) {
                eventOfInterest.createMethod("setAction", "void", false, Arrays.asList(new ParameterMethod("action", "String", "in")));
            }
            try {
                eventOfInterest.findMethodByName("setParameters");
            } catch (MethodNotFoundException ex) {
                eventOfInterest.createMethod("setParameters", "void", false, Arrays.asList(new ParameterMethod("parameters", "Object", "in")));
            }

        }

        return eventOfInterest;
    }

    /**
     * Gets the or create mediator interface.
     *
     * @param participants the participants
     * @param concern the concern
     * @param eventOfInterest the event of interest
     * @return the mediator interface
     * @throws Exception the exception
     */
    public static Interface getOrCreateMediatorInterface(List<Element> participants, Concern concern, Class eventOfInterest) throws Exception {
        Interface mediator = null;

        for (Relationship relationship : ElementUtil.getRelationships(eventOfInterest)) {
            Element client = RelationshipUtil.getClientElementFromRelationship(relationship);
            if (client != null
                    && !client.equals(eventOfInterest)
                    && client.getOwnConcerns().contains(concern)
                    && client instanceof Interface
                    && client.getName().endsWith("Mediator")) {
                mediator = (Interface) client;
                break;
            }
        }

        if (mediator == null) {
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

            String namespace = ElementUtil.getNameSpace(participants);
            String packageName = UtilResources.extractPackageName(namespace);
            boolean naArquitetura = packageName.equalsIgnoreCase("model");
            if (naArquitetura) {
                mediator = architecture.createInterface(Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) + "Mediator");
            } else {
                Package aPackage = architecture.findPackageByName(UtilResources.extractPackageName(namespace));
                mediator = aPackage.createInterface(Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) + "Mediator");
            }

            mediator.addConcern(concern.getName());

            Method operation = new Method(Character.toLowerCase(concern.getName().charAt(0)) + concern.getName().substring(1) + "Event", "void", mediator.getName(), false, UUID.randomUUID().toString());
            operation.getParameters().add(new ParameterMethod("eventOfInterest", "Object", "in"));
            mediator.addExternalOperation(operation);

            RelationshipUtil.createNewUsageRelationship("uses", mediator, eventOfInterest);
        }

        return mediator;
    }

    /**
     * Gets the or create mediator class.
     *
     * @param participants the participants
     * @param concern the concern
     * @param mediatorInterface the mediator interface
     * @return the mediator class
     * @throws ConcernNotFoundException the concern not found exception
     */
    public static Class getOrCreateMediatorClass(List<Element> participants, Concern concern, Interface mediatorInterface) throws ConcernNotFoundException {
        Class mediator = null;

        for (Element subElement : ElementUtil.getAllSubElements(mediatorInterface)) {
            if (subElement != null
                    && subElement.getOwnConcerns().contains(concern)
                    && subElement instanceof Class
                    && !((Class) subElement).isAbstract()
                    && subElement.getName().endsWith("MediatorImpl")) {
                mediator = (Class) subElement;
                break;
            }
        }

        if (mediator == null) {
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

            String namespace = ElementUtil.getNameSpace(participants);
            String packageName = UtilResources.extractPackageName(namespace);
            boolean naArquitetura = packageName.equalsIgnoreCase("model");
            if (naArquitetura) {
                mediator = architecture.createClass(Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) + "MediatorImpl", false);
            } else {
                Package aPackage = architecture.findPackageByName(UtilResources.extractPackageName(namespace));
                mediator = aPackage.createClass(Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) + "MediatorImpl", false);
            }

            mediator.addConcern(concern.getName());

            ElementUtil.implementInterface(mediator, mediatorInterface);
            mediator.addImplementedInterface(mediatorInterface);
        }

        return mediator;
    }

    /**
     * Gets the or create colleague interface.
     *
     * @param participants the participants
     * @param concern the concern
     * @param mediatorInterface the mediator interface
     * @param eventOfInterest the event of interest
     * @return the colleague interface
     * @throws ConcernNotFoundException the concern not found exception
     */
    public static Interface getOrCreateColleagueInterface(List<Element> participants, Concern concern, Interface mediatorInterface, Class eventOfInterest) throws ConcernNotFoundException {
        Interface colleague = null;

        root:
        for (Relationship relationship : ElementUtil.getRelationships(eventOfInterest)) {
            Element maybeColleague = RelationshipUtil.getClientElementFromRelationship(relationship);
            if (maybeColleague != null
                    && !maybeColleague.equals(mediatorInterface)
                    && !maybeColleague.equals(eventOfInterest)
                    && maybeColleague.getOwnConcerns().contains(concern)
                    && maybeColleague instanceof Interface
                    && maybeColleague.getName().endsWith("Colleague")) {
                for (Relationship relationshipMediator : ElementUtil.getRelationships(maybeColleague)) {
                    if (relationshipMediator instanceof UsageRelationship
                            || relationshipMediator instanceof DependencyRelationship) {
                        if (RelationshipUtil.getClientElementFromRelationship(relationshipMediator).equals(maybeColleague)
                                && RelationshipUtil.getUsedElementFromRelationship(relationshipMediator).equals(mediatorInterface)) {
                            colleague = (Interface) maybeColleague;
                            break root;
                        }
                    }
                }
            }
        }

        if (colleague == null) {
            Architecture architecture = ArchitectureRepository.getCurrentArchitecture();

            String namespace = ElementUtil.getNameSpace(participants);
            String packageName = UtilResources.extractPackageName(namespace);
            boolean naArquitetura = packageName.equalsIgnoreCase("model");
            if (naArquitetura) {
                colleague = architecture.createInterface(Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) + "Colleague");
            } else {
                Package aPackage = architecture.findPackageByName(UtilResources.extractPackageName(namespace));
                colleague = aPackage.createInterface(Character.toUpperCase(concern.getName().charAt(0)) + concern.getName().substring(1) + "Colleague");
            }

            colleague.addConcern(concern.getName());

            Method operation = new Method("attachMediator", "void", colleague.getName(), false, UUID.randomUUID().toString());
            operation.getParameters().add(new ParameterMethod("mediator", "Object", "in"));
            colleague.addExternalOperation(operation);

            operation = new Method("detachMediator", "void", colleague.getName(), false, UUID.randomUUID().toString());
            operation.getParameters().add(new ParameterMethod("mediator", "Object", "in"));
            colleague.addExternalOperation(operation);

            RelationshipUtil.createNewUsageRelationship("uses", colleague, eventOfInterest);
            RelationshipUtil.createNewUsageRelationship("uses", colleague, mediatorInterface);
        }

        return colleague;
    }

    /**
     * Removes the relationships.
     *
     * @param participants the participants
     * @param concern the concern
     */
    public static void removeRelationships(List<Element> participants, Concern concern) {
        Architecture currentArchitecture = ArchitectureRepository.getCurrentArchitecture();

        for (Element element : participants) {
            List<Relationship> relationships = ElementUtil.getRelationships(element);
            for (Relationship relationship : relationships) {
                Element usedElementFromRelationship = RelationshipUtil.getUsedElementFromRelationship(relationship);
                if (usedElementFromRelationship != null
                        && !usedElementFromRelationship.equals(element)
                        && participants.contains(usedElementFromRelationship)
                        && usedElementFromRelationship.getAllConcerns().size() == 1
                        && usedElementFromRelationship.getAllConcerns().contains(concern)) {
                    currentArchitecture.removeRelationship(relationship);
                }
            }
        }
    }
}
