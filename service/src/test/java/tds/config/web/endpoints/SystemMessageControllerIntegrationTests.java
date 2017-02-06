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

import tds.common.web.advice.ExceptionAdvice;
import tds.config.services.SystemMessageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SystemMessageController.class)
@Import(ExceptionAdvice.class)
public class SystemMessageControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private SystemMessageService mockSystemMessageService;

    @Test
    public void shouldGetAClientSystemMessage() throws Exception {
        String clientName = "UNIT_TEST";
        String messageKey = "unit test";
        String languageCode = "Unit Language";
        String context = "Unit context";
        String subject = "Unit subject";
        String grade = "Unit grade";
        when(mockSystemMessageService.getSystemMessage(clientName, messageKey, languageCode, context, subject, grade))
            .thenReturn("Mocked message");

        MvcResult result = http.perform(get("/config/messages/" + clientName + "/" + context + "/" + messageKey + "/" + languageCode + "?subject=" + subject + "&grade=" + grade)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("Mocked message");
    }
}
