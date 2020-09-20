package dev.luke10x.fis.offer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HTTPIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void successJourney() throws Exception {
        var url = String.format("http://localhost:%d/offers", port);
        MultiValueMap<String, String> headers = new HttpHeaders();
        ((HttpHeaders) headers).setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request =
        new HttpEntity<String>(
         """
        {
            "description": "Hi!",
            "amountInMinorUnits": "100",
            "currency": "USD",
            "durationInSeconds": "300"
        }
        """,
         headers
        );

        var postResponse = this.restTemplate.postForEntity(url, request, String.class);
        assertThat(postResponse.getBody()).contains("Created: ");
        var offerId = postResponse.getBody().replace("Created: ", "");
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        var getResponse = this.restTemplate.getForEntity(url + "/" + offerId, String.class);
        assertThat(getResponse.getBody()).contains("Hi!");

        var deleteResponse = this.restTemplate.exchange(url + "/" + offerId, HttpMethod.DELETE, null, String.class);
        assertThat(deleteResponse.getBody()).contains("Deleted");
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
    }
}
