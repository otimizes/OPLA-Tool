package br.otimizes.oplatool.patterns.designpatterns;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.repositories.ArchitectureRepository;
import br.otimizes.oplatool.patterns.util.AdapterUtil;
import br.otimizes.oplatool.patterns.util.ElementUtil;
import br.otimizes.oplatool.patterns.util.RelationshipUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class Adapter.
 */
public class Adapter extends DesignPattern {

    private static volatile Adapter INSTANCE;

    private Adapter() {
        super("Adapter", "Structural");
    }

    public static synchronized Adapter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Adapter();
        }
        return INSTANCE;
    }

    @Override
    public boolean verifyPS(Scope scope) {
        return false;
    }

    @Override
    public boolean verifyPSPLA(Scope scope) {
        return false;
    }

    @Override
    public boolean apply(Scope scope) {
        return false;
    }

    public Class applyAdapter(Element target, Element adaptee) {
        Class adapterClass = null;
        if ((target instanceof Class || target instanceof Interface)
                && (adaptee instanceof Class || adaptee instanceof Interface)) {
            try {
                adapterClass = AdapterUtil.getAdapterClass(target, adaptee);
                if (adapterClass == null) {
                    adapterClass = AdapterUtil.createAdapterClass(adaptee);
                }
                implementsAndExtendsAddingAllMethods(target, adapterClass);
                RelationshipUtil.createNewUsageRelationship("adaptee", adapterClass, adaptee);
                Relationship relationshipToBeExcluded = getRelationshipToBeExcluded(target, adaptee);
                if (relationshipToBeExcluded != null) {
                    ArchitectureRepository.getCurrentArchitecture().removeRelationship(relationshipToBeExcluded);
                }
                coyConcerns(target, adaptee, adapterClass);
                Variant variant = adaptee.getVariant();
                if (variant != null) {
                    adaptee.setVariant(null);
                    adapterClass.setVariant(variant);
                    variant.setVariantElement(adapterClass);
                }
                addStereotype(target);
                addStereotype(adaptee);
                addStereotype(adapterClass);
            } catch (Exception ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return adapterClass;
    }

    private void implementsAndExtendsAddingAllMethods(Element target, Class adapterClass) {
        if (target instanceof Class) {
            ElementUtil.extendClass(adapterClass, (Class) target);
        } else {
            ElementUtil.implementInterface(adapterClass, (Interface) target);
        }
    }

    private void coyConcerns(Element target, Element adaptee, Class adapterClass) {
        for (Concern concern : CollectionUtils.union(target.getOwnConcerns(), adaptee.getOwnConcerns())) {
            try {
                adapterClass.addConcern(concern.getName());
            } catch (ConcernNotFoundException ex) {
                Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Relationship getRelationshipToBeExcluded(Element target, Element adaptee) {
        Relationship relationshipToBeExcluded = null;
        if (adaptee.getClass().equals(target.getClass())) {
            for (Relationship relationship : ElementUtil.getRelationships(adaptee)) {
                if (target.equals(RelationshipUtil.getSuperElement(relationship))) {
                    relationshipToBeExcluded = relationship;
                    break;
                }
            }
        } else {
            for (Relationship relationship : ElementUtil.getRelationships(adaptee)) {
                if (target.equals(RelationshipUtil.getImplementedInterface(relationship))) {
                    relationshipToBeExcluded = relationship;
                    break;
                }
            }
        }
        return relationshipToBeExcluded;
    }
}
