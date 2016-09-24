package tds.config.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;
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

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

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

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

        assertThat(result.size()).isEqualTo(0);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientTestProperty Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientTestProperty() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configRepository.findClientTestProperty(clientName, assessmentId);

        assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo("SBAC_PT");
        assertThat(result.get().getAssessmentId()).isEqualTo("SBAC Math 3-MATH-3");
        assertThat(result.get().getMaxOpportunities()).isEqualTo(3);
        assertThat(result.get().getPrefetch()).isEqualTo(2);
        assertThat(result.get().getIsSelectable()).isTrue();
        assertThat(result.get().getLabel()).isEqualTo("Grades 3 - 5 MATH");
        assertThat(result.get().getSubjectName()).isEqualTo("MATH");
        assertThat(result.get().getAccommodationFamily()).isEqualTo("MATH");
        assertThat(result.get().getRtsFormField()).isEqualTo("tds-testform");
        assertThat(result.get().getRequireRtsWindow()).isFalse();
        assertThat(result.get().getRtsModeField()).isEqualTo("tds-testmode");
        assertThat(result.get().getRequireRtsMode()).isFalse();
        assertThat(result.get().getRequireRtsModeWindow()).isFalse();
        assertThat(result.get().getDeleteUnansweredItems()).isFalse();
        assertThat(result.get().getAbilitySlope()).isEqualTo(1D);
        assertThat(result.get().getAbilityIntercept()).isEqualTo(0D);
        assertThat(result.get().getValidateCompleteness()).isFalse();
        assertThat(result.get().getGradeText()).isEqualTo("Grades 3 - 5");
    }

    @Test
    public void shouldReturnAnOptionalEmptyClientTestPropertyForAnInvalidClientName() {
        final String phonyClientName = "foo";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = configRepository.findClientTestProperty(phonyClientName, assessmentId);

        assertThat(result).isNotPresent();
    }
}