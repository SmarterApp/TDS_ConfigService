package tds.config.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
@WebAppConfiguration
public class ConfigServiceImplIntegrationTests {
    @Autowired
    private ConfigService configService;

    @Test
    public void shouldGetAClientTestProperty() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        ClientTestProperty result = configService.getClientTestProperty(clientName, assessmentId);

        assertThat(result).isNotNull();
        assertThat(result.getClientName()).isEqualTo("SBAC_PT");
        assertThat(result.getAssessmentId()).isEqualTo("SBAC Math 3-MATH-3");
        assertThat(result.getMaxOpportunities()).isEqualTo(9999);
        assertThat(result.getPrefetch()).isEqualTo(2);
        assertThat(result.getIsPrintable()).isEqualTo(false);
        assertThat(result.getIsSelectable()).isEqualTo(true);
        assertThat(result.getLabel()).isEqualTo("Grades 3 - 5 MATH");
        assertThat(result.getScoreByTds()).isEqualTo(true);
        assertThat(result.getSubjectName()).isEqualTo("MATH");
        assertThat(result.getMaskItemsBySubject()).isEqualTo(true);
        assertThat(result.getAccommodationFamily()).isEqualTo("MATH");
        assertThat(result.getRtsFormField()).isEqualTo("tds-testform");
        assertThat(result.getRtsWindowField()).isEqualTo("tds-testwindow");
        assertThat(result.getWindowTideSelectable()).isEqualTo(false);
        assertThat(result.getRequireRtsWindow()).isEqualTo(false);
        assertThat(result.getForceComplete()).isEqualTo(true);
        assertThat(result.getRtsModeField()).isEqualTo("tds-testmode");
        assertThat(result.getModeTideSelectable()).isEqualTo(false);
        assertThat(result.getRequireRtsMode()).isEqualTo(false);
        assertThat(result.getRequireRtsModeWindow()).isEqualTo(false);
        assertThat(result.getDeleteUnansweredItems()).isEqualTo(false);
        assertThat(result.getAbilitySlope()).isEqualTo(1d);
        assertThat(result.getAbilityIntercept()).isEqualTo(0d);
        assertThat(result.getValidateCompleteness()).isEqualTo(false);
        assertThat(result.getGradeText()).isEqualTo("Grades 3 - 5");
        assertThat(result.getProctorEligibility()).isEqualTo(0);
    }

    @Test
    public void should_Get_a_ClientTestProperty() {
        shouldGetAClientTestProperty();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementExceptionForInvalidClientName() {
        final String clientName = "foo";
        final String assessmentId = "SBAC Math 3-MATH-3";

        configService.getClientTestProperty(clientName, assessmentId);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_Throw_NoSuchElementException_For_Invalid_ClientName() {
        shouldThrowNoSuchElementExceptionForInvalidClientName();
    }

    @Test
    public void shouldGetAClientSystemFlag() {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        ClientSystemFlag result = configService.getClientSystemFlag(clientName, auditObject);

        assertThat(result).isNotNull();
        assertThat(result.getClientName()).isEqualTo(clientName);
        assertThat(result.getAuditObject()).isEqualTo(auditObject);
        assertThat(result.getDateChanged()).isNotNull();
        assertThat(result.getIsPracticeTest()).isTrue();
        assertThat(result.getIsOn()).isTrue();
    }

    @Test
    public void should_Get_a_ClientSystemFlag() {
        shouldGetAClientSystemFlag();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementExceptionForInvalidAuditObject() {
        final String clientName = "SBAC_PT";
        final String auditObject = "foo";

        configService.getClientSystemFlag(clientName, auditObject);
    }

    @Test(expected = NoSuchElementException.class)
    public void should_Throw_NoSuchElementException_For_Invalid_AuditObject() {
        shouldThrowNoSuchElementExceptionForInvalidAuditObject();
    }
}
