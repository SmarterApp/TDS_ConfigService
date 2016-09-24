package tds.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeLimitConfigurationUnitTests {
    @Test
    public void shouldBuildATimeLimitConfiguration() {
        TimeLimitConfiguration timeLimitConfiguration = new TimeLimitConfiguration.Builder()
                .withTaCheckinTimeMinutes(10)
                .withAssessmentId("Unit Test Assessment")
                .withClientName("SBAC_PT")
                .withEnvironment("unit test")
                .withExamDelayDays(1)
                .withExamRestartWindowMinutes(20)
                .withInterfaceTimeoutMinutes(10)
                .withRequestInterfaceTimeoutMinutes(15)
                .build();

        assertThat(timeLimitConfiguration.getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getAssessmentId()).isEqualTo("Unit Test Assessment");
        assertThat(timeLimitConfiguration.getClientName()).isEqualTo("SBAC_PT");
        assertThat(timeLimitConfiguration.getEnvironment()).isEqualTo("unit test");
        assertThat(timeLimitConfiguration.getExamDelayDays()).isEqualTo(1);
        assertThat(timeLimitConfiguration.getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(timeLimitConfiguration.getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
    }
}
