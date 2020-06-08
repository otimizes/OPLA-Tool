package br.otimizes.oplatool.patterns.comparators;

import java.util.Comparator;

import br.otimizes.oplatool.patterns.util.ElementUtil;
import br.otimizes.oplatool.architecture.representation.Element;

/**
 * The Class SubElementsComparator.
 */
public abstract class SubElementsComparator {

    /** The Constant ASCENDING. */
    private static final Comparator<Element> ASCENDING = new SubElementsComparatorAscending();
    
    /** The Constant DESCENDING. */
    private static final Comparator<Element> DESCENDING = new SubElementsComparatorDescending();

    /**
     * Instantiates a new sub elements comparator.
     */
    private SubElementsComparator() {
    }

    /**
     * Gets the ascending orderer.
     *
     * @return the ascending orderer
     */
    public static Comparator<Element> getAscendingOrderer() {
        return ASCENDING;
    }

    /**
     * Gets the descending orderer.
     *
     * @return the descending orderer
     */
    public static Comparator<Element> getDescendingOrderer() {
        return DESCENDING;
    }

    /**
     * The Class SubElementsComparatorAscending.
     */
    private static class SubElementsComparatorAscending implements Comparator<Element> {

        /**
         * Compare two elements.
         *
         * @param o1 First element to compare
         * @param o2 Second element to compare
         * @return The difference 
         */
        @Override
        public int compare(Element o1, Element o2) {
            return ElementUtil.getAllSubElements(o1).size() - ElementUtil.getAllSubElements(o2).size();
        }
    }

    /**
     * The Class SubElementsComparatorDescending.
     */
    private static class SubElementsComparatorDescending implements Comparator<Element> {

        /**
         * Compare.
         *
         * @param o1 the first element to compare
         * @param o2 the second element to compare
         * @return the difference of both elements number of subElements
         */
        @Override
        public int compare(Element o1, Element o2) {
            return ElementUtil.getAllSubElements(o2).size() - ElementUtil.getAllSubElements(o1).size();
        }
    }

}
