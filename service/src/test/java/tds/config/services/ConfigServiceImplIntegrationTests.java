package tds.config.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
public class ConfigServiceImplIntegrationTests {
    @Autowired
    private ConfigService configService;

    @Test
    public void shouldGetAClientTestProperty() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configService.getClientTestProperty(clientName, assessmentId);

        assertThat(result.isPresent()).isTrue();

        ClientTestProperty clientTestProperty = result.get();
        assertThat(clientTestProperty.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientTestProperty.getAssessmentId()).isEqualTo("SBAC Math 3-MATH-3");
        assertThat(clientTestProperty.getMaxOpportunities()).isEqualTo(3);
        assertThat(clientTestProperty.getPrefetch()).isEqualTo(2);
        assertThat(clientTestProperty.getIsPrintable()).isEqualTo(false);
        assertThat(clientTestProperty.getIsSelectable()).isEqualTo(true);
        assertThat(clientTestProperty.getLabel()).isEqualTo("Grades 3 - 5 MATH");
        assertThat(clientTestProperty.getScoreByTds()).isEqualTo(true);
        assertThat(clientTestProperty.getSubjectName()).isEqualTo("MATH");
        assertThat(clientTestProperty.getMaskItemsBySubject()).isEqualTo(true);
        assertThat(clientTestProperty.getAccommodationFamily()).isEqualTo("MATH");
        assertThat(clientTestProperty.getRtsFormField()).isEqualTo("tds-testform");
        assertThat(clientTestProperty.getRtsWindowField()).isEqualTo("tds-testwindow");
        assertThat(clientTestProperty.getWindowTideSelectable()).isEqualTo(false);
        assertThat(clientTestProperty.getRequireRtsWindow()).isEqualTo(false);
        assertThat(clientTestProperty.getForceComplete()).isEqualTo(true);
        assertThat(clientTestProperty.getRtsModeField()).isEqualTo("tds-testmode");
        assertThat(clientTestProperty.getModeTideSelectable()).isEqualTo(false);
        assertThat(clientTestProperty.getRequireRtsMode()).isEqualTo(false);
        assertThat(clientTestProperty.getRequireRtsModeWindow()).isEqualTo(false);
        assertThat(clientTestProperty.getDeleteUnansweredItems()).isEqualTo(false);
        assertThat(clientTestProperty.getAbilitySlope()).isEqualTo(1d);
        assertThat(clientTestProperty.getAbilityIntercept()).isEqualTo(0d);
        assertThat(clientTestProperty.getValidateCompleteness()).isEqualTo(false);
        assertThat(clientTestProperty.getGradeText()).isEqualTo("Grades 3 - 5");
        assertThat(clientTestProperty.getProctorEligibility()).isEqualTo(0);
    }

    @Test
    public void shouldNotBePresentForInvalidClientName() {
        final String clientName = "foo";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configService.getClientTestProperty(clientName, assessmentId);

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldGetAClientSystemFlag() {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        Optional<ClientSystemFlag> result = configService.getClientSystemFlag(clientName, auditObject);

        assertThat(result.isPresent()).isTrue();

        ClientSystemFlag clientSystemFlag = result.get();
        assertThat(clientSystemFlag.getClientName()).isEqualTo(clientName);
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo(auditObject);
        assertThat(clientSystemFlag.getDateChanged()).isNotNull();
        assertThat(clientSystemFlag.getIsPracticeTest()).isTrue();
        assertThat(clientSystemFlag.getIsOn()).isTrue();
    }

    @Test
    public void shouldNotBePresentForInvalidAuditObject() {
        final String clientName = "SBAC_PT";
        final String auditObject = "foo";

        Optional<ClientSystemFlag> result = configService.getClientSystemFlag(clientName, auditObject);

        assertThat(result.isPresent()).isFalse();
    }
}
