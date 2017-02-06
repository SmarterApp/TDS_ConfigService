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
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import tds.common.web.advice.ExceptionAdvice;
import tds.config.ClientSystemFlag;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigController.class)
@Import(ExceptionAdvice.class)
public class ConfigControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private ConfigService mockConfigService;

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
    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemMessage Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemMessage() throws Exception {
        String clientName = "UNIT_TEST";
        String messageKey = "unit test";
        String languageCode = "Unit Language";
        String context = "Unit context";
        String subject = "Unit subject";
        String grade = "Unit grade";
        when(mockConfigService.getSystemMessage(clientName, messageKey, languageCode, context, subject, grade))
            .thenReturn("Mocked message");

        MvcResult result = http.perform(get("/config/" + clientName + "/messages/" + context + "/" + messageKey + "/" + languageCode + "?subject=" + subject + "&grade=" + grade)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Mocked message");
    }
}
