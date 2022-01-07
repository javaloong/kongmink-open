package org.javaloong.kongmink.open.data.jpa.internal;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.data.CrudRepository;
import org.javaloong.kongmink.open.data.domain.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CrudRepositoryJpa<T extends AbstractEntity<ID>, ID extends Serializable>
        implements CrudRepository<T, ID> {

    private final Class<T> entityClass;

    @PersistenceContext(unitName = "kmOpen")
    protected EntityManager em;

    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @SuppressWarnings("unchecked")
    public CrudRepositoryJpa() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public <S extends T> S create(S entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public <S extends T> S update(S entity) {
        S saved = em.merge(entity);
        em.flush();
        return saved;
    }

    @Override
    public void delete(ID id) {
        T entity = em.find(entityClass, id);
        em.remove(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    @Override
    public List<T> findAll() {
        return findAll(null, null);
    }

    public List<T> findAll(CriteriaPredicateCollector<T> predicateCollector,
                               CriteriaOrderCollector<T> orderCollector) {
        return findAll(predicateCollector, orderCollector, null);
    }

    public List<T> findAll(CriteriaPredicateCollector<T> predicateCollector,
                               CriteriaOrderCollector<T> orderCollector, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> entityRoot = cq.from(entityClass);
        if (predicateCollector != null) {
            Predicate restrictions = predicateCollector.collect(cb, entityRoot);
            if (restrictions != null) cq.where(restrictions);
        }
        if (orderCollector != null) {
            Order[] orders = orderCollector.collect(cb, entityRoot);
            if (orders != null && orders.length > 0) cq.orderBy(orders);
        }
        TypedQuery<T> q = em.createQuery(cq);
        if (size != null && size > 0) q.setMaxResults(size);
        return q.getResultList();
    }

    public Page<T> findAll(CriteriaPredicateCollector<T> predicateCollector, Integer page, Integer size) {
        CriteriaOrderCollector<T> orderCollector = (cb, entityRoot) -> {
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(entityRoot.get(AbstractEntity.ID_FIELD_KEY)));
            return orders.toArray(new Order[0]);
        };
        return findAll(predicateCollector, orderCollector, page, size);
    }

    public Page<T> findAll(CriteriaPredicateCollector<T> predicateCollector,
                           CriteriaOrderCollector<T> orderCollector, Integer page, Integer size) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> entityRoot = cq.from(entityClass);
        entityRoot.alias("entityAlias");
        Predicate restrictions = null;
        if (predicateCollector != null) {
            restrictions = predicateCollector.collect(cb, entityRoot);
        }
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countEntityRoot = countQuery.from(cq.getResultType());
        countEntityRoot.alias("entityAlias");
        countQuery.select(cb.count(countEntityRoot));
        if (restrictions != null) {
            countQuery.where(restrictions);
            cq.where(restrictions);
        }
        long totalItems = em.createQuery(countQuery).getSingleResult();
        if (orderCollector != null) {
            Order[] orders = orderCollector.collect(cb, entityRoot);
            if (orders != null && orders.length > 0) cq.orderBy(orders);
        }
        List<T> items = em.createQuery(cq)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
        return new Page<T>(items, totalItems);
    }

    public Optional<T> findByCriteria(CriteriaPredicateCollector<T> predicateCollector) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> entityRoot = cq.from(entityClass);
        if (predicateCollector != null) {
            cq.where(predicateCollector.collect(cb, entityRoot));
        }
        try {
            return Optional.ofNullable(em.createQuery(cq).getSingleResult());
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public interface CriteriaPredicateCollector<T> {
        Predicate collect(CriteriaBuilder cb, Root<T> entityRoot);
    }

    public interface CriteriaOrderCollector<T> {
        Order[] collect(CriteriaBuilder cb, Root<T> entityRoot);
    }
}
