package br.ufpr.dinf.gres.core.persistence;

public interface IPersistentDto<T> {
    public T newPersistentInstance(IPersistentDto persistentDto);
}
