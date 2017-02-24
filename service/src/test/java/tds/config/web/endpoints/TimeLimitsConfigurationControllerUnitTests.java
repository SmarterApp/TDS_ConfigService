package tds.config.web.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import tds.common.web.exceptions.NotFoundException;
import tds.config.TimeLimitConfiguration;
import tds.config.services.TimeLimitConfigurationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeLimitsConfigurationControllerUnitTests {
    private TimeLimitConfigurationController timeLimitConfigurationController;
    private TimeLimitConfigurationService mockTimeLimitConfigurationService;

    @Before
    public void Setup() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        mockTimeLimitConfigurationService = mock(TimeLimitConfigurationService.class);
        timeLimitConfigurationController = new TimeLimitConfigurationController(mockTimeLimitConfigurationService);
    }

    @Test
    public void shouldGetATimeLimitConfigurationForClientNameAndAssessmentId() {
        String clientName = "UNIT_TEST";
        String assessmentId = "Unit Test Assessment";
        TimeLimitConfiguration mockTimeLimitConfiguration = new TimeLimitConfiguration.Builder()
                .withTaCheckinTimeMinutes(10)
                .withAssessmentId("Unit Test Assessment")
                .withClientName("UNIT_TEST")
                .withEnvironment("unit test")
                .withExamDelayDays(1)
                .withExamRestartWindowMinutes(20)
                .withInterfaceTimeoutMinutes(10)
                .withRequestInterfaceTimeoutMinutes(15)
                .build();
        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId))
                .thenReturn(Optional.of(mockTimeLimitConfiguration));

        ResponseEntity<TimeLimitConfiguration> response = timeLimitConfigurationController.getTimeLimitConfiguration(clientName, assessmentId);

        verify(mockTimeLimitConfigurationService).findTimeLimitConfiguration(clientName, assessmentId);

        TimeLimitConfiguration timeLimitConfiguration = response.getBody();
        
        assertThat(timeLimitConfiguration.getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getAssessmentId()).isEqualTo("Unit Test Assessment");
        assertThat(timeLimitConfiguration.getClientName()).isEqualTo("UNIT_TEST");
        assertThat(timeLimitConfiguration.getEnvironment()).isEqualTo("unit test");
        assertThat(timeLimitConfiguration.getExamDelayDays()).isEqualTo(1);
        assertThat(timeLimitConfiguration.getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(timeLimitConfiguration.getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
    }

    @Test
    public void shouldGetATimeLimitConfigurationForClientNameAndAssessmentIdThatDoesNotExist() {
        String clientName = "UNIT_TEST";
        String assessmentId = "foo";
        TimeLimitConfiguration mockTimeLimitConfiguration = new TimeLimitConfiguration.Builder()
                .withTaCheckinTimeMinutes(10)
                .withAssessmentId(null)
                .withClientName("UNIT_TEST")
                .withEnvironment("unit test")
                .withExamDelayDays(1)
                .withExamRestartWindowMinutes(20)
                .withInterfaceTimeoutMinutes(10)
                .withRequestInterfaceTimeoutMinutes(15)
                .build();
        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId))
                .thenReturn(Optional.of(mockTimeLimitConfiguration));

        ResponseEntity<TimeLimitConfiguration> response = timeLimitConfigurationController.getTimeLimitConfiguration(clientName, assessmentId);

        verify(mockTimeLimitConfigurationService).findTimeLimitConfiguration(clientName, assessmentId);

        TimeLimitConfiguration timeLimitConfiguration = response.getBody();

        assertThat(timeLimitConfiguration.getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getAssessmentId()).isNull();
        assertThat(timeLimitConfiguration.getClientName()).isEqualTo("UNIT_TEST");
        assertThat(timeLimitConfiguration.getEnvironment()).isEqualTo("unit test");
        assertThat(timeLimitConfiguration.getExamDelayDays()).isEqualTo(1);
        assertThat(timeLimitConfiguration.getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(timeLimitConfiguration.getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
    }

    @Test
    public void shouldGetATimeLimitsConfigurationForAClientName() {
        String clientName = "UNIT_TEST";
        TimeLimitConfiguration mockTimeLimitConfiguration = new TimeLimitConfiguration.Builder()
                .withTaCheckinTimeMinutes(10)
                .withAssessmentId(null)
                .withClientName("UNIT_TEST")
                .withEnvironment("unit test")
                .withExamDelayDays(1)
                .withExamRestartWindowMinutes(20)
                .withInterfaceTimeoutMinutes(10)
                .withRequestInterfaceTimeoutMinutes(15)
                .build();
        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName))
                .thenReturn(Optional.of(mockTimeLimitConfiguration));

        ResponseEntity<TimeLimitConfiguration> response = timeLimitConfigurationController.getTimeLimitConfiguration(clientName);

        verify(mockTimeLimitConfigurationService).findTimeLimitConfiguration(clientName);

        TimeLimitConfiguration timeLimitConfiguration = response.getBody();

        assertThat(timeLimitConfiguration.getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getAssessmentId()).isNull();
        assertThat(timeLimitConfiguration.getClientName()).isEqualTo("UNIT_TEST");
        assertThat(timeLimitConfiguration.getEnvironment()).isEqualTo("unit test");
        assertThat(timeLimitConfiguration.getExamDelayDays()).isEqualTo(1);
        assertThat(timeLimitConfiguration.getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(timeLimitConfiguration.getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
    }

    @Test(expected = NotFoundException.class)
    public void shouldGetANotFoundExceptionWhenGettingTimeLimitConfigurationForInvalidClientNameAndAssessmentId() {
        String clientName = "UNIT_TEST";
        String assessmentId = "foo";

        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId)).thenReturn(Optional.empty());

        timeLimitConfigurationController.getTimeLimitConfiguration(clientName, assessmentId);
    }
}