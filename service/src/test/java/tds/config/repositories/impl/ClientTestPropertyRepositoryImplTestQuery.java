package tds.config.repositories.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;
import tds.config.repositories.ClientTestPropertyQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
public class ClientTestPropertyRepositoryImplTestQuery {

    @Autowired
    private ClientTestPropertyQueryRepository clientTestPropertyQueryRepository;

    @Test
    public void shouldGetAClientTestProperty() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> maybeClientTestProperty = clientTestPropertyQueryRepository.findClientTestProperty(clientName, assessmentId);

        assertThat(maybeClientTestProperty).isPresent();

        ClientTestProperty clientTestProperty = maybeClientTestProperty.get();
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
    public void shouldReturnAnOptionalEmptyClientTestPropertyForAnInvalidClientName() {
        final String phonyClientName = "foo";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<ClientTestProperty> result = clientTestPropertyQueryRepository.findClientTestProperty(phonyClientName, assessmentId);

        assertThat(result).isNotPresent();
    }
}
