package tds.config.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import tds.config.TimeLimitConfiguration;
import tds.config.repositories.TimeLimitConfigurationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeLimitConfigurationRepositoryImplIntegrationTests {

    @Autowired
    private TimeLimitConfigurationRepository timeLimitConfigurationRepository;

    @Test
    public void shouldGetTimeLimitsConfigurationForAClientName() {
        final String clientName = "SBAC_PT";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);

        assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo(clientName);
        assertThat(result.get().getAssessmentId()).isNull();
        assertThat(result.get().getEnvironment()).isEqualTo("Development");
        assertThat(result.get().getExamRestartWindowMinutes()).isEqualTo(10);
        assertThat(result.get().getExamDelayDays()).isEqualTo(-1);
        assertThat(result.get().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(result.get().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
        assertThat(result.get().getTaCheckinTimeMinutes()).isEqualTo(20);
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForAClientNameAndAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId);

        assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo(clientName);
        assertThat(result.get().getAssessmentId()).isNotNull();
        assertThat(result.get().getAssessmentId()).isEqualTo(assessmentId);
        assertThat(result.get().getEnvironment()).isEqualTo("Development");
        assertThat(result.get().getExamRestartWindowMinutes()).isEqualTo(10);
        assertThat(result.get().getExamDelayDays()).isEqualTo(-1);
        assertThat(result.get().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(result.get().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
        assertThat(result.get().getTaCheckinTimeMinutes()).isEqualTo(20);
    }

    @Test
    public void shouldReturnOptionalEmptyTimeLimitsConfigurationForInvalidClientName() {
        final String clientName = "foo";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);

        assertThat(result).isNotPresent();
    }

    @Test
    public void shouldReturnOptionalEmptyTimeLimitsConfigurationForAValidClientNameAndInvalidAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "foo";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId);

        assertThat(result).isNotPresent();
    }
}
