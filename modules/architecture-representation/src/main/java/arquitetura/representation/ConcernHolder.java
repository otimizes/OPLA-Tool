package arquitetura.representation;

import arquitetura.exceptions.ConcernNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public enum ConcernHolder {

    INSTANCE;

    private final HashMap<String, Concern> concerns = new HashMap<String, Concern>();

    /**
     * Esta lista é carregada a partir do arquivo de concerns indica no arquivo de configuração.<br/>
     * <p>
     * Ela serve para sabermos quais concern são passiveis de manipulação.<Br />
     * <p>
     * Ex: Ao adicionar um  concern em uma classe, o mesmo deve estar presente nesta lista.
     */
    private List<Concern> allowedConcerns = new ArrayList<Concern>();

    /**
     * Procura concern por nome.
     * <p>
     * Se o concern não estiver no arquivo de profile é lançada uma Exception.
     *
     * @param name
     * @return
     * @throws ConcernNotFoundException
     */
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
