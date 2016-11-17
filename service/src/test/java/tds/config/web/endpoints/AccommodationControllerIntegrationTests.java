package tds.config.web.endpoints;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import tds.common.web.advice.ExceptionAdvice;
import tds.config.Accommodation;
import tds.config.services.AccommodationsService;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccommodationController.class)
@Import(ExceptionAdvice.class)
public class AccommodationControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private AccommodationsService mockAccommodationsService;

    @Test
    public void shouldGetAClientTestProperty() throws Exception {
        Accommodation accommodation = new Accommodation.Builder()
            .withDefaultAccommodation(true)
            .withAccommodationCode("code")
            .withAccommodationType("type")
            .withAccommodationValue("value")
            .withAllowChange(true)
            .withDependsOnToolType("depends")
            .withAllowCombine(false)
            .withDisableOnGuestSession(true)
            .withEntryControl(false)
            .withFunctional(true)
            .withSegmentPosition(99)
            .withToolMode("toolMode")
            .withToolTypeSortOrder(25)
            .withToolValueSortOrder(50)
            .withVisible(true)
            .build();

        when(mockAccommodationsService.findAccommodations("key")).thenReturn(singletonList(accommodation));

        String requestUri = UriComponentsBuilder.fromUriString("/config/accommodations/key")
            .build()
            .toUriString();

        http.perform(get(requestUri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("[0].defaultAccommodation", is(true)))
            .andExpect(jsonPath("[0].accommodationCode", is("code")))
            .andExpect(jsonPath("[0].accommodationValue", is("value")))
            .andExpect(jsonPath("[0].allowChange", is(true)))
            .andExpect(jsonPath("[0].dependsOnToolType", is("depends")))
            .andExpect(jsonPath("[0].allowCombine", is(false)))
            .andExpect(jsonPath("[0].disableOnGuestSession", is(true)))
            .andExpect(jsonPath("[0].entryControl", is(false)))
            .andExpect(jsonPath("[0].functional", is(true)))
            .andExpect(jsonPath("[0].segmentPosition", is(99)))
            .andExpect(jsonPath("[0].toolMode", is("toolMode")))
            .andExpect(jsonPath("[0].toolTypeSortOrder", is(25)))
            .andExpect(jsonPath("[0].toolValueSortOrder", is(50)))
            .andExpect(jsonPath("[0].visible", is(true)))
            .andExpect(jsonPath("[0].accommodationType", is("type")));
    }
}
