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
import tds.config.model.AssessmentFormWindowProperties;
import tds.config.model.AssessmentWindowParameters;
import tds.config.repositories.AssessmentWindowQueryRepository;
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
    private AssessmentWindowQueryRepository mockAssessmentWindowQueryRepository;

    @Before
    public void Setup() {
        configService = new ConfigServiceImpl(
            mockConfigRepository,
            mockAssessmentWindowQueryRepository
        );
    }

    @After
    public void Teardown() {
        mockConfigRepository = null;
        configService = null;
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
        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(-1, "test", "assessment", 0).build();
        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("test", "assessment", 0, 0, 0)).thenReturn(Collections.emptyList());
        assertThat(configService.findAssessmentWindows(properties)).isEmpty();
    }

    @Test
    public void shouldReturnWindowForGuestWhenFound() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").build();
        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(-1, "test", "assessment", 0).build();

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("test", "assessment", 0, 0, 0)).thenReturn(Collections.singletonList(window));
        assertThat(configService.findAssessmentWindows(properties).get(0)).isEqualTo(window);
        verify(mockAssessmentWindowQueryRepository).findCurrentAssessmentWindows("test", "assessment", 0, 0, 0);
    }

    @Test
    public void shouldReturnDistinctWindowsWhenWindowIsNotRequired() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window3 = new AssessmentWindow.Builder().withWindowId("id3").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window4 = new AssessmentWindow.Builder().withWindowId("id4").withAssessmentKey("SBAC-Mathematics-3").build();
        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0).build();

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0)).thenReturn(Arrays.asList(window, window2, window3, window4));
        List<AssessmentWindow> windows = configService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window, window3, window4);
    }

    @Test
    public void shouldReturnAllFormWindowsByDefault() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window3 = new AssessmentWindow.Builder().withWindowId("id3").withAssessmentKey("SBAC-Mathematics-8-2018").build();
        AssessmentWindow window4 = new AssessmentWindow.Builder().withWindowId("id4").withAssessmentKey("SBAC-Mathematics-3").build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0).build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2, window3, window4));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8", 0)).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows = configService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window, window2, window3, window4);
    }

    @Test
    public void shouldReturnDistinctFormWindowsByFormKey() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("id")
            .withFormKey("formKey1")
            .withAssessmentKey("SBAC-Mathematics-8")
            .build();

        AssessmentWindow window2 = new AssessmentWindow.Builder()
            .withWindowId("id2")
            .withFormKey("formKey2")
            .withAssessmentKey("SBAC-Mathematics-8-2018")
            .build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0)
            .withFormList("formKey2")
            .build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8", 0)).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows = configService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window2);
    }

    @Test
    public void shouldReturnDistinctFormWindowsByWindowIdAndFormKey() {
        AssessmentWindow window = new AssessmentWindow.Builder()
            .withWindowId("id")
            .withFormKey("formKey")
            .withAssessmentKey("SBAC-Mathematics-8")
            .build();

        AssessmentWindow window2 = new AssessmentWindow.Builder()
            .withWindowId("id2")
            .withFormKey("formKey")
            .withAssessmentKey("SBAC-Mathematics-8-2018")
            .build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8", 0)
            .withFormList("id:formKey")
            .build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8", 0)).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows = configService.findAssessmentWindows(properties);

        assertThat(windows).containsExactly(window);
    }

    private List<ClientSystemFlag> getMockClientSystemFlag() {
        List<ClientSystemFlag> clientSystemFlags = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ClientSystemFlag clientSystemFlag = new ClientSystemFlag.Builder()
                .withClientName("SBAC_PT")
                .withAuditObject("AUDIT_OBJECT " + i)
                .withDescription("unit test description " + i)
                .withEnabled(true)
                .withIsPracticeTest(true)
                .build();

            clientSystemFlags.add(clientSystemFlag);
        }

        return clientSystemFlags;
    }
}
