package org.javaloong.kongmink.open.data.jpa.internal;

import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.data.ClientRepository;
import org.javaloong.kongmink.open.data.domain.Client;
import org.javaloong.kongmink.open.data.domain.User;
import org.osgi.service.component.annotations.Component;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component(service = ClientRepository.class, immediate = true)
public class JpaClientRepository extends CrudRepositoryJpa<Client, String> implements ClientRepository {

    @Override
    public List<Client> findAllByUser(User user) {
        return findAll(userCriteriaPredicate(user), defaultCriteriaOrder());
    }

    @Override
    public Page<Client> findAllByUser(User user, int page, int size) {
        return findAll(userCriteriaPredicate(user), defaultCriteriaOrder(), page, size);
    }

    private CriteriaPredicateCollector<Client> userCriteriaPredicate(User user) {
        return (cb, entityRoot) -> {
            Predicate predicate = null;
            if(user != null) {
                predicate = cb.equal(entityRoot.get("user"), user);
            }
            return predicate;
        };
    }

    private CriteriaOrderCollector<Client> defaultCriteriaOrder() {
        return (cb, entityRoot) -> {
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(entityRoot.get("createdDate")));
            return orders.toArray(new Order[0]);
        };
    }
}
