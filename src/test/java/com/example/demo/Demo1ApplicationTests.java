package com.example.demo;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.net.URI;
import okhttp3.OkHttpClient;
import org.eclipse.jetty.client.HttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.ConnectionOptions;
import org.mockserver.model.HttpResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class Demo1ApplicationTests {

  private static final ClientAndServer nettyMockServer = ClientAndServer.startClientAndServer(8080);
  public static final HttpStatus HTTP_STATUS = HttpStatus.ALREADY_REPORTED;

  @Test
  void emptyGZIPResponse_Scenario_RestTemplate_ApacheHttpClient_Chunked() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withChunkSize(100));
    TestRestTemplate testRestTemplate = new TestRestTemplate(
        new RestTemplateBuilder()
            .requestFactory(HttpComponentsClientHttpRequestFactory.class));

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HTTP_STATUS, response.getStatusCode());
  }

  @Test
  void emptyGZIPResponse_Scenario_RestTemplate_ApacheHttpClient_CloseSocket() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withCloseSocket(true));
    TestRestTemplate testRestTemplate = new TestRestTemplate(
        new RestTemplateBuilder()
            .requestFactory(HttpComponentsClientHttpRequestFactory.class));

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HTTP_STATUS, response.getStatusCode());
  }

  @Test
  void emptyGZIPResponse_Scenario_RestTemplate_OkHttp_Chunked() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withChunkSize(100));
    TestRestTemplate testRestTemplate = new TestRestTemplate(
        new RestTemplateBuilder()
            .requestFactory(OkHttp3ClientHttpRequestFactory.class));

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HTTP_STATUS, response.getStatusCode());
  }

  @Test
  void emptyGZIPResponse_Scenario_RestTemplate_ApacheOkHttp_CloseSocket() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withCloseSocket(true));
    TestRestTemplate testRestTemplate = new TestRestTemplate(
        new RestTemplateBuilder()
            .requestFactory(OkHttp3ClientHttpRequestFactory.class));

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HTTP_STATUS, response.getStatusCode());
  }

  @Test
  void emptyGZIPResponse_Scenario_RestTemplate_SimpleJDKRequest_Chunked() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withChunkSize(100));
    TestRestTemplate testRestTemplate = new TestRestTemplate(
        new RestTemplateBuilder()
            .requestFactory(SimpleClientHttpRequestFactory.class));

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HTTP_STATUS, response.getStatusCode());
  }

  @Test
  void emptyGZIPResponse_Scenario_RestTemplate_SimpleJDKRequest_CloseSocket() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withCloseSocket(true));
    TestRestTemplate testRestTemplate = new TestRestTemplate(
        new RestTemplateBuilder()
            .requestFactory(SimpleClientHttpRequestFactory.class));

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HTTP_STATUS, response.getStatusCode());
  }

  @Test
  void emptyGZIPResponse_Scenario_WebClient_JettyClient_CloseSocket() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withCloseSocket(true));

    // WHEN
    HttpClient httpClient = new HttpClient();
    ClientHttpConnector connector = new JettyClientHttpConnector(httpClient);

    String response = WebClient.builder().clientConnector(connector)
        .baseUrl("http://localhost:8080/").build()
        .post()
        .retrieve()
        .bodyToFlux(String.class)
        .blockFirst();

    // THEN
    Assertions.assertNull(response);
  }

  @Test
  void emptyGZIPResponse_Scenario_WebClient_JettyClient_Chunked() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withChunkSize(100));

    // WHEN
    HttpClient httpClient = new HttpClient();
    ClientHttpConnector connector = new JettyClientHttpConnector(httpClient);

    String response = WebClient.builder().clientConnector(connector)
        .baseUrl("http://localhost:8080/").build()
        .post()
        .retrieve()
        .bodyToFlux(String.class)
        .blockFirst();

    // THEN
    Assertions.assertNull(response);
  }

  @Test
  void emptyGZIPResponse_Scenario_WebClient_HttpComponents_CloseSocket() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withCloseSocket(true));

    // WHEN
    String response = WebClient.builder().clientConnector(new HttpComponentsClientHttpConnector())
        .baseUrl("http://localhost:8080/").build()
        .post()
        .retrieve()
        .bodyToFlux(String.class)
        .blockFirst();

    // THEN
    Assertions.assertNull(response);
  }

  @Test
  void emptyGZIPResponse_Scenario_WebClient_HttpComponents_Chunked() {
    // GIVEN
    mockServer(ConnectionOptions.connectionOptions()
        .withSuppressContentLengthHeader(true)
        .withChunkSize(100));

    // WHEN
    String response = WebClient.builder().clientConnector(new HttpComponentsClientHttpConnector())
        .baseUrl("http://localhost:8080/").build()
        .post()
        .retrieve()
        .bodyToFlux(String.class)
        .blockFirst();

    // THEN
    Assertions.assertNull(response);
  }

  private void mockServer(ConnectionOptions connectionOptions) {
    HttpResponse httpResponse = response()
        .withStatusCode(HTTP_STATUS.value())
        .withConnectionOptions(connectionOptions)
        .withHeader("Content-Encoding", "gzip")
        .withBody(new byte[0]);

    nettyMockServer
        .when(request().withPath("/"), Times.once())
        .respond(httpResponse);
  }

}
