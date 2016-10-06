package tds.config.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "config-service")
public class ConfigServiceProperties {
    private String studentUrl = "";

    public String getStudentUrl() {
        return studentUrl.endsWith("/")
            ? studentUrl
            : studentUrl + "/";
    }

    public void setStudentUrl(String studentUrl) {
        if(studentUrl == null) throw new IllegalArgumentException("studentUrl cannot be null");

        this.studentUrl = studentUrl;
    }
}
