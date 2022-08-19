package org.javaloong.kongmink.open.data.jpa.internal;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import com.github.database.rider.junit5.util.EntityManagerProvider;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.persistence.EntityManager;

@ExtendWith(DBUnitExtension.class)
public abstract class RepositoryTestSupport {

    private static final String PERSISTENCE_UNIT_NAME = "km-open-test";

    private final ConnectionHolder connectionHolder = () ->
            EntityManagerProvider.instance(PERSISTENCE_UNIT_NAME).connection();

    protected EntityManager em() {
        return EntityManagerProvider.em();
    }
}
