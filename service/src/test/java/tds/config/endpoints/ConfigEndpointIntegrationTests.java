package tds.config.endpoints;

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
            .get(CONFIG_RESOURCE + "/isAlive")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200);
    }

    @Test
    public void should_Respond_to_IsAlive() {
        shouldRespondToIsAlive();
    }

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
            .body("clientTestProperty.maxOpportunities", equalTo(9999))
            .body("_links.self.href", equalTo("http://localhost:8080/config/clientTestProperties/SBAC_PT/SBAC%20Math%203-MATH-3"));
    }


    @Test
    public void should_Get_a_ClientTestProperty() {
        shouldGetAClientTestProperty();
    }

    @Test
    public void shouldReturn404ForClientTestPropertyWithInvalidClientName() {
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

    @Test
    public void should_Return_a_404_For_ClientTestProperty_With_Invalid_ClientName() {
        shouldReturn404ForClientTestPropertyWithInvalidClientName();
    }

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
    public void should_Get_a_ClientSystemFlag() {
        shouldGetAClientSystemFlag();
    }

    @Test
    public void shouldGetA404ForClientSystemFlagWithAnInvalidAuditObject() {
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

    @Test
    public void should_Get_a_404_For_ClientSystemFlag_With_an_Invalid_AuditObject() {
        shouldGetA404ForClientSystemFlagWithAnInvalidAuditObject();
    }
}
