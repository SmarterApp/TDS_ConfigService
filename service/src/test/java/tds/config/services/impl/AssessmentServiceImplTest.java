package tds.config.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tds.assessment.Assessment;
import tds.assessment.Segment;
import tds.config.configuration.ConfigServiceProperties;
import tds.config.services.AssessmentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssessmentServiceImplTest {
    private RestTemplate restTemplate;
    private AssessmentService assessmentService;

    @Before
    public void setUp() {
        restTemplate = mock(RestTemplate.class);
        ConfigServiceProperties properties = new ConfigServiceProperties();
        properties.setAssessmentUrl("http://localhost:8080/assessments");
        assessmentService = new AssessmentServiceImpl(restTemplate, properties);
    }

    @Test
    public void shouldFindAssessmentByKey() {
        List<Segment> segments = new ArrayList<>();
        Segment seg1 = new Segment("segkey");
        seg1.setSegmentId("segid");
        seg1.setAssessmentKey("key");
        seg1.setSelectionAlgorithm("fixedform");
        seg1.setStartAbility(0);
        segments.add(seg1);

        Assessment assessment = new Assessment();
        assessment.setKey("key");
        assessment.setAssessmentId("assessmentId");
        assessment.setSegments(segments);
        assessment.setSelectionAlgorithm("virtual");
        assessment.setStartAbility(100);

        when(restTemplate.getForObject("http://localhost:8080/assessments/key", Assessment.class)).thenReturn(assessment);
        Optional<Assessment> maybeAssessment = assessmentService.findAssessment("key");
        verify(restTemplate).getForObject("http://localhost:8080/assessments/key", Assessment.class);

        assertThat(maybeAssessment.get()).isEqualTo(assessment);
    }

    @Test
    public void shouldReturnEmptyWhenAssessmentNotFound() {
        when(restTemplate.getForObject("http://localhost:8080/assessments/key", Assessment.class)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        Optional<Assessment> maybeAssessment = assessmentService.findAssessment("key");
        verify(restTemplate).getForObject("http://localhost:8080/assessments/key", Assessment.class);

        assertThat(maybeAssessment).isNotPresent();
    }

    @Test (expected = RestClientException.class)
    public void shouldThrowIfStatusNotNotFoundWhenUnexpectedErrorFindingAssessment() {
        when(restTemplate.getForObject("http://localhost:8080/assessments/key", Assessment.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        assessmentService.findAssessment("key");
    }
}
