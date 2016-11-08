package tds.config.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config-service")
public class ConfigServiceProperties {
    private String assessmentUrl = "";

    public String getAssessmentUrl() {
        return assessmentUrl;
    }

    public void setAssessmentUrl(String assessmentUrl) {
        if (assessmentUrl == null) throw new IllegalArgumentException("assessmentUrl cannot be null");
        this.assessmentUrl = assessmentUrl;
    }
}
