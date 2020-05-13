package br.ufpr.dinf.gres.persistence.base;

import br.ufpr.dinf.gres.domain.OPLAThreadScope;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Base to all created services
 *
 * @param <T> Type of entity
 */
@Service
@Scope("prototype")
public abstract class BaseService<T> {
    protected final JpaRepository<T, String> repository;

    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> domain;

    public BaseService(JpaRepository<T, String> repository) {
        this.repository = repository;
        this.domain = this.clazz();
    }

    public T save(T var1) {
        return repository.save(var1);
    }

    public List<T> saveAll(Iterable<T> var1) {
        return repository.saveAll(var1);
    }

    public Optional<T> findById(String var1) {
        return repository.findById(var1);
    }

    public T getOne(String var1) {
        return repository.getOne(var1);
    }

    public boolean existsById(String var1) {
        return repository.existsById(var1);
    }

    public List<T> findAll() {
        String where = "";
        if (doesObjectContainField(domain, "hash")) {
            where = " obj where obj.hash like '" + OPLAThreadScope.token.get() + "/%'";
        } else if (doesObjectContainField(domain, "experiment")) {
            where = " obj where obj.experiment.hash like '" + OPLAThreadScope.token.get() + "/%'";
        }
        String select = "from " + domain.getName();
        Query query = entityManager.createQuery(select + where);
        return query.getResultList();
    }

    public boolean doesObjectContainField(Class object, String fieldName) {
        return Arrays.stream(object.getDeclaredFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }

    public List<T> findAllById(Iterable<String> var1) {
        return repository.findAllById(var1);
    }

    public long count() {
        return repository.count();
    }

    public void deleteById(String var1) {
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

    public List<T> findByExperiment(String experiment) {
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
