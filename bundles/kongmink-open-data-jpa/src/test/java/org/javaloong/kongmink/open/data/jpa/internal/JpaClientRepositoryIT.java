package org.javaloong.kongmink.open.data.jpa.internal;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.CompareOperation;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.data.ClientRepository;
import org.javaloong.kongmink.open.data.domain.ClientEntity;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBUnit(mergeDataSets = true)
@DataSet(value = {"userData.xml", "clientData.xml"}, cleanAfter = true)
public class JpaClientRepositoryIT extends RepositoryTestSupport {

    private JpaClientRepository clientRepository;

    @Test
    @DataSet(transactional = true)
    @ExpectedDataSet(value = "createClientDataExpected.xml", compareOperation = CompareOperation.CONTAINS)
    public void createClient() {
        ClientEntity client = new ClientEntity();
        client.setId("6");
        client.setName("client6");
        client.setCreatedDate(LocalDateTime.parse("2021-12-30T10:28:30"));
        client.setUser(getUser("1"));
        ClientEntity result = getClientRepository().create(client);
        assertThat(result).isNotNull();
    }

    @Test
    @DataSet(transactional = true)
    @ExpectedDataSet(value = "updateClientDataExpected.xml", compareOperation = CompareOperation.CONTAINS)
    public void updateClient() {
        getClientRepository().findById("2").ifPresent(client -> {
            client.setName("client22");
            ClientEntity result = getClientRepository().update(client);
            assertThat(result).returns("client22", ClientEntity::getName);
        });
    }

    @Test
    @DataSet(transactional = true)
    public void deleteClient() {
        getClientRepository().delete("3");
        assertThat(getClientRepository().findById("3")).isEmpty();
    }

    @Test
    public void findClientById() {
        Optional<ClientEntity> result = getClientRepository().findById("1");
        assertThat(result).isPresent().hasValueSatisfying(client -> {
            assertThat(client.getName()).isEqualTo("client1");
        });
    }

    @Test
    public void findAllClientsByUser() {
        UserEntity user = getUser("1");
        List<ClientEntity> clients = getClientRepository().findAllByUser(user, 10);
        assertThat(clients).isNotEmpty().hasSize(4)
                .extracting(ClientEntity::getName)
                .containsExactly("client5", "client4", "client2", "client1");
    }

    @Test
    public void findAllClientsByUserByPagination() {
        UserEntity user = getUser("1");
        Page<ClientEntity> result = getClientRepository().findAllByUser(user, 1, 2);
        assertThat(result.getTotalCount()).isEqualTo(4);
        assertThat(result.getData()).isNotEmpty().hasSize(2)
                .extracting(ClientEntity::getName).containsExactly("client5", "client4");
    }

    private UserEntity getUser(String id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        return user;
    }

    private synchronized ClientRepository getClientRepository() {
        if(clientRepository == null) {
            clientRepository = new JpaClientRepository();
            clientRepository.setEntityManager(em());
        }
        return clientRepository;
    }
}
