package tds.config.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConfigServiceApplication.class)
@WebAppConfiguration
public class ConfigRepositoryImplIntegrationTests {

    @Autowired
    private ConfigRepository configRepository;

    @Test
    public void shouldGetAClientSystemFlagsList() {
        final String clientName = "SBAC_PT";

        List<ClientSystemFlag> result = configRepository.getClientSystemFlags(clientName);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(16);
    }

    @Test
    public void should_Get_a_ClientSystemFlag_List() {
        shouldGetAClientSystemFlagsList();
    }

    @Test
    public void shouldGetAClientTestProperty() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        ClientTestProperty result = configRepository.getClientTestProperty(clientName, assessmentId);

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
    public void should_Get_a_ClientTestProperty() throws Exception {
        shouldGetAClientTestProperty();
    }

    @Test
    public void shouldReturnNullForAnInvalidClientName() {
        final String phonyClientName = "foo";
        final String assessmentId = "SBAC Math 3-MATH-3";

        ClientTestProperty result = configRepository.getClientTestProperty(phonyClientName, assessmentId);

        assertThat(result).isNull();
    }

    @Test
    public void should_Return_Null_For_an_Invalid_ClientName() {
        shouldReturnNullForAnInvalidClientName();
    }
}