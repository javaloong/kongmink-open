package org.javaloong.kongmink.open.data.jpa.internal;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.model.user.query.UserQuery;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.osgi.service.component.annotations.Component;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Component(service = UserRepository.class)
public class JpaUserRepository extends JpaRepositorySupport<UserEntity, String> implements UserRepository {

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return findByCriteria((cb, entityRoot) -> cb.equal(entityRoot.get("username"), username));
    }

    @Override
    public List<UserEntity> findAll() {
        return super.findAll(null, defaultCriteriaOrder());
    }

    @Override
    public Page<UserEntity> findAll(UserQuery query, int page, int size) {
        return findAll(userCriteriaPredicate(query), defaultCriteriaOrder(), page, size);
    }

    private CriteriaPredicateCollector<UserEntity> userCriteriaPredicate(UserQuery query) {
        return (cb, entityRoot) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query != null) {
                if (query.getFrom() > 0 && query.getTo() > 0) {
                    predicates.add(cb.between(entityRoot.get("createdAt"),
                            getLocalDateTime(query.getFrom()), getLocalDateTime(query.getTo())));
                }
                if (isNotEmpty(query.getField()) && isNotEmpty(query.getQuery())) {
                    if (query.getField().equals("username")) {
                        predicates.add(cb.like(entityRoot.get(query.getField()), query.getQuery() + "%"));
                    } else {
                        predicates.add(cb.equal(entityRoot.get(query.getField()), query.getQuery()));
                    }
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private CriteriaOrderCollector<UserEntity> defaultCriteriaOrder() {
        return (cb, entityRoot) -> {
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(entityRoot.get("createdAt")));
            return orders.toArray(new Order[0]);
        };
    }

    private LocalDateTime getLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(TimeZone.getDefault().toZoneId())
                .toLocalDateTime();
    }

    private boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}
