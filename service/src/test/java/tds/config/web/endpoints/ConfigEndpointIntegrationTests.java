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
public class ConfigEndpointIntegrationTests {
    private static final String CONFIG_RESOURCE = "/config";

    @Test
    public void shouldRespondToIsAlive() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get("/isAlive")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemProperty Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientTestProperty() {
        final String clientName = "SBAC_PT";
        final String testId = "SBAC Math 3-MATH-3";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/clientTestProperties/" + clientName + "/" + testId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("clientTestProperty.clientName", equalTo(clientName))
            .body("clientTestProperty.assessmentId", equalTo(testId))
            .body("clientTestProperty.maxOpportunities", equalTo(3))
            .body("_links.self.href", equalTo("http://localhost:8080/config/clientTestProperties/SBAC_PT/SBAC%20Math%203-MATH-3"));
    }

    @Test
    public void shouldReturn404WhenGettingClientTestPropertyWithInvalidClientName() {
        final String clientName = "foo";
        final String testId = "SBAC Math 3-MATH-3";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/clientTestProperties/" + clientName + "/" + testId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(404);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlag() {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/clientSystemFlags/" + clientName + "/" + auditObject)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("clientSystemFlag.clientName", equalTo(clientName))
            .body("clientSystemFlag.auditObject", equalTo(auditObject))
            .body("clientSystemFlag.isPracticeTest", equalTo(true))
            .body("clientSystemFlag.isOn", equalTo(true))
            .body("_links.self.href", equalTo("http://localhost:8080/config/clientSystemFlags/SBAC_PT/accommodations"));
    }

    @Test
    public void shouldReturn404WhenGettingClientSystemFlagWithAnInvalidAuditObject() {
        final String clientName = "SBAC_PT";
        final String auditObject = "foo";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/clientSystemFlags/" + clientName + "/" + auditObject)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(404);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // TimeLimits Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetTimeLimitsForClientNameAndAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/timelimits/" + clientName + "/" + assessmentId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("timeLimits.clientName", equalTo(clientName))
            .body("timeLimits.environment", equalTo("Development"))
            .body("timeLimits.assessmentId", equalTo(assessmentId))
            .body("timeLimits.examRestartWindowMinutes", equalTo(10))
            .body("timeLimits.examDelayDays", equalTo(-1))
            .body("timeLimits.interfaceTimeoutMinutes", equalTo(10))
            .body("timeLimits.requestInterfaceTimeoutMinutes", equalTo(15))
            .body("timeLimits.taCheckinTimeMinutes", equalTo(20));
    }

    @Test
    public void shouldGetTimeLimitsForClientNameAndNonExistentAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "foo";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/timelimits/" + clientName + "/" + assessmentId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("timeLimits.clientName", equalTo(clientName))
            .body("timeLimits.clientName", equalTo(clientName))
            .body("timeLimits.environment", equalTo("Development"))
            .body("timeLimits.assessmentId", is(nullValue()))
            .body("timeLimits.examRestartWindowMinutes", equalTo(10))
            .body("timeLimits.examDelayDays", equalTo(-1))
            .body("timeLimits.interfaceTimeoutMinutes", equalTo(10))
            .body("timeLimits.requestInterfaceTimeoutMinutes", equalTo(15))
            .body("timeLimits.taCheckinTimeMinutes", equalTo(20));
    }

    @Test
    public void shouldGet404WhenGettingTimeLimitsWithInvalidClientNameAndAssessmentId() {
        final String clientName = "foo";
        final String assessmentId = "bar";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/timelimits/" + clientName + "/" + assessmentId)
        .then()
            .contentType(ContentType.JSON)
            .statusCode(404);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // TaCheckIn Time Tests
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    public void shouldGetATaCheckInTimeForAClientName() {
        final String clientName = "SBAC_PT";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/timelimits/" + clientName + "/checkin")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("taCheckinTimeMinutes", equalTo(20));
    }

    @Test
    public void shouldGet404ForTaCheckInTimeForAnInvalidClientName() {
        final String clientName = "foo";

        given()
            .accept(ContentType.JSON)
        .when()
            .get(CONFIG_RESOURCE + "/timelimits/" + clientName + "/checkin")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(404);
    }
}
