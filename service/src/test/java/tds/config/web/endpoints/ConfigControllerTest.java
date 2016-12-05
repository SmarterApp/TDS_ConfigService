package tds.config.web.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tds.common.web.exceptions.NotFoundException;
import tds.config.AssessmentWindow;
import tds.config.ClientSystemFlag;
import tds.config.model.AssessmentWindowParameters;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
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
    public void shouldFindListOfAssessmentWindows() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withAssessmentKey("assessment")
            .withWindowId("windowId")
            .build();

        when(mockConfigService.findAssessmentWindows(isA(AssessmentWindowParameters.class))).thenReturn(Collections.singletonList(window));

        ResponseEntity<List<AssessmentWindow>> response = configController.findAssessmentWindows("SBAC_PT",
            "assessment",
            0,
            1,
            25,
            50,
            75,
            100,
            "wid:formKey");

        ArgumentCaptor<AssessmentWindowParameters> assessmentWindowParametersArgumentCaptor = ArgumentCaptor.forClass(AssessmentWindowParameters.class);
        verify(mockConfigService).findAssessmentWindows(assessmentWindowParametersArgumentCaptor.capture());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(window);

        AssessmentWindowParameters parameters = assessmentWindowParametersArgumentCaptor.getValue();
        assertThat(parameters.getAssessmentId()).isEqualTo("assessment");
        assertThat(parameters.getClientName()).isEqualTo("SBAC_PT");
        assertThat(parameters.getSessionType()).isEqualTo(0);
        assertThat(parameters.getStudentId()).isEqualTo(1);
        assertThat(parameters.getShiftWindowStart()).isEqualTo(25);
        assertThat(parameters.getShiftWindowEnd()).isEqualTo(50);
        assertThat(parameters.getShiftFormStart()).isEqualTo(75);
        assertThat(parameters.getShiftFormEnd()).isEqualTo(100);
        assertThat(parameters.getFormList()).isEqualTo("wid:formKey");
    }

    @Test
    public void shouldFindListOfAssessmentWindowsWithNullArguments() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .build();

        when(mockConfigService.findAssessmentWindows(isA(AssessmentWindowParameters.class))).thenReturn(Collections.singletonList(window));

        ResponseEntity<List<AssessmentWindow>> response = configController.findAssessmentWindows("SBAC",
            "assessment",
            0,
            1,
            null,
            null,
            null,
            null,
            null);

        ArgumentCaptor<AssessmentWindowParameters> assessmentWindowParametersArgumentCaptor = ArgumentCaptor.forClass(AssessmentWindowParameters.class);
        verify(mockConfigService).findAssessmentWindows(assessmentWindowParametersArgumentCaptor.capture());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(window);

        AssessmentWindowParameters parameters = assessmentWindowParametersArgumentCaptor.getValue();
        assertThat(parameters.getShiftWindowStart()).isEqualTo(0);
        assertThat(parameters.getShiftWindowEnd()).isEqualTo(0);
        assertThat(parameters.getShiftFormStart()).isEqualTo(0);
        assertThat(parameters.getShiftFormEnd()).isEqualTo(0);
        assertThat(parameters.getFormList()).isNull();
    }
}
