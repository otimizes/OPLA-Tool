package br.ufpr.dinf.gres.persistence.service;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@Scope("prototype")
public abstract class BaseService<T> {
    protected final JpaRepository<T, Long> repository;

    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> domain;

    public BaseService(JpaRepository<T, Long> repository) {
        this.repository = repository;
        this.domain = this.clazz();
    }

    public T save(T var1) {
        return repository.save(var1);
    }

    public List<T> saveAll(Iterable<T> var1) {
        return repository.saveAll(var1);
    }

    public Optional<T> findById(Long var1) {
        return repository.findById(var1);
    }

    public T getOne(Long var1) {
        return repository.getOne(var1);
    }

    public boolean existsById(Long var1) {
        return repository.existsById(var1);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public List<T> findAllById(Iterable<Long> var1) {
        return repository.findAllById(var1);
    }

    public long count() {
        return repository.count();
    }

    public void deleteById(Long var1) {
        repository.deleteById(var1);
    }

    public void delete(T var1) {
        repository.delete(var1);
    }

    public void deleteAll(Iterable<T> var1) {
        repository.deleteAll(var1);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<T> findByExperiment(Long experiment) {
        Query query = entityManager.createQuery("from " + domain.getName() + " obj where obj.experiment.id = :experiment");
        query.setParameter("experiment", experiment);
        return query.getResultList();
    }

    private JpaEntityInformation<T, ?> getEntityInformation() {
        return JpaEntityInformationSupport.getEntityInformation(clazz(), entityManager);
    }

    public static Class<?> inferGenericType(Class<?> clazz) {
        return inferGenericType(clazz, 0);
    }

    public static Class<?> inferGenericType(Class<?> clazz, int index) {
        Type superClass = clazz.getGenericSuperclass();
        return (Class<?>) ((ParameterizedType) superClass).getActualTypeArguments()[index];
    }

    @SuppressWarnings("unchecked")
    private Class<T> clazz() {
        return (Class<T>) inferGenericType(getClass());
    }

}
