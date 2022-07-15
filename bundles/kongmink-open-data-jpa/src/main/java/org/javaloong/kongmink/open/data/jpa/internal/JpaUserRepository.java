package org.javaloong.kongmink.open.data.jpa.internal;

import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.osgi.service.component.annotations.Component;

import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private CriteriaOrderCollector<UserEntity> defaultCriteriaOrder() {
        return (cb, entityRoot) -> {
            List<Order> orders = new ArrayList<>();
            orders.add(cb.desc(entityRoot.get("createdAt")));
            return orders.toArray(new Order[0]);
        };
    }
}
