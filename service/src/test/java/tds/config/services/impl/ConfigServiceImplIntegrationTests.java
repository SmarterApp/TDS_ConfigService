package tds.config.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.CurrentExamWindow;
import tds.config.model.ExamWindowProperties;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
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

    /*
    Exam Window Tests
     */
    @Test
    public void shouldReturnEmptyWindowWhenNoResultsAreFoundForGuest() {
        ExamWindowProperties properties = new ExamWindowProperties.Builder(-1, "clientName", "assessment", 0)
            .withWindowList("")
            .withFormList("")
            .build();
        assertThat(configService.getExamWindow(properties)).isNotPresent();
    }

    @Test
    public void shouldReturnEmptyWindowWhenNotFound() {
        ExamWindowProperties properties = new ExamWindowProperties.Builder(-1, "clientName", "assessment", 0).build();
        Optional<CurrentExamWindow> maybeWindow = configService.getExamWindow(properties);
        assertThat(maybeWindow).isEmpty();
    }

    @Test
    public void shouldReturnWindowWhenFound() {
        ExamWindowProperties properties = new ExamWindowProperties.Builder(-1, "SBAC_PT", "SBAC-IRP-CAT-ELA-11", 0).build();

        Optional<CurrentExamWindow> maybeWindow = configService.getExamWindow(properties);
        assertThat(maybeWindow).isPresent();

        CurrentExamWindow window = maybeWindow.get();
        assertThat(window.getMode()).isEqualTo("online");
        assertThat(window.getWindowId()).isEqualTo("ANNUAL");
        assertThat(window.getModeMaxAttempts()).isEqualTo(999);
        assertThat(window.getModeSessionType()).isEqualTo(0);
        assertThat(window.getWindowMaxAttempts()).isEqualTo(999);
        assertThat(window.getWindowSessionType()).isEqualTo(-1);
    }
}
