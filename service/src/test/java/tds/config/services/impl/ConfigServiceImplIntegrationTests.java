package tds.config.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;
import tds.config.services.ConfigService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
public class ConfigServiceImplIntegrationTests {
    @Autowired
    private ConfigService configService;

    // -----------------------------------------------------------------------------------------------------------------
    // ClientTestProperty Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientTestProperty() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configService.findClientTestProperty(clientName, assessmentId);

        assertThat(result).isPresent();

        ClientTestProperty clientTestProperty = result.get();
        assertThat(clientTestProperty.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientTestProperty.getAssessmentId()).isEqualTo("SBAC Math 3-MATH-3");
        assertThat(clientTestProperty.getMaxOpportunities()).isEqualTo(3);
        assertThat(clientTestProperty.getPrefetch()).isEqualTo(2);
        assertThat(clientTestProperty.getIsSelectable()).isTrue();
        assertThat(clientTestProperty.getLabel()).isEqualTo("Grades 3 - 5 MATH");
        assertThat(clientTestProperty.getSubjectName()).isEqualTo("MATH");
        assertThat(clientTestProperty.getAccommodationFamily()).isEqualTo("MATH");
        assertThat(clientTestProperty.getRtsFormField()).isEqualTo("tds-testform");
        assertThat(clientTestProperty.getRequireRtsWindow()).isFalse();
        assertThat(clientTestProperty.getRtsModeField()).isEqualTo("tds-testmode");
        assertThat(clientTestProperty.getRequireRtsMode()).isFalse();
        assertThat(clientTestProperty.getRequireRtsModeWindow()).isFalse();
        assertThat(clientTestProperty.getDeleteUnansweredItems()).isFalse();
        assertThat(clientTestProperty.getAbilitySlope()).isEqualTo(1D);
        assertThat(clientTestProperty.getAbilityIntercept()).isEqualTo(0D);
        assertThat(clientTestProperty.getValidateCompleteness()).isFalse();
        assertThat(clientTestProperty.getGradeText()).isEqualTo("Grades 3 - 5");
    }

    @Test
    public void shouldGetOptionalEmptyClientTestPropertyForInvalidClientName() {
        final String clientName = "foo";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configService.findClientTestProperty(clientName, assessmentId);

        assertThat(result).isNotPresent();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlag() {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag(clientName, auditObject);

        assertThat(result).isPresent();

        ClientSystemFlag clientSystemFlag = result.get();
        assertThat(clientSystemFlag.getClientName()).isEqualTo(clientName);
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo(auditObject);
        assertThat(clientSystemFlag.getDateChanged()).isNotNull();
        assertThat(clientSystemFlag.getIsPracticeTest()).isTrue();
        assertThat(clientSystemFlag.getIsOn()).isTrue();
    }

    @Test
    public void shouldReturnOptionalEmptyClientSystemFlagForInvalidAuditObject() {
        final String clientName = "SBAC_PT";
        final String auditObject = "foo";

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag(clientName, auditObject);

        assertThat(result.isPresent()).isFalse();
    }
}
