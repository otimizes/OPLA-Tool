package br.ufpr.dinf.gres.core.persistence;

public interface IPersistentDto<S> {
    public S newPersistentInstance();
}
