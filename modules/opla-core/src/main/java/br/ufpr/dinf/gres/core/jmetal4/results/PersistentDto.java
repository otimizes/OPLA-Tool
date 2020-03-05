package br.ufpr.dinf.gres.core.jmetal4.results;

import java.io.Serializable;

public interface PersistentDto<T> {
    public T newPersistentInstance(PersistentDto persistentDto);
}
