package org.javaloong.kongmink.open.data.jpa.internal;

import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.User;
import org.osgi.service.component.annotations.Component;

import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component(service = UserRepository.class, immediate = true)
public class JpaUserRepository extends JpaRepositorySupport<User, String> implements UserRepository {

    @Override
    public Optional<User> findByUsername(String username) {
        return findByCriteria((cb, entityRoot) -> cb.equal(entityRoot.get("username"), username));
    }

    @Override
    public List<User> findAll() {
        return super.findAll(null, defaultCriteriaOrder());
    }

    private CriteriaOrderCollector<User> defaultCriteriaOrder() {
        return (cb, entityRoot) -> {
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(entityRoot.get("createdDate")));
            return orders.toArray(new Order[0]);
        };
    }
}
