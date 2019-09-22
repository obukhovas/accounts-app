package ru.ao.app.endpoint;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ao.app.Application;
import ru.ao.app.endpoint.config.AppConfig;
import ru.ao.app.endpoint.dto.*;
import ru.ao.app.util.DatabaseUtil;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static javax.ws.rs.core.Response.Status;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.ao.app.endpoint.JsonUtil.fromInputStream;

public class AccountEndpointTest {

    private static HttpServer server;

    private static HttpClient httpClient;
    private static URIBuilder baseUri;

    @BeforeClass
    public static void setUp() throws Exception {
        server = GrizzlyHttpServerFactory
                .createHttpServer(Application.BASE_URI, new AppConfig(), false);
        httpClient = HttpClients.createDefault();
        baseUri = new URIBuilder(Application.BASE_URI);
        DatabaseUtil.initializeTestData();
        server.start();
    }

    @AfterClass
    public static void tearsDown() {
        server.shutdownNow();
    }

    @Test
    public void healthCheckTest() throws Exception {
        HttpGet httpGet = httpGet("/acc-app/ping");

        HttpResponse actual = httpClient.execute(httpGet);

        assertEquals(Status.OK.getStatusCode(), actual.getStatusLine().getStatusCode());
    }

    @Test
    public void getAllTest() throws Exception {
        HttpGet httpGet = httpGet("/acc-app/accounts/all");

        HttpResponse response = httpClient.execute(httpGet);
        assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

        AccountsRsDTO actual = fromInputStream(response.getEntity().getContent(), AccountsRsDTO.class);
        assertEquals(8, actual.getAccounts().size());
    }

    @Test
    public void getByIdTest() throws Exception {
        HttpGet httpGet = httpGet("/acc-app/accounts/5");

        HttpResponse response = httpClient.execute(httpGet);
        assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

        AccountRsDTO actual = fromInputStream(response.getEntity().getContent(), AccountRsDTO.class);
        assertEquals("5", actual.getName());
        assertEquals(BigDecimal.valueOf(350000), actual.getBalance());

        /* not exist */
        httpGet = httpGet("/acc-app/accounts/999999");
        response = httpClient.execute(httpGet);
        assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatusLine().getStatusCode());
    }

    @Test
    public void createTest() throws Exception {
        CreateAccountRqDTO request = new CreateAccountRqDTO();
        request.setName("new");
        request.setBalance(BigDecimal.TEN);
        HttpPut httpPut = httpPut("/acc-app/accounts/create", request);

        HttpResponse response = httpClient.execute(httpPut);
        assertEquals(Status.CREATED.getStatusCode(), response.getStatusLine().getStatusCode());

        AccountRsDTO actual = fromInputStream(response.getEntity().getContent(), AccountRsDTO.class);
        assertNotNull(actual.getId());
        assertEquals("new", actual.getName());
        assertEquals(BigDecimal.TEN, actual.getBalance());
    }

    @Test
    public void deleteTest() throws Exception {
        HttpDelete httpDelete = httpDelete("/acc-app/accounts/2");

        HttpResponse response = httpClient.execute(httpDelete);
        assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

        AccountRsDTO actual = fromInputStream(response.getEntity().getContent(), AccountRsDTO.class);
        assertEquals("2", actual.getName());
        assertEquals(BigDecimal.valueOf(9000), actual.getBalance());

        /* not exist */
        httpDelete = httpDelete("/acc-app/accounts/9999");
        response = httpClient.execute(httpDelete);
        assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusLine().getStatusCode());

        ErrorResponse actualError = fromInputStream(response.getEntity().getContent(), ErrorResponse.class);
        assertEquals(3, actualError.getErrorCode());
    }

    @Test
    public void withdrawTest() throws Exception {

        HttpPost httpPost = httpPost("/acc-app/accounts/4/withdraw/400");

        HttpResponse response = httpClient.execute(httpPost);
        assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

        AccountRsDTO actual = fromInputStream(response.getEntity().getContent(), AccountRsDTO.class);
        assertEquals(BigDecimal.valueOf(28600) /* 29000 - 400 */, actual.getBalance());

        /* not exist */
        httpPost = httpPost("/acc-app/accounts/9999/withdraw/400");
        response = httpClient.execute(httpPost);
        assertEquals(Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusLine().getStatusCode());

        ErrorResponse actualError = fromInputStream(response.getEntity().getContent(), ErrorResponse.class);
        assertEquals(3, actualError.getErrorCode());
    }

    @Test
    public void transferTest() throws Exception {
        TransferMoneyRqDTO request = new TransferMoneyRqDTO();
        request.setFromAccountId(7L);
        request.setToAccountId(8L);
        request.setAmount(BigDecimal.valueOf(400));
        HttpPost httpPost = httpPost("/acc-app/accounts/transfer", request);

        HttpResponse response = httpClient.execute(httpPost);
        assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

        HttpGet httpGet = httpGet("/acc-app/accounts/7");
        response = httpClient.execute(httpGet);
        AccountRsDTO sourceAccount = fromInputStream(response.getEntity().getContent(), AccountRsDTO.class);

        httpGet = httpGet("/acc-app/accounts/8");
        response = httpClient.execute(httpGet);
        AccountRsDTO targetAccount = fromInputStream(response.getEntity().getContent(), AccountRsDTO.class);


        assertEquals(BigDecimal.valueOf(41000)/* 41400 - 400 */, sourceAccount.getBalance());
        assertEquals(BigDecimal.valueOf(22000)/* 21600 + 400 */, targetAccount.getBalance());
    }

    private static HttpGet httpGet(String path) throws URISyntaxException {
        URI uri = baseUri.setPath(path).build();
        return new HttpGet(uri);
    }

    private static HttpPut httpPut(String path, Object rq) throws URISyntaxException, UnsupportedEncodingException {
        URI uri = baseUri.setPath(path).build();
        HttpPut httpPut = new HttpPut(uri);
        httpPut.setEntity(new StringEntity(JsonUtil.toString(rq)));
        httpPut.setHeader("Content-type", MediaType.APPLICATION_JSON);
        return httpPut;
    }

    private static HttpDelete httpDelete(String path) throws URISyntaxException {
        URI uri = baseUri.setPath(path).build();
        return new HttpDelete(uri);
    }

    private static HttpPost httpPost(String path) throws URISyntaxException {
        URI uri = baseUri.setPath(path).build();
        return new HttpPost(uri);
    }

    private static HttpPost httpPost(String path, Object rq) throws URISyntaxException, UnsupportedEncodingException {
        URI uri = baseUri.setPath(path).build();
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(JsonUtil.toString(rq)));
        httpPost.setHeader("Content-type", MediaType.APPLICATION_JSON);
        return httpPost;
    }
}