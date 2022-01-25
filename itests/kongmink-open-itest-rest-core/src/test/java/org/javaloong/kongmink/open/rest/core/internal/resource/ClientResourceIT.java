package org.javaloong.kongmink.open.rest.core.internal.resource;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.javaloong.kongmink.open.common.model.Page;
import org.javaloong.kongmink.open.common.client.Client;
import org.javaloong.kongmink.open.common.user.User;
import org.javaloong.kongmink.open.rest.core.model.ClientDto;
import org.javaloong.kongmink.open.service.ClientService;
import org.javaloong.kongmink.open.service.model.ComplexClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class ClientResourceIT extends AbstractResourceTestSupport {

    @BeforeClass
    public static void beforeClass() {
        BundleContext context = FrameworkUtil.getBundle(ClientResourceIT.class).getBundleContext();
        registerUserContextProvider(context);
        context.registerService(ClientService.class, Mockito.mock(ClientService.class), null);
    }

    @Inject
    ClientService clientService;

    @Test
    public void createClient_ClientNameInvalid_ShouldReturnValidationErrors() {
        assertThat(given().contentType(ContentType.JSON).body(new ClientDto())
                .post("/clients").then().assertThat()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .extract().body().jsonPath())
                .returns("name", path -> path.getString("errors[0].field"));
    }

    @Test
    public void createClient_ShouldAddClientAndReturnHttpStatusCreated() {
        ArgumentCaptor<Client> clientCapture = ArgumentCaptor.forClass(Client.class);
        when(clientService.create(any(User.class), clientCapture.capture())).thenReturn(new ComplexClient());
        ClientDto clientDto = new ClientDto();
        clientDto.setName("client1");
        given().contentType(ContentType.JSON).body(clientDto)
                .post("/clients").then().assertThat()
                .statusCode(Response.Status.CREATED.getStatusCode());
        assertThat(clientCapture.getValue()).isNotNull()
                .returns(clientDto.getName(), Client::getName);
    }

    @Test
    public void updateClient_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<Client> clientCapture = ArgumentCaptor.forClass(Client.class);
        doNothing().when(clientService).update(clientCapture.capture());
        ClientDto clientDto = new ClientDto();
        clientDto.setName("client2");
        given().contentType(ContentType.JSON).body(clientDto)
                .put("/clients/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(clientCapture.getValue()).isNotNull()
                .returns("1", Client::getId)
                .returns(clientDto.getName(), Client::getName);
    }

    @Test
    public void deleteClient_ShouldReturnHttpStatusOk() {
        ArgumentCaptor<String> idCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(clientService).delete(idCapture.capture());
        delete("/clients/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        assertThat(idCapture.getValue()).isEqualTo("1");
    }

    @Test
    public void getClient_ClientNotFound_ShouldReturnHttpStatusNotFound() {
        when(clientService.findById(anyString())).thenReturn(Optional.empty());
        get("/clients/{id}", "1").then().assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void getClient_ShouldReturnHttpStatusOk() {
        ComplexClient complexClient = createComplexClient("1", "client1");
        when(clientService.findById(anyString())).thenReturn(Optional.ofNullable(complexClient));
        ComplexClient result = get("/clients/{id}", "1").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(ComplexClient.class);
        assertThat(result).isNotNull()
                .extracting(ComplexClient::getId, ComplexClient::getName)
                .containsExactly(complexClient.getId(), complexClient.getName());
    }

    @Test
    public void getClients_ShouldReturnHttpStatusOk() {
        when(clientService.findAllByUser(any(User.class), anyInt(), anyInt())).thenReturn(createComplexClients());
        Page<ComplexClient> page = given().param("size", 2)
                .get("/clients").then().assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().as(new TypeRef<Page<ComplexClient>>() {
                });
        assertThat(page).isNotNull()
                .returns(3L, Page::getTotalCount)
                .extracting(Page::getData)
                .satisfies(data -> {
                    assertThat(data).hasSize(2)
                            .extracting(Client::getName)
                            .containsExactly("client1", "client2");
                });
    }

    private ComplexClient createComplexClient(String id, String name) {
        ComplexClient client = new ComplexClient();
        client.setId(id);
        client.setName(name);
        return client;
    }

    private Page<ComplexClient> createComplexClients() {
        List<ComplexClient> clients = new ArrayList<>();
        clients.add(createComplexClient("1", "client1"));
        clients.add(createComplexClient("2", "client2"));
        return new Page<>(clients, 3);
    }
}
