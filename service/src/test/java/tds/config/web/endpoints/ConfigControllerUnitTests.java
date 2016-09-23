package tds.config.web.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import tds.common.web.exceptions.NotFoundException;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.services.ConfigService;
import tds.config.web.resources.ClientSystemFlagResource;
import tds.config.web.resources.ClientTestPropertyResource;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    @Test
    public void shouldGetAClientTestProperty() {
        String clientName = "UNIT_TEST";
        String assessmentId = "SBAC Math 3-MATH-3";
        ClientTestProperty mockClientTestProperty = new ClientTestProperty.Builder()
                .withClientName(clientName)
                .withAssessmentId(assessmentId)
                .withMaxOpportunities(3)
                .withPrefetch(2)
                .withIsSelectable(true)
                .withLabel("Grades 3 - 5 MATH")
                .build();
        when(mockConfigService.findClientTestProperty(clientName, assessmentId))
                .thenReturn(Optional.of(mockClientTestProperty));

        ResponseEntity<ClientTestPropertyResource> response = configController.getClientTestProperty(clientName, assessmentId);

        verify(mockConfigService).findClientTestProperty(clientName, assessmentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getClientTestProperty().getClientName()).isEqualTo(clientName);
        assertThat(response.getBody().getClientTestProperty().getAssessmentId()).isEqualTo(assessmentId);
        assertThat(response.getBody().getClientTestProperty().getMaxOpportunities()).isEqualTo(3);
        assertThat(response.getBody().getClientTestProperty().getPrefetch()).isEqualTo(2);
        assertThat(response.getBody().getClientTestProperty().getIsSelectable()).isTrue();
        assertThat(response.getBody().getClientTestProperty().getLabel()).isEqualTo("Grades 3 - 5 MATH");

        UriComponentsBuilder expectedUrl = UriComponentsBuilder.fromHttpUrl(
                String.format("%s/client-test-properties/%s/%s", LOCALHOST_HREF_ROOT, clientName, assessmentId));
        assertThat(response.getBody().getId().getHref()).isEqualTo(expectedUrl.toUriString());
    }

    @Test(expected = NotFoundException.class)
    public void shouldGetNotFoundExceptionWhenGettingClientTestPropertyForInvalidClientName() {
        String clientName = "foo";
        String assessmentId = "SBAC Math 3-MATH-3";

        when(mockConfigService.findClientTestProperty(clientName, assessmentId))
                .thenReturn(Optional.empty());

        configController.getClientTestProperty(clientName, assessmentId);
    }
}
