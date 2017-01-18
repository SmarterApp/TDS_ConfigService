package tds.config.services.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import tds.config.AssessmentWindow;
import tds.config.model.AssessmentFormWindowProperties;
import tds.config.model.AssessmentWindowParameters;
import tds.config.repositories.AssessmentWindowQueryRepository;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConfigServiceImplIntegrationTests {
    @MockBean
    private AssessmentWindowQueryRepository mockAssessmentWindowQueryRepository;

    @Autowired
    private ConfigService configService;

    @Test
    public void shouldReturnCachedAssessmentWindow() {
        AssessmentWindow window = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8").build();
        AssessmentWindow window2 = new AssessmentWindow.Builder().withWindowId("id").withAssessmentKey("SBAC-Mathematics-8-2018").build();

        AssessmentWindowParameters properties = new AssessmentWindowParameters.Builder(23, "SBAC_PT", "SBAC-Mathematics-8").build();
        AssessmentFormWindowProperties assessmentFormWindowProperties = new AssessmentFormWindowProperties(true, true, "formField", true);

        when(mockAssessmentWindowQueryRepository.findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0)).thenReturn(Arrays.asList(window, window2));
        when(mockAssessmentWindowQueryRepository.findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8")).thenReturn(Optional.of(assessmentFormWindowProperties));
        List<AssessmentWindow> windows1 = configService.findAssessmentWindows(properties);
        List<AssessmentWindow> windows2 = configService.findAssessmentWindows(properties);

        assertThat(windows1).containsExactly(window, window2);
        assertThat(windows2).containsExactly(window, window2);
        verify(mockAssessmentWindowQueryRepository, times(1)).findCurrentAssessmentFormWindows("SBAC_PT", "SBAC-Mathematics-8", 0, 0, 0, 0);
        verify(mockAssessmentWindowQueryRepository, times(1)).findAssessmentFormWindowProperties("SBAC_PT", "SBAC-Mathematics-8");
    }
}
