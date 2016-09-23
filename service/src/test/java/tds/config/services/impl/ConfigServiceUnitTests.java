package tds.config.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceUnitTests {

    private ConfigService configService;

    @Mock
    private ConfigRepository mockConfigRepository;

    @Before
    public void Setup() {
        configService = new ConfigServiceImpl(mockConfigRepository);
    }

    @After
    public void Teardown() {
        mockConfigRepository = null;
        configService = null;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemProperty Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientTestProperty() {
        when(mockConfigRepository.findClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());

        Optional<ClientTestProperty> result = configService.findClientTestProperty("SBAC_PT", "TEST_ID");

        assertThat(result.isPresent()).isTrue();

        ClientTestProperty clientTestProperty = result.get();
        assertThat(clientTestProperty.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientTestProperty.getAssessmentId()).isEqualTo("TEST_ID");
        assertThat(clientTestProperty.getMaxOpportunities()).isEqualTo(3);
        assertThat(clientTestProperty.getPrefetch()).isEqualTo(2);
        assertThat(clientTestProperty.getIsSelectable()).isTrue();
        assertThat(clientTestProperty.getLabel()).isEqualTo("label");
        assertThat(clientTestProperty.getSubjectName()).isEqualTo("subject name");
        assertThat(clientTestProperty.getInitialAbilityBySubject()).isTrue();
        assertThat(clientTestProperty.getAccommodationFamily()).isEqualTo("accommodation family");
        assertThat(clientTestProperty.getSortOrder()).isEqualTo(1);
        assertThat(clientTestProperty.getRtsFormField()).isEqualTo("rts form field");
        assertThat(clientTestProperty.getRequireRtsWindow()).isTrue();
        assertThat(clientTestProperty.getTideId()).isEqualTo("tide id");
        assertThat(clientTestProperty.getRequireRtsModeWindow()).isTrue();
        assertThat(clientTestProperty.getDeleteUnansweredItems()).isTrue();
        assertThat(clientTestProperty.getAbilitySlope()).isEqualTo(5.0D);
        assertThat(clientTestProperty.getAbilityIntercept()).isEqualTo(10.0D);
        assertThat(clientTestProperty.getValidateCompleteness()).isTrue();
        assertThat(clientTestProperty.getGradeText()).isEqualTo("grade text");
    }

    @Test
    public void shouldReturnOptionalEmptyClientTestPropertyForAnInvalidClientName() {
        when(mockConfigRepository.findClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());
        when(mockConfigRepository.findClientTestProperty("foo", "TEST_ID")).thenReturn(Optional.empty());

        Optional<ClientTestProperty> result = configService.findClientTestProperty("foo", "TEST_ID");

        assertThat(result).isNotPresent();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlag() {
        when(mockConfigRepository.findClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag("SBAC_PT", "AUDIT_OBJECT 1");

        assertThat(result.isPresent()).isTrue();

        ClientSystemFlag clientSystemFlag = result.get();
        assertThat(clientSystemFlag.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo("AUDIT_OBJECT 1");
    }

    @Test
    public void shouldReturnOptionalEmptyClientSystemFlagForAnInvalidAuditObject() {
        when(mockConfigRepository.findClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag("SBAC_PT", "foo");

        assertThat(result).isNotPresent();
    }

    private Optional<ClientTestProperty> getMockClientTestProperty() {
        ClientTestProperty clientTestProperty = new ClientTestProperty.Builder()
                .withClientName("SBAC_PT")
                .withAssessmentId("TEST_ID")
                .withMaxOpportunities(3)
                .withPrefetch(2)
                .withIsSelectable(true)
                .withLabel("label")
                .withSubjectName("subject name")
                .withInitialAbilityBySubject(true)
                .withAccommodationFamily("accommodation family")
                .withSortOrder(1)
                .withRtsFormField("rts form field")
                .withRequireRtsWindow(true)
                .withTideId("tide id")
                .withRequireRtsMode(true)
                .withRequireRtsModeWindow(true)
                .withDeleteUnansweredItems(true)
                .withAbilitySlope(5.0D)
                .withAbilityIntercept(10.0D)
                .withValidateCompleteness(true)
                .withGradeText("grade text")
                .build();

        return Optional.of(clientTestProperty);
    }

    private List<ClientSystemFlag> getMockClientSystemFlag() {
        List<ClientSystemFlag> clientSystemFlags = new ArrayList<>();

        for (Integer i = 0; i < 5; i++) {
            ClientSystemFlag clientSystemFlag = new ClientSystemFlag.Builder()
                    .withClientName("SBAC_PT")
                    .withAuditObject("AUDIT_OBJECT " + i.toString())
                    .withDescription("unit test description " + i.toString())
                    .withIsOn(true)
                    .withIsPracticeTest(true)
                    .build();

            clientSystemFlags.add(clientSystemFlag);
        }

        return clientSystemFlags;
    }
}
