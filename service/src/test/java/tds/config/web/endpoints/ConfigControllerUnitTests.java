package tds.config.web.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import tds.common.web.exceptions.NotFoundException;
import tds.config.ClientSystemFlag;
import tds.config.services.ConfigService;
import tds.config.web.resources.ClientSystemFlagResource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigControllerUnitTests {
    private ConfigController configController;
    private ConfigService mockConfigService;
    private final String LOCALHOST_HREF_ROOT = "http://localhost/config";

    @Before
    public void Setup() {
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

        ResponseEntity<ClientSystemFlagResource> response = configController.getClientSystemFlag(clientName, auditObject);

        verify(mockConfigService).findClientSystemFlag(clientName, auditObject);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getClientSystemFlag().getClientName()).isEqualTo(clientName);
        assertThat(response.getBody().getClientSystemFlag().getAuditObject()).isEqualTo(auditObject);
        assertThat(response.getBody().getClientSystemFlag().getDescription()).isEqualTo("mock client system flag");

        UriComponentsBuilder expectedUrl = UriComponentsBuilder.fromHttpUrl(
                String.format("%s/client-system-flags/%s/%s", LOCALHOST_HREF_ROOT, clientName, auditObject));
        assertThat(response.getBody().getId().getHref()).isEqualTo(expectedUrl.toUriString());
    }

    @Test(expected = NotFoundException.class)
    public void shouldGetNotFoundExceptionWhenForInvalidClientNameAndAuditObject() {
        String clientName = "UNIT_TEST";
        String auditObject = "unit_test";

        when(mockConfigService.findClientSystemFlag(clientName, auditObject))
                .thenReturn(Optional.empty());

        configController.getClientSystemFlag(clientName, auditObject);
    }
}
