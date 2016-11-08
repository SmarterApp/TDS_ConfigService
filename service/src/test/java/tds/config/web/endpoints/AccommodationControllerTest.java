package tds.config.web.endpoints;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import tds.config.Accommodation;
import tds.config.services.AccommodationsService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccommodationControllerTest {
    @Mock
    private AccommodationsService accommodationsService;

    private AccommodationController controller;

    @Before
    public void setUp() {
        controller = new AccommodationController(accommodationsService);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldReturnAccommodations() {
        Accommodation accommodation = new Accommodation.Builder().build();
        when(accommodationsService.findAccommodations("key")).thenReturn(Collections.singletonList(accommodation));

        ResponseEntity<List<Accommodation>> response = controller.findAccommodations("key");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsOnly(accommodation);
    }
}
