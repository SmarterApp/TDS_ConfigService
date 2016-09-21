package tds.config.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;
import tds.config.TimeLimits;
import tds.config.repositories.ConfigRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
public class ConfigRepositoryImplIntegrationTests {

    @Autowired
    private ConfigRepository configRepository;

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlagsList() {
        final String clientName = "SBAC_PT";

        List<ClientSystemFlag> result = configRepository.getClientSystemFlags(clientName);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(16);
        result.forEach(r -> {
            assertThat(r.getAuditObject()).isNotNull();
            assertThat(r.getClientName()).isEqualTo(clientName);
        });
    }

    @Test
    public void shouldGetOptionalEmptyClientSystemFlagsForAnInvalidClientName() {
        final String clientName = "foo";

        List<ClientSystemFlag> result = configRepository.getClientSystemFlags(clientName);

        assertThat(result.size()).isEqualTo(0);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientTestProperty Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientTestProperty() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configRepository.getClientTestProperty(clientName, assessmentId);

        assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo("SBAC_PT");
        assertThat(result.get().getAssessmentId()).isEqualTo("SBAC Math 3-MATH-3");
        assertThat(result.get().getMaxOpportunities()).isEqualTo(3);
        assertThat(result.get().getPrefetch()).isEqualTo(2);
        assertThat(result.get().getIsPrintable()).isEqualTo(false);
        assertThat(result.get().getIsSelectable()).isEqualTo(true);
        assertThat(result.get().getLabel()).isEqualTo("Grades 3 - 5 MATH");
        assertThat(result.get().getScoreByTds()).isEqualTo(true);
        assertThat(result.get().getSubjectName()).isEqualTo("MATH");
        assertThat(result.get().getMaskItemsBySubject()).isEqualTo(true);
        assertThat(result.get().getAccommodationFamily()).isEqualTo("MATH");
        assertThat(result.get().getRtsFormField()).isEqualTo("tds-testform");
        assertThat(result.get().getRtsWindowField()).isEqualTo("tds-testwindow");
        assertThat(result.get().getWindowTideSelectable()).isEqualTo(false);
        assertThat(result.get().getRequireRtsWindow()).isEqualTo(false);
        assertThat(result.get().getForceComplete()).isEqualTo(true);
        assertThat(result.get().getRtsModeField()).isEqualTo("tds-testmode");
        assertThat(result.get().getModeTideSelectable()).isEqualTo(false);
        assertThat(result.get().getRequireRtsMode()).isEqualTo(false);
        assertThat(result.get().getRequireRtsModeWindow()).isEqualTo(false);
        assertThat(result.get().getDeleteUnansweredItems()).isEqualTo(false);
        assertThat(result.get().getAbilitySlope()).isEqualTo(1d);
        assertThat(result.get().getAbilityIntercept()).isEqualTo(0d);
        assertThat(result.get().getValidateCompleteness()).isEqualTo(false);
        assertThat(result.get().getGradeText()).isEqualTo("Grades 3 - 5");
        assertThat(result.get().getProctorEligibility()).isEqualTo(0);
    }

    @Test
    public void shouldReturnAnOptionalEmptyClientTestPropertyForAnInvalidClientName() {
        final String phonyClientName = "foo";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configRepository.getClientTestProperty(phonyClientName, assessmentId);

        assertThat(result).isNotPresent();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // TimeLimits Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetTimeLimitsForAClientName() {
        final String clientName = "SBAC_PT";

        Optional<TimeLimits> result = configRepository.getTimeLimits(clientName);

        assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo(clientName);
        assertThat(result.get().getAssessmentId()).isNull();
        assertThat(result.get().getEnvironment()).isEqualTo("Development");
        assertThat(result.get().getExamRestartWindowMinutes()).isEqualTo(10);
        assertThat(result.get().getExamDelayDays()).isEqualTo(-1);
        assertThat(result.get().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(result.get().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
        assertThat(result.get().getTaCheckinTimeMinutes()).isEqualTo(20);
    }

    @Test
    public void shouldGetTimeLimitsForAClientNameAndAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<TimeLimits> result = configRepository.getTimeLimits(clientName, assessmentId);

        assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo(clientName);
        assertThat(result.get().getAssessmentId()).isNotNull();
        assertThat(result.get().getAssessmentId()).isEqualTo(assessmentId);
        assertThat(result.get().getEnvironment()).isEqualTo("Development");
        assertThat(result.get().getExamRestartWindowMinutes()).isEqualTo(10);
        assertThat(result.get().getExamDelayDays()).isEqualTo(-1);
        assertThat(result.get().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(result.get().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
        assertThat(result.get().getTaCheckinTimeMinutes()).isEqualTo(20);
    }

    @Test
    public void shouldReturnOptionalEmptyTimeLimitForInvalidClientName() {
        final String clientName = "foo";

        Optional<TimeLimits> result = configRepository.getTimeLimits(clientName);

        assertThat(result).isNotPresent();
    }

    @Test
    public void shouldReturnOptionalEmptyTimeLimitsForAValidClientNameAndInvalidAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "foo";

        Optional<TimeLimits> result = configRepository.getTimeLimits(clientName, assessmentId);

        assertThat(result).isNotPresent();
    }
}