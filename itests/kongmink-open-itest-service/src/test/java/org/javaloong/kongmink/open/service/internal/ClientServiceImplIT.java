package org.javaloong.kongmink.open.service.internal;

import org.javaloong.kongmink.open.am.client.ClientProvider;
import org.javaloong.kongmink.open.common.model.client.Client;
import org.javaloong.kongmink.open.data.UserRepository;
import org.javaloong.kongmink.open.data.domain.UserEntity;
import org.javaloong.kongmink.open.service.ClientService;
import org.javaloong.kongmink.open.service.model.ComplexClient;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.transaction.control.TransactionControl;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ClientServiceImplIT extends AbstractServiceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(UserServiceImplIT.class).getBundleContext();
        context.registerService(ClientProvider.class, Mockito.mock(ClientProvider.class), null);
    }

    @Inject
    TransactionControl transactionControl;
    @Inject
    UserRepository userRepository;
    @Inject
    ClientProvider clientProvider;
    @Inject
    ClientService clientService;

    @Before
    public void setUp() {
        transactionControl.required(() -> userRepository.create(createUser("1", "user1")));
    }

    @After
    public void tearDown() {
        transactionControl.required(() -> {
            userRepository.delete("1");
            return null;
        });
    }

    @Test
    public void testCRUD() {
        Client client = TestUtils.createClient("1", "client1");
        when(clientProvider.create(any(Client.class))).thenReturn(client);
        ComplexClient complexClient = createComplexClient("1", "client1", "1");
        clientService.create(complexClient);
        Collection<ComplexClient> clients = clientService.findAllByUser("1", 10);
        assertThat(clients).hasSize(1);
        doNothing().when(clientProvider).update(any(Client.class));
        complexClient.setName("client11");
        clientService.update(complexClient);
        verify(clientProvider).update(complexClient);
        doNothing().when(clientProvider).delete(anyString());
        clientService.delete("1");
        when(clientProvider.findById(anyString())).thenReturn(Optional.of(client));
        assertThat(clientService.findById("1")).isEmpty();
    }

    private ComplexClient createComplexClient(String id, String name, String userId) {
        ComplexClient client = new ComplexClient();
        client.setId(id);
        client.setName(name);
        client.setUserId(userId);
        return client;
    }

    private UserEntity createUser(String id, String name) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setUsername(name);
        user.setCreatedDate(LocalDateTime.now());
        return user;
    }
}
