package tds.config.web.endpoints;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tds.config.ConfigServiceApplication;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:8080")
public class TimeLimitsConfigurationControllerIntegrationTests {
    private static final String TIME_LIMITS_RESOURCE = "/config/time-limits/";

    @Test
    public void shouldGetTimeLimitsConfigurationForClientNameAndAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(TIME_LIMITS_RESOURCE + clientName + "/" + assessmentId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("timeLimitConfiguration.clientName", equalTo(clientName))
            .body("timeLimitConfiguration.environment", equalTo("Development"))
            .body("timeLimitConfiguration.assessmentId", equalTo(assessmentId))
            .body("timeLimitConfiguration.examRestartWindowMinutes", equalTo(10))
            .body("timeLimitConfiguration.examDelayDays", equalTo(-1))
            .body("timeLimitConfiguration.interfaceTimeoutMinutes", equalTo(10))
            .body("timeLimitConfiguration.requestInterfaceTimeoutMinutes", equalTo(15))
            .body("timeLimitConfiguration.taCheckinTimeMinutes", equalTo(20))
            .body("_links.self.href", equalTo("http://localhost:8080/config/time-limits/SBAC_PT/SBAC%20Math%203-MATH-3"));
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForClientNameAndNonExistentAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "foo";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(TIME_LIMITS_RESOURCE + clientName + "/" + assessmentId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("timeLimitConfiguration.clientName", equalTo(clientName))
            .body("timeLimitConfiguration.environment", equalTo("Development"))
            .body("timeLimitConfiguration.assessmentId", is(nullValue()))
            .body("timeLimitConfiguration.examRestartWindowMinutes", equalTo(10))
            .body("timeLimitConfiguration.examDelayDays", equalTo(-1))
            .body("timeLimitConfiguration.interfaceTimeoutMinutes", equalTo(10))
            .body("timeLimitConfiguration.requestInterfaceTimeoutMinutes", equalTo(15))
            .body("timeLimitConfiguration.taCheckinTimeMinutes", equalTo(20))
            .body("_links.self.href", equalTo("http://localhost:8080/config/time-limits/SBAC_PT"));
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForClientName() {
        final String clientName = "SBAC_PT";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(TIME_LIMITS_RESOURCE + clientName)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("timeLimitConfiguration.clientName", equalTo(clientName))
            .body("timeLimitConfiguration.environment", equalTo("Development"))
            .body("timeLimitConfiguration.assessmentId", is(nullValue()))
            .body("timeLimitConfiguration.examRestartWindowMinutes", equalTo(10))
            .body("timeLimitConfiguration.examDelayDays", equalTo(-1))
            .body("timeLimitConfiguration.interfaceTimeoutMinutes", equalTo(10))
            .body("timeLimitConfiguration.requestInterfaceTimeoutMinutes", equalTo(15))
            .body("timeLimitConfiguration.taCheckinTimeMinutes", equalTo(20))
            .body("_links.self.href", equalTo("http://localhost:8080/config/time-limits/SBAC_PT"));
    }

    @Test
    public void shouldGet404WhenGettingTimeLimitsConfigurationWithInvalidClientNameAndAssessmentId() {
        final String clientName = "foo";
        final String assessmentId = "bar";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(TIME_LIMITS_RESOURCE + clientName + "/" + assessmentId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(404);
    }
}
