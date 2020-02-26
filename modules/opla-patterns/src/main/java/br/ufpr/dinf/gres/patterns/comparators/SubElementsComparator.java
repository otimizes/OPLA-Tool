package br.ufpr.dinf.gres.patterns.comparators;

import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.patterns.util.ElementUtil;

import java.util.Comparator;

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
