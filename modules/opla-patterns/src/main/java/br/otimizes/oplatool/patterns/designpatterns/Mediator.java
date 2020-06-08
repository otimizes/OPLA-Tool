package br.otimizes.oplatool.patterns.designpatterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections4.CollectionUtils;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.models.ps.impl.PSMediator;
import br.otimizes.oplatool.patterns.util.ElementUtil;
import br.otimizes.oplatool.patterns.util.MediatorUtil;
import br.otimizes.oplatool.patterns.util.RelationshipUtil;

/**
 * The Class Mediator.
 */
public class Mediator extends DesignPattern {

    /** The instance. */
    private static volatile Mediator INSTANCE;

    /**
     * Instantiates a new mediator.
     */
    private Mediator() {
        super("Mediator", "Behavioral");
    }

    /**
     * Gets the single instance of Mediator.
     *
     * @return single instance of Mediator
     */
    public static synchronized Mediator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Mediator();
        }
        return INSTANCE;
    }

    /**
     * Verify PS.
     *
     * @param scope the scope
     * @return true, if PS
     */
    @Override
    public boolean verifyPS(Scope scope) {
        boolean isPs = false;

        List<Element> elements = scope.getElements();
        HashMap<Concern, List<Element>> groupedElements = ElementUtil.groupElementsByConcern(elements);
        for (Map.Entry<Concern, List<Element>> entry : groupedElements.entrySet()) {
            Concern concern = entry.getKey();
            List<Element> list = entry.getValue();
            if (concern != null) {
                List<Element> chainOfElements = ElementUtil.getChainOfRelatedElementsWithSameConcern(list, concern);
                int count = 0;
                for (int i = 0; i < chainOfElements.size(); i++) {
                    Element iElement = chainOfElements.get(i);
                    boolean isTypeOfOtherParticipant = false;
                    for (int j = 0; j < chainOfElements.size(); j++) {
                        if (i == j) {
                            continue;
                        }
                        Element jElement = chainOfElements.get(j);
                        if (ElementUtil.isTypeOf(iElement, jElement)) {
                            isTypeOfOtherParticipant = true;
                            break;
                        }
                    }
                    if (!isTypeOfOtherParticipant) {
                        count++;
                        if (count > 2) {
                            break;
                        }
                    }
                }
                if (count > 2) {
                    PSMediator psMediator = new PSMediator(chainOfElements, concern);
                    if (!scope.getPSs().contains(psMediator)) {
                        scope.addPS(psMediator);
                    } else {
                        psMediator = (PSMediator) scope.getPSs().get(scope.getPSs().indexOf(psMediator));
                        psMediator.setParticipants(new ArrayList<>(CollectionUtils.union(chainOfElements, psMediator.getParticipants())));
                    }
                    isPs = true;
                }
            }
        }

        return isPs;
    }

    /**
     * Verify PSPLA.
     *
     * @param scope the scope
     * @return true, if PSPLA
     */
    @Override
    public boolean verifyPSPLA(Scope scope) {
        return verifyPS(scope);
    }

    /**
     * Apply.
     *
     * @param scope the scope
     * @return true, if successful
     */
    @Override
    public boolean apply(Scope scope) {
        boolean applied = false;
        if (!scope.getPSs(this).isEmpty()) {
            try {
                PSMediator psMediator = (PSMediator) scope.getPSs(this).get(0);
                List<Element> participants = psMediator.getParticipants();
                final Concern concern = psMediator.getConcern();

                // Identify or create EventOfInterest class
                Class eventOfInterest = MediatorUtil.getOrCreateEventOfInterestClass();
                participants.remove(eventOfInterest);

                // "" Mediator interface 
                Interface mediatorInterface = MediatorUtil.getOrCreateMediatorInterface(participants, concern, eventOfInterest);
                participants.remove(mediatorInterface);

                // "" Mediator class
                Class mediatorClass = MediatorUtil.getOrCreateMediatorClass(participants, concern, mediatorInterface);
                participants.remove(mediatorClass);

                // "" colleague interface 
                Interface colleagueInterface = MediatorUtil.getOrCreateColleagueInterface(participants, concern, mediatorInterface, eventOfInterest);
                participants.remove(colleagueInterface);

                // Removing relationships
                MediatorUtil.removeRelationships(participants, concern);

                // Implementing Colleague interfaces 
                List<Element> adapterList = new ArrayList<>();
                List<Element> adapteeList = new ArrayList<>();

                ElementUtil.implementInterface(participants, colleagueInterface, adapterList, adapteeList);

                participants.removeAll(adapteeList);
                for (Element adapterClass : adapterList) {
                    if (!participants.contains(adapterClass)) {
                        participants.add(adapterClass);
                    }
                }

                // Using colleagues
                for (Element element : participants) {
                    RelationshipUtil.createNewUsageRelationship("usesColleague", mediatorClass, element);
                }

                // Applying stereotypes
                addStereotype(eventOfInterest);
                addStereotype(mediatorInterface);
                addStereotype(mediatorClass);
                addStereotype(colleagueInterface);
                addStereotype(participants);
                addStereotype(adapteeList);
                applied = true;
            } catch (Exception ex) {
                Logger.getLogger(Mediator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return applied;
    }

}
