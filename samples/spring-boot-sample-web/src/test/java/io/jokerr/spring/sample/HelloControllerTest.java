package io.jokerr.spring.sample;

import io.jokerr.spring.security.EnableDevelopmentSecurityValidator;
import io.jokerr.spring.security.providers.CustomAuthenticationProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
            "io.jokerr.custom.enable-dev-validator=true"
        })
@EnableDevelopmentSecurityValidator
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)   //prevent context collisions between tests
public class HelloControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void hello() {
        ResponseEntity<String> entity = get("/hello", headersFor("123", "Name","ROLE"), String.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

    public <T>ResponseEntity<T> get(String uri, HttpHeaders headers, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), responseType);
    }

    public HttpHeaders headersFor(String id, String name, String...roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CustomAuthenticationProvider.HEADER_ID, id);
        headers.add(CustomAuthenticationProvider.HEADER_NAME, name);
        headers.addAll(CustomAuthenticationProvider.HEADER_ROLES, Arrays.asList(roles));
        return headers;
    }
}
