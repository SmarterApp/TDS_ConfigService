package tds.config.web.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;
import tds.common.web.exceptions.NotFoundException;
import tds.config.TimeLimitConfiguration;
import tds.config.services.TimeLimitConfigurationService;
import tds.config.web.resources.TimeLimitConfigurationResource;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeLimitsConfigurationControllerUnitTests {
    private TimeLimitConfigurationController timeLimitConfigurationController;
    private TimeLimitConfigurationService mockTimeLimitConfigurationService;
    private final String LOCALHOST_HREF_ROOT = "http://localhost/config/timelimits";

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

        ResponseEntity<TimeLimitConfigurationResource> response = timeLimitConfigurationController.getTimeLimitConfiguration(clientName, assessmentId);

        verify(mockTimeLimitConfigurationService).findTimeLimitConfiguration(clientName, assessmentId);

        assertThat(response.getBody().getTimeLimitConfiguration().getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(response.getBody().getTimeLimitConfiguration().getAssessmentId()).isEqualTo("Unit Test Assessment");
        assertThat(response.getBody().getTimeLimitConfiguration().getClientName()).isEqualTo("UNIT_TEST");
        assertThat(response.getBody().getTimeLimitConfiguration().getEnvironment()).isEqualTo("unit test");
        assertThat(response.getBody().getTimeLimitConfiguration().getExamDelayDays()).isEqualTo(1);
        assertThat(response.getBody().getTimeLimitConfiguration().getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(response.getBody().getTimeLimitConfiguration().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(response.getBody().getTimeLimitConfiguration().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);

        UriComponentsBuilder expectedUrl = UriComponentsBuilder.fromHttpUrl(
                String.format("%s/%s/%s", LOCALHOST_HREF_ROOT, clientName, assessmentId));
        assertThat(response.getBody().getId().getHref()).isEqualTo(expectedUrl.toUriString());
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

        ResponseEntity<TimeLimitConfigurationResource> response = timeLimitConfigurationController.getTimeLimitConfiguration(clientName, assessmentId);

        verify(mockTimeLimitConfigurationService).findTimeLimitConfiguration(clientName, assessmentId);

        assertThat(response.getBody().getTimeLimitConfiguration().getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(response.getBody().getTimeLimitConfiguration().getAssessmentId()).isNull();
        assertThat(response.getBody().getTimeLimitConfiguration().getClientName()).isEqualTo("UNIT_TEST");
        assertThat(response.getBody().getTimeLimitConfiguration().getEnvironment()).isEqualTo("unit test");
        assertThat(response.getBody().getTimeLimitConfiguration().getExamDelayDays()).isEqualTo(1);
        assertThat(response.getBody().getTimeLimitConfiguration().getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(response.getBody().getTimeLimitConfiguration().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(response.getBody().getTimeLimitConfiguration().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);

        UriComponentsBuilder expectedUrl = UriComponentsBuilder.fromHttpUrl(
                String.format("%s/%s", LOCALHOST_HREF_ROOT, clientName));
        assertThat(response.getBody().getId().getHref()).isEqualTo(expectedUrl.toUriString());
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

        ResponseEntity<TimeLimitConfigurationResource> response = timeLimitConfigurationController.getTimeLimitConfiguration(clientName);

        verify(mockTimeLimitConfigurationService).findTimeLimitConfiguration(clientName);

        assertThat(response.getBody().getTimeLimitConfiguration().getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(response.getBody().getTimeLimitConfiguration().getAssessmentId()).isNull();
        assertThat(response.getBody().getTimeLimitConfiguration().getClientName()).isEqualTo("UNIT_TEST");
        assertThat(response.getBody().getTimeLimitConfiguration().getEnvironment()).isEqualTo("unit test");
        assertThat(response.getBody().getTimeLimitConfiguration().getExamDelayDays()).isEqualTo(1);
        assertThat(response.getBody().getTimeLimitConfiguration().getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(response.getBody().getTimeLimitConfiguration().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(response.getBody().getTimeLimitConfiguration().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);

        UriComponentsBuilder expectedUrl = UriComponentsBuilder.fromHttpUrl(
                String.format("%s/%s", LOCALHOST_HREF_ROOT, clientName));
        assertThat(response.getBody().getId().getHref()).isEqualTo(expectedUrl.toUriString());
    }

    @Test(expected = NotFoundException.class)
    public void shouldGetANotFoundExceptionWhenGettingTimeLimitConfigurationForInvalidClientNameAndAssessmentId() {
        String clientName = "UNIT_TEST";
        String assessmentId = "foo";

        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId)).thenReturn(Optional.empty());

        timeLimitConfigurationController.getTimeLimitConfiguration(clientName, assessmentId);
    }
}