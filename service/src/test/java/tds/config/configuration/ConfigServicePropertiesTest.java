package tds.config.configuration;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigServicePropertiesTest {
    @Test (expected = IllegalArgumentException.class)
    public void shouldThrowWhenStudentUrlIsNull() {
        ConfigServiceProperties properties = new ConfigServiceProperties();
        properties.setStudentUrl(null);
    }

    @Test
    public void shouldEnsureStudentUrlAlwaysEndsWithSlash() {
        ConfigServiceProperties properties = new ConfigServiceProperties();
        assertThat(properties.getStudentUrl()).isEqualTo("/");

        properties.setStudentUrl("http://localhost/student");
        assertThat(properties.getStudentUrl()).isEqualTo("http://localhost/student/");

        properties.setStudentUrl("http://localhost/bogus/");
        assertThat(properties.getStudentUrl()).isEqualTo("http://localhost/bogus/");

    }
}
