package tds.config.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.CurrentExamWindow;
import tds.config.model.ExamWindowProperties;
import tds.config.repositories.ClientTestFormPropertiesQueryRepository;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.ConfigRepository;
import tds.config.repositories.ExamWindowQueryRepository;
import tds.config.services.ConfigService;
import tds.config.services.StudentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceImplTest {

    private ConfigService configService;

    @Mock
    private ConfigRepository mockConfigRepository;

    @Mock
    private ClientTestPropertyQueryRepository mockClientTestPropertyQueryRepository;

    @Mock
    private ExamWindowQueryRepository mockExamWindowQueryRepository;

    @Mock
    private ClientTestFormPropertiesQueryRepository mockClientTestFormPropertiesRepository;

    @Mock
    private ExamWindowQueryRepository mockExamQueryRepository;

    @Mock
    private StudentService mockStudentService;

    @Before
    public void Setup() {
        configService = new ConfigServiceImpl(
            mockConfigRepository,
            mockClientTestPropertyQueryRepository,
            mockExamWindowQueryRepository,
            mockClientTestFormPropertiesRepository,
            mockStudentService
        );
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
        when(mockClientTestPropertyQueryRepository.findClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());

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
        when(mockClientTestPropertyQueryRepository.findClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());
        when(mockClientTestPropertyQueryRepository.findClientTestProperty("foo", "TEST_ID")).thenReturn(Optional.empty());

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

    @Test
    public void shouldReturnEmptyWindowWhenNoResultsAreFoundForGuest() {
        ExamWindowProperties properties = new ExamWindowProperties.Builder(-1, "test", "assessment", 0).build();
        when(mockExamWindowQueryRepository.findCurrentTestWindowsForGuest("test", "assessment", 0, 0)).thenReturn(Optional.empty());
        assertThat(configService.getExamWindow(properties)).isNotPresent();
    }

    @Test
    public void shouldReturnWindowForGuestWhenFound() {
        CurrentExamWindow window = new CurrentExamWindow.Builder().withWindowId("id").build();
        ExamWindowProperties properties = new ExamWindowProperties.Builder(-1, "test", "assessment", 0).build();

        when(mockExamWindowQueryRepository.findCurrentTestWindowsForGuest("test", "assessment", 0, 0)).thenReturn(Optional.of(window));
        assertThat(configService.getExamWindow(properties).get()).isEqualTo(window);
        verify(mockExamWindowQueryRepository).findCurrentTestWindowsForGuest("test", "assessment", 0, 0);
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
