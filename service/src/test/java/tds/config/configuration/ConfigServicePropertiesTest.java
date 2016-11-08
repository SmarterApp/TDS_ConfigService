package tds.config.configuration;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigServicePropertiesTest {
    @Test
    public void shouldSetAssessmentUrl() {
        ConfigServiceProperties prop = new ConfigServiceProperties();
        assertThat(prop.getAssessmentUrl()).isEmpty();
        prop.setAssessmentUrl("assessments/");
        assertThat(prop.getAssessmentUrl()).isEqualTo("assessments/");
    }

    @Test (expected = IllegalArgumentException.class)
    public void shouldNotAllowNullAssessmentUrl() {
        ConfigServiceProperties prop = new ConfigServiceProperties();
        prop.setAssessmentUrl(null);
    }
}
