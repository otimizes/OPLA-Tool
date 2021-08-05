package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Concern holder enum
 */
public enum ConcernHolder {

    INSTANCE;

    private final HashMap<String, Concern> concerns = new HashMap<>();

    private final List<Concern> allowedConcerns = new ArrayList<>();

    public synchronized Concern getOrCreateConcern(String name) throws ConcernNotFoundException {
        Concern concern = allowedConcernContains(name.toLowerCase());
        if (concerns.containsValue(concern)) return concern;
        if (concern != null) {
            concerns.put(name.toLowerCase(), concern);
            return concern;
        }
        throw new ConcernNotFoundException("Concern " + name + " cannot be found");
    }

    public synchronized HashMap<String, Concern> getConcerns() {
        return concerns;
    }

    private synchronized Concern allowedConcernContains(String concernName) {
        for (Concern concern : allowedConcerns) {
            if (concern.getName().equalsIgnoreCase(concernName))
                return concern;
        }
        return null;
    }

    public synchronized Concern getConcernByName(String concernName) {
        return concerns.get(concernName);
    }


    public synchronized List<Concern> allowedConcerns() {
        return allowedConcerns;
    }

    public void clear() {
        concerns.clear();
    }
}
