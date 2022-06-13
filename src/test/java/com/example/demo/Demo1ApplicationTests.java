package com.example.demo;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.net.URI;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.ConnectionOptions;
import org.mockserver.model.HttpResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class Demo1ApplicationTests {

  private static final ClientAndServer nettyMockServer = ClientAndServer.startClientAndServer(8080);

  @Test
  void errorWhenEmptyGZIPResponseScenario1() {
    // GIVEN
    HttpResponse httpResponse = response()
        .withStatusCode(HttpStatus.FOUND.value())
        .withConnectionOptions(ConnectionOptions.connectionOptions()
            .withSuppressContentLengthHeader(true)
            .withChunkSize(100))
        .withHeader("Content-Encoding", "gzip")
        .withBody(new byte[0]);

    nettyMockServer
        .when(request().withPath("/"), Times.once())
        .respond(httpResponse);
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HttpStatus.FOUND, response.getStatusCode());
  }

  @Test
  void errorWhenEmptyGZIPResponseScenario2() {
    // GIVEN
    HttpResponse httpResponse = response()
        .withStatusCode(HttpStatus.FOUND.value())
        .withConnectionOptions(ConnectionOptions.connectionOptions()
            .withSuppressContentLengthHeader(true)
            .withCloseSocket(true))
        .withHeader("Content-Encoding", "gzip")
        .withBody(new byte[0]);

    nettyMockServer
        .when(request().withPath("/"), Times.once())
        .respond(httpResponse);
    TestRestTemplate testRestTemplate = new TestRestTemplate();

    // WHEN
    ResponseEntity<String> response = testRestTemplate.postForEntity(URI.create("http://localhost:8080/"), null, String.class);

    // THEN
    Assertions.assertEquals(HttpStatus.FOUND, response.getStatusCode());
  }

}
