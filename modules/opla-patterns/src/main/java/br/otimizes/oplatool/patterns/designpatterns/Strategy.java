package br.otimizes.oplatool.patterns.designpatterns;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.models.ps.PS;
import br.otimizes.oplatool.patterns.models.ps.impl.PSPLAStrategy;
import br.otimizes.oplatool.patterns.models.ps.impl.PSStrategy;
import br.otimizes.oplatool.patterns.util.AlgorithmFamilyUtil;
import br.otimizes.oplatool.patterns.util.ElementUtil;
import br.otimizes.oplatool.patterns.util.RelationshipUtil;
import br.otimizes.oplatool.patterns.util.StrategyUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class Strategy.
 */
public class Strategy extends DesignPattern {

    private static volatile Strategy INSTANCE;

    private Strategy() {
        super("Strategy", "Behavioral");
    }

    public static synchronized Strategy getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Strategy();
        }
        return INSTANCE;
    }

    @Override
    public boolean verifyPS(Scope scope) {
        List<AlgorithmFamily> familiesInScope = AlgorithmFamilyUtil.getFamiliesFromScope(scope);
        Collections.sort(familiesInScope);
        boolean isPs = false;
        for (int i = familiesInScope.size() - 1; i >= 0; i--) {
            AlgorithmFamily iFamily = familiesInScope.get(i);
            List<Element> participants = iFamily.getParticipants();
            List<Element> elementsInScope = new ArrayList<>(scope.getElements());
            elementsInScope.removeAll(participants);
            List<Element> contexts = new ArrayList<>();
            for (Element element : elementsInScope) {
                List<Element> usedElements = new ArrayList<>();
                for (Relationship relationship : ElementUtil.getRelationships(element)) {
                    Element usedElement = RelationshipUtil.getUsedElementFromRelationship(relationship);
                    if (usedElement != null && !usedElement.equals(element)) {
                        usedElements.add(usedElement);
                    }
                }
                for (int j = 0; j < usedElements.size(); j++) {
                    if (!participants.contains(usedElements.get(j))) {
                        usedElements.remove(j);
                        j--;
                    }
                }
                if (!usedElements.isEmpty()) {
                    contexts.add(element);
                }
            }
            isPs = isContextEmpty(scope, isPs, iFamily, contexts);
        }
        return isPs;
    }

    private boolean isContextEmpty(Scope scope, boolean isPs, AlgorithmFamily iFamily, List<Element> contexts) {
        if (!contexts.isEmpty()) {
            PSStrategy psStrategy = new PSStrategy(contexts, iFamily);
            if (!scope.getPSs(this).contains(psStrategy)) {
                scope.addPS(psStrategy);
            }
            isPs = true;
        }
        return isPs;
    }

    @Override
    public boolean verifyPSPLA(Scope scope) {
        boolean isPsPla = false;
        if (verifyPS(scope)) {
            for (PS ps : scope.getPSs(this)) {
                PSStrategy psStrategy = (PSStrategy) ps;
                List<Element> contexts = psStrategy.getContexts();
                AlgorithmFamily algorithmFamily = psStrategy.getAlgorithmFamily();
                if (StrategyUtil.areTheAlgorithmFamilyAndContextsPartOfAVariability(algorithmFamily, contexts)) {
                    PSPLAStrategy psPlaStrategy = new PSPLAStrategy(contexts, algorithmFamily);
                    if (!scope.getPSsPLA(this).contains(psPlaStrategy)) {
                        scope.addPSPLA(psPlaStrategy);
                        isPsPla = true;
                    }
                }
            }
        }
        return isPsPla;
    }

    @Override
    public boolean apply(Scope scope) {
        boolean applied = false;
        List<PS> pSs = scope.getPSs(this);
        if (!pSs.isEmpty()) {
            PSStrategy psStrategy = (PSStrategy) pSs.get(0);
            AlgorithmFamily algorithmFamily = psStrategy.getAlgorithmFamily();
            List<Element> participants = psStrategy.getAlgorithmFamily().getParticipants();
            Interface strategyInterface = StrategyUtil.getStrategyInterfaceFromAlgorithmFamily(algorithmFamily);
            if (strategyInterface == null) {
                strategyInterface = StrategyUtil.createStrategyInterfaceForAlgorithmFamily(algorithmFamily);
            } else participants.remove(strategyInterface);
            List<Element> adapterList = new ArrayList<>();
            List<Element> adapteeList = new ArrayList<>();
            ElementUtil.implementInterface(participants, strategyInterface, adapterList, adapteeList);
            participants.removeAll(adapteeList);
            for (Element adapterClass : adapterList) {
                if (!participants.contains(adapterClass)) {
                    participants.add(adapterClass);
                }
            }
            for (Element participant : participants) {
                for (Concern concern : participant.getOwnConcerns()) {
                    if (!strategyInterface.containsConcern(concern)) {
                        try {
                            strategyInterface.addConcern(concern.getName());
                        } catch (ConcernNotFoundException ex) {
                            Logger.getLogger(Strategy.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            List<Element> contexts = psStrategy.getContexts();
            StrategyUtil.moveContextsRelationshipWithSameTypeAndName(contexts, new ArrayList<>(CollectionUtils
                    .union(participants, adapteeList)), strategyInterface);
            StrategyUtil.moveVariabilitiesFromContextsToTarget(contexts, new ArrayList<>(CollectionUtils
                    .union(participants, adapteeList)), strategyInterface);
            applied = true;
            addStereotype(strategyInterface);
            addStereotype(participants);
            addStereotype(adapteeList);
        }
        return applied;
    }
}
