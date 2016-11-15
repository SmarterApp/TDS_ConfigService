package tds.config.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthControllerTest {
    private HealthController controller;

    @Before
    public void setUp() {
        controller = new HealthController();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldReturnOkForIsAliveEndpoint() {
        ResponseEntity<String> response = controller.isAlive();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Config Service Alive");
    }
}
