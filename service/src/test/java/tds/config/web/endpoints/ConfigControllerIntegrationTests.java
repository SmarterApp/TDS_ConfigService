package tds.config.web.endpoints;

import org.joda.time.Days;
import org.joda.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Optional;

import tds.config.AssessmentWindow;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.AssessmentWindowParameters;
import tds.config.services.ConfigService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.isA;
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
            .withEnabled(true)
            .withIsPracticeTest(true)
            .build();

        when(mockConfigService.findClientSystemFlag(clientName, auditObject)).thenReturn(Optional.of(flag));

        http.perform(get("/config/client-system-flags/" + clientName + "/" + auditObject)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is(clientName)))
            .andExpect(jsonPath("auditObject", is(auditObject)))
            .andExpect(jsonPath("enabled", is(true)))
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

    /*
    Assessment Windows Endpoint Tests
     */
    @Test
    public void shouldReturnAssessmentWindows() throws Exception {
        Instant startTime = Instant.now();
        Instant endTime = Instant.now().plus(Days.days(20).toStandardDuration());
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("windowId")
            .withAssessmentKey("SLA 11")
            .withMode("mode")
            .withWindowMaxAttempts(3)
            .withWindowSessionType(0)
            .withStartTime(startTime)
            .withEndTime(endTime)
            .withFormKey("formKey")
            .withModeMaxAttempts(5)
            .withModeSessionType(-1)
            .build();


        when(mockConfigService.findAssessmentWindows(isA(AssessmentWindowParameters.class))).
            thenReturn(Collections.singletonList(window));

        String url = "/config/assessment-windows/SBAC_PT/math11/session-type/0/student/23";

        http.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", is(1)))
            .andExpect(jsonPath("[0].windowId", is("windowId")))
            .andExpect(jsonPath("[0].assessmentKey", is("SLA 11")))
            .andExpect(jsonPath("[0].windowMaxAttempts", is(3)))
            .andExpect(jsonPath("[0].windowSessionType", is(0)))
            .andExpect(jsonPath("[0].formKey", is("formKey")))
            .andExpect(jsonPath("[0].modeMaxAttempts", is(5)))
            .andExpect(jsonPath("[0].modeSessionType", is(-1)));
    }
}
