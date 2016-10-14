package tds.config.web.endpoints;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.services.ConfigService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigController.class)
public class ConfigControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private ConfigService mockConfigService;

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemProperty Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientTestProperty() throws Exception {
        final String clientName = "SBAC_PT";
        final String testId = "SBAC Math 3-MATH-3";
        ClientTestProperty prop = new ClientTestProperty.Builder()
            .withClientName(clientName)
            .withAssessmentId(testId)
            .withMaxOpportunities(3)
            .build();

        when(mockConfigService.findClientTestProperty(clientName, testId)).thenReturn(Optional.of(prop));

        String requestUri = UriComponentsBuilder.fromUriString("/config/client-test-properties/" + clientName + "/" + testId)
            .build()
            .toUriString();

        http.perform(get(requestUri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is(clientName)))
            .andExpect(jsonPath("assessmentId", is(testId)))
            .andExpect(jsonPath("maxOpportunities", is(3)));
    }

    @Test
    public void shouldReturn404WhenGettingClientTestPropertyWithInvalidClientName() throws Exception{
        final String clientName = "foo";
        final String testId = "SBAC Math 3-MATH-3";

        when(mockConfigService.findClientTestProperty(clientName, testId)).thenReturn(Optional.empty());

        String requestUri = UriComponentsBuilder.fromUriString("/config/client-test-properties/" + clientName + "/" + testId)
            .build()
            .toUriString();

        http.perform(get(requestUri)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlag() throws Exception {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        ClientSystemFlag flag = new ClientSystemFlag.Builder()
            .withAuditObject(auditObject)
            .withClientName(clientName)
            .withIsOn(true)
            .withIsPracticeTest(true)
            .build();

        when(mockConfigService.findClientSystemFlag(clientName, auditObject)).thenReturn(Optional.of(flag));

        http.perform(get("/config/client-system-flags/" + clientName + "/" + auditObject)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is(clientName)))
            .andExpect(jsonPath("auditObject", is(auditObject)))
            .andExpect(jsonPath("isOn", is(true)))
            .andExpect(jsonPath("isPracticeTest", is(true)));
    }

    @Test
    public void shouldReturn404WhenGettingClientSystemFlagWithAnInvalidAuditObject() throws Exception {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        when(mockConfigService.findClientSystemFlag(clientName, auditObject)).thenReturn(Optional.empty());

        http.perform(get("/config/client-system-flags/" + clientName + "/" + auditObject)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
