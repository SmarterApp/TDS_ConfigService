package tds.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientTestPropertyUnitTests {
    @Test
    public void shouldBuildAClientTestProperty() {
        ClientTestProperty clientTestProperty = new ClientTestProperty.Builder()
                .withClientName("SBAC_PT")
                .withAssessmentId("SBAC Math 3-MATH-3")
                .withMaxOpportunities(3)
                .withPrefetch(2)
                .withIsSelectable(true)
                .withLabel("Grades 3 - 5 MATH")
                .withSubjectName("MATH")
                .withAccommodationFamily("MATH")
                .withRtsFormField("tds-testform")
                .withRequireRtsWindow(true)
                .withRtsModeField("tds-testmode")
                .withRequireRtsMode(true)
                .withRequireRtsModeWindow(true)
                .withDeleteUnansweredItems(true)
                .withAbilitySlope(1D)
                .withAbilityIntercept(0D)
                .withValidateCompleteness(true)
                .withGradeText("Grades 3 - 5")
                .build();

        assertThat(clientTestProperty.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientTestProperty.getAssessmentId()).isEqualTo("SBAC Math 3-MATH-3");
        assertThat(clientTestProperty.getMaxOpportunities()).isEqualTo(3);
        assertThat(clientTestProperty.getPrefetch()).isEqualTo(2);
        assertThat(clientTestProperty.getIsSelectable()).isTrue();
        assertThat(clientTestProperty.getLabel()).isEqualTo("Grades 3 - 5 MATH");
        assertThat(clientTestProperty.getSubjectName()).isEqualTo("MATH");
        assertThat(clientTestProperty.getAccommodationFamily()).isEqualTo("MATH");
        assertThat(clientTestProperty.getRtsFormField()).isEqualTo("tds-testform");
        assertThat(clientTestProperty.getRequireRtsWindow()).isTrue();
        assertThat(clientTestProperty.getRtsModeField()).isEqualTo("tds-testmode");
        assertThat(clientTestProperty.getRequireRtsMode()).isTrue();
        assertThat(clientTestProperty.getRequireRtsModeWindow()).isTrue();
        assertThat(clientTestProperty.getDeleteUnansweredItems()).isTrue();
        assertThat(clientTestProperty.getAbilitySlope()).isEqualTo(1D);
        assertThat(clientTestProperty.getAbilityIntercept()).isEqualTo(0D);
        assertThat(clientTestProperty.getValidateCompleteness()).isTrue();
        assertThat(clientTestProperty.getGradeText()).isEqualTo("Grades 3 - 5");
    }
}
