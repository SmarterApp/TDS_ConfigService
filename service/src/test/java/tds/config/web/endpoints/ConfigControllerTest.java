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
import tds.config.ClientTestProperty;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigControllerTest {
    private ConfigController configController;
    private ConfigService mockConfigService;

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

        ResponseEntity<ClientTestProperty> response = configController.getClientTestProperty(clientName, assessmentId);

        verify(mockConfigService).findClientTestProperty(clientName, assessmentId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getClientName()).isEqualTo(clientName);
        assertThat(response.getBody().getAssessmentId()).isEqualTo(assessmentId);
        assertThat(response.getBody().getMaxOpportunities()).isEqualTo(3);
        assertThat(response.getBody().getPrefetch()).isEqualTo(2);
        assertThat(response.getBody().getIsSelectable()).isTrue();
        assertThat(response.getBody().getLabel()).isEqualTo("Grades 3 - 5 MATH");
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