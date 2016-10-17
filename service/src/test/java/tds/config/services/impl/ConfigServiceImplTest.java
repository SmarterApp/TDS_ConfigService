package tds.config.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tds.config.AssessmentWindow;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.AssessmentProperties;
import tds.config.model.ExamWindowProperties;
import tds.config.repositories.AssessmentWindowQueryRepository;
import tds.config.repositories.ClientTestPropertyQueryRepository;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

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
    private AssessmentWindowQueryRepository mockAssessmentWindowQueryRepository;

    @Mock
    private AssessmentWindowQueryRepository mockExamQueryRepository;

    @Before
    public void Setup() {
        configService = new ConfigServiceImpl(
            mockConfigRepository,
            mockClientTestPropertyQueryRepository,
            mockAssessmentWindowQueryRepository
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
        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("test", "assessment", 0, 0, 0)).thenReturn(Collections.emptyList());
        assertThat(configService.getExamWindow(properties)).isEmpty();
    }

    @Test
    public void shouldReturnWindowForGuestWhenFound() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").build();
        ExamWindowProperties properties = new ExamWindowProperties.Builder(-1, "test", "assessment", 0).build();

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("test", "assessment", 0, 0, 0)).thenReturn(Collections.singletonList(window));
        assertThat(configService.getExamWindow(properties).get(0)).isEqualTo(window);
        verify(mockAssessmentWindowQueryRepository).findCurrentAssessmentWindows("test", "assessment", 0, 0, 0);
    }

    @Test
    public void shouldReturnDistinctWindowsWhenWindowIsNotRequired() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentId("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentId("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window3 = new AssessmentWindow.Builder().withWindowId("id3").withAssessmentId("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window4 = new AssessmentWindow.Builder().withWindowId("id4").withAssessmentId("SBAC-Mathematics-3").build();

        ExamWindowProperties properties = new ExamWindowProperties.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0).build();
        ClientTestProperty property = new ClientTestProperty.Builder().build();

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0)).thenReturn(Arrays.asList(window, window2, window3, window4));
        when(mockClientTestPropertyQueryRepository.findClientTestProperty("SBAC_PT", "SBAC-Mathematics-8")).thenReturn(Optional.of(property));
        List<AssessmentWindow> windows = configService.getExamWindow(properties);

        assertThat(windows).containsExactly(window, window3, window4);
    }

    @Test
    public void shouldReturnDistinctFormWindowsByWindowId() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentId("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentId("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window3 = new AssessmentWindow.Builder().withWindowId("id3").withAssessmentId("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window4 = new AssessmentWindow.Builder().withWindowId("id4").withAssessmentId("SBAC-Mathematics-3").build();

        ExamWindowProperties properties = new ExamWindowProperties.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0).build();
        AssessmentProperties assessmentProperties = new AssessmentProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2, window3, window4));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8", 0)).thenReturn(Optional.of(assessmentProperties));
        List<AssessmentWindow> windows = configService.getExamWindow(properties);

        assertThat(windows).containsExactly(window, window3, window4);
    }

    @Test
    public void shouldReturnDistinctFormWindowsByFormKey() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("id")
            .withFormKey("formKey1")
            .withAssessmentId("SBAC-Mathematics-8")
            .build();

        AssessmentWindow window2 = new AssessmentWindow.Builder()
            .withWindowId("id2")
            .withFormKey("formKey2")
            .withAssessmentId("SBAC-Mathematics-8-2018")
            .build();

        ExamWindowProperties properties = new ExamWindowProperties.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0)
            .withFormList("formKey2")
            .build();
        AssessmentProperties assessmentProperties = new AssessmentProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8", 0)).thenReturn(Optional.of(assessmentProperties));
        List<AssessmentWindow> windows = configService.getExamWindow(properties);

        assertThat(windows).containsExactly(window2);
    }

    @Test
    public void shouldReturnDistinctFormWindowsByWindowIdAndFormKey() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("id")
            .withFormKey("formKey")
            .withAssessmentId("SBAC-Mathematics-8")
            .build();

        AssessmentWindow window2 = new AssessmentWindow.Builder()
            .withWindowId("id2")
            .withFormKey("formKey")
            .withAssessmentId("SBAC-Mathematics-8-2018")
            .build();

        ExamWindowProperties properties = new ExamWindowProperties.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0)
            .withFormList("id:formKey")
            .build();
        AssessmentProperties assessmentProperties = new AssessmentProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8", 0)).thenReturn(Optional.of(assessmentProperties));
        List<AssessmentWindow> windows = configService.getExamWindow(properties);

        assertThat(windows).containsExactly(window);
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
