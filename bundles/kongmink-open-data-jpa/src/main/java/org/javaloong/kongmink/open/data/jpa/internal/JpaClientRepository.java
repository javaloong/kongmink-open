package org.javaloong.kongmink.open.data.jpa.internal;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.data.ClientRepository;
import org.javaloong.kongmink.open.data.domain.ClientEntity;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.osgi.service.component.annotations.Component;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component(service = ClientRepository.class)
public class JpaClientRepository extends JpaRepositorySupport<ClientEntity, String> implements ClientRepository {

    @Override
    public List<ClientEntity> findAllByUser(UserEntity user, int size) {
        return findAll(userCriteriaPredicate(user), defaultCriteriaOrder(), size);
    }

    @Override
    public Page<ClientEntity> findAllByUser(UserEntity user, int page, int size) {
        return findAll(userCriteriaPredicate(user), defaultCriteriaOrder(), page, size);
    }

    private CriteriaPredicateCollector<ClientEntity> userCriteriaPredicate(UserEntity user) {
        return (cb, entityRoot) -> {
            Predicate predicate = null;
            if (user != null) {
                predicate = cb.equal(entityRoot.get("user").get("id"), user.getId());
            }
            return predicate;
        };
    }

    private CriteriaOrderCollector<ClientEntity> defaultCriteriaOrder() {
        return (cb, entityRoot) -> {
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(entityRoot.get("createdDate")));
            return orders.toArray(new Order[0]);
        };
    }
}
