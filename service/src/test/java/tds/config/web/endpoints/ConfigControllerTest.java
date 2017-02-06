package tds.config.web.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import tds.common.web.exceptions.NotFoundException;
import tds.config.ClientSystemFlag;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigControllerTest {
    private ConfigController configController;
    private ConfigService mockConfigService;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        mockConfigService = mock(ConfigService.class);
        configController = new ConfigController(mockConfigService);
    }

    @Test
    public void shouldGetClientSystemFlag() {
        String clientName = "UNIT_TEST";
        String auditObject = "unit test";
        ClientSystemFlag mockClientSystemFlag = new ClientSystemFlag.Builder()
            .withClientName(clientName)
            .withAuditObject(auditObject)
            .withDescription("mock client system flag")
            .build();
        when(mockConfigService.findClientSystemFlag(clientName, auditObject))
            .thenReturn(Optional.of(mockClientSystemFlag));

        ResponseEntity<ClientSystemFlag> response = configController.getClientSystemFlag(clientName, auditObject);

        verify(mockConfigService).findClientSystemFlag(clientName, auditObject);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getClientName()).isEqualTo(clientName);
        assertThat(response.getBody().getAuditObject()).isEqualTo(auditObject);
        assertThat(response.getBody().getDescription()).isEqualTo("mock client system flag");
    }

    @Test(expected = NotFoundException.class)
    public void shouldGetNotFoundExceptionWhenForInvalidClientNameAndAuditObject() {
        String clientName = "UNIT_TEST";
        String auditObject = "unit_test";

        when(mockConfigService.findClientSystemFlag(clientName, auditObject))
            .thenReturn(Optional.empty());

        configController.getClientSystemFlag(clientName, auditObject);
    }

    @Test
    public void shouldGetClientSystemMessage() {
        String clientName = "UNIT_TEST";
        String messageKey = "unit test";
        String languageCode = "Unit Language";
        String context = "Unit context";
        String subject = "Unit subject";
        String grade = "Unit grade";
        when(mockConfigService.getSystemMessage(clientName, messageKey, languageCode, context, subject, grade))
            .thenReturn("Mocked message");

        ResponseEntity<String> response = configController.getClientSystemMessage(clientName, languageCode, context, messageKey, subject, grade);

        verify(mockConfigService).getSystemMessage(clientName, messageKey, languageCode, context, subject, grade);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Mocked message");
    }
}
