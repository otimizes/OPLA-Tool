package br.otimizes.oplatool.patterns.designpatterns;

import java.util.logging.Level;
import java.util.logging.Logger;

import br.otimizes.oplatool.architecture.representation.Class;
import org.apache.commons.collections4.CollectionUtils;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Variant;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.repositories.ArchitectureRepository;
import br.otimizes.oplatool.patterns.util.AdapterUtil;
import br.otimizes.oplatool.patterns.util.ElementUtil;
import br.otimizes.oplatool.patterns.util.RelationshipUtil;

/**
 * The Class Adapter.
 */
public class Adapter extends DesignPattern {

    /** The instance. */
    private static volatile Adapter INSTANCE;

    /**
     * Instantiates a new adapter.
     */
    private Adapter() {
        super("Adapter", "Structural");
    }

    /**
     * Gets the single instance of Adapter.
     *
     * @return single instance of Adapter
     */
    public static synchronized Adapter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Adapter();
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
        return false;
    }

    /**
     * Verify PSPLA.
     *
     * @param scope the scope
     * @return true, if PSPLA
     */
    @Override
    public boolean verifyPSPLA(Scope scope) {
        return false;
    }

    /**
     * Apply.
     *
     * @param scope the scope
     * @return true, if apply
     */
    @Override
    public boolean apply(Scope scope) {
        return false;
    }

    /**
     * Apply adapter.
     *
     * @param target the target
     * @param adaptee the adaptee
     * @return the br.otimizes.oplatool.architecture.representation.Class
     */
    public Class applyAdapter(Element target, Element adaptee) {
        Class adapterClass = null;
        if (target != null
                && adaptee != null
                && (target instanceof Class || target instanceof Interface)
                && (adaptee instanceof Class || adaptee instanceof Interface)) {
            try {

                adapterClass = AdapterUtil.getAdapterClass(target, adaptee);
                if (adapterClass == null) {
                    adapterClass = AdapterUtil.createAdapterClass(adaptee);
                }
                //Implements/Extends and add all methods.
                if (target instanceof Class) {
                    ElementUtil.extendClass(adapterClass, (Class) target);
                } else {
                    ElementUtil.implementInterface(adapterClass, (Interface) target);
                }

                RelationshipUtil.createNewUsageRelationship("adaptee", adapterClass, adaptee);

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

                if (relationshipToBeExcluded != null) {
                    ArchitectureRepository.getCurrentArchitecture().removeRelationship(relationshipToBeExcluded);
                }

                //Copy concerns
                for (Concern concern : CollectionUtils.union(target.getOwnConcerns(), adaptee.getOwnConcerns())) {
                    try {
                        adapterClass.addConcern(concern.getName());
                    } catch (ConcernNotFoundException ex) {
                        Logger.getLogger(Adapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Move variants
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

}
