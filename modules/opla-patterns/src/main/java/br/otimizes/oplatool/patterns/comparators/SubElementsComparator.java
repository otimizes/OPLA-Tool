package br.otimizes.oplatool.patterns.comparators;

import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.patterns.util.ElementUtil;

import java.util.Comparator;

/**
 * The Class SubElementsComparator.
 */
public abstract class SubElementsComparator {

    private static final Comparator<Element> ASCENDING = new SubElementsComparatorAscending();

    private static final Comparator<Element> DESCENDING = new SubElementsComparatorDescending();

    private SubElementsComparator() {
    }

    public static Comparator<Element> getAscendingOrderer() {
        return ASCENDING;
    }

    public static Comparator<Element> getDescendingOrderer() {
        return DESCENDING;
    }

    private static class SubElementsComparatorAscending implements Comparator<Element> {
        @Override
        public int compare(Element o1, Element o2) {
            return ElementUtil.getAllSubElements(o1).size() - ElementUtil.getAllSubElements(o2).size();
        }
    }

    private static class SubElementsComparatorDescending implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            return ElementUtil.getAllSubElements(o2).size() - ElementUtil.getAllSubElements(o1).size();
        }
    }
}
