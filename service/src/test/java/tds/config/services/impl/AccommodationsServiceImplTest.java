package tds.config.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tds.assessment.Assessment;
import tds.assessment.Property;
import tds.assessment.Segment;
import tds.common.web.exceptions.NotFoundException;
import tds.config.Accommodation;
import tds.config.repositories.AccommodationsQueryRepository;
import tds.config.services.AccommodationsService;
import tds.config.services.AssessmentService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccommodationsServiceImplTest {
    @Mock
    private AssessmentService assessmentService;

    @Mock
    private AccommodationsQueryRepository accommodationsQueryRepository;

    private AccommodationsService accommodationsService;

    @Captor
    private ArgumentCaptor<Set<String>> languageCaptor;

    @Before
    public void setUp() {
        accommodationsService = new AccommodationServiceImpl(assessmentService, accommodationsQueryRepository);
    }

    @After
    public void tearDown() {}

    @Test (expected = NotFoundException.class)
    public void shouldThrowNotFoundWhenAssessmentCannotBeFound() {
        when(assessmentService.findAssessment("id")).thenReturn(Optional.empty());
        accommodationsService.findAccommodations("id");
    }

    @Test
    public void shouldFindSegmentedAccommodations() {
        Segment segmentOne = new Segment.Builder("key1").build();
        Segment segmentTwo = new Segment.Builder("key2").build();

        Assessment assessment = new Assessment.Builder()
            .withKey("key")
            .withSegments(Arrays.asList(segmentOne, segmentTwo))
            .build();

        Accommodation accommodation = new Accommodation.Builder().build();

        when(assessmentService.findAssessment("key")).thenReturn(Optional.of(assessment));
        when(accommodationsQueryRepository.findAccommodationsForSegmentedAssessment("key"))
            .thenReturn(Collections.singletonList(accommodation));

        List<Accommodation> accommodationList = accommodationsService.findAccommodations("key");

        assertThat(accommodationList).containsExactly(accommodation);
    }

    @Test
    public void shouldFindNonSegmentedAccommodations() {
        List<Property> languages = Arrays.asList(
            new Property("Language", "ENU"),
            new Property("Language", "Braille"),
            new Property("Language", "FRN")
        );
        Segment segmentOne = new Segment.Builder("key1")
            .withLanguages(languages)
            .build();

        Assessment assessment = new Assessment.Builder()
            .withKey("key")
            .withSegments(Collections.singletonList(segmentOne))
            .build();

        Accommodation accommodation = new Accommodation.Builder().build();

        when(assessmentService.findAssessment("key")).thenReturn(Optional.of(assessment));

        when(accommodationsQueryRepository
            .findAccommodationsForNonSegmentedAssessment(isA(String.class), anySetOf(String.class)))
            .thenReturn(Collections.singletonList(accommodation));

        List<Accommodation> accommodations = accommodationsService.findAccommodations("key");

        verify(accommodationsQueryRepository)
            .findAccommodationsForNonSegmentedAssessment(isA(String.class), languageCaptor.capture());

        assertThat(accommodations).containsExactly(accommodation);
        assertThat(languageCaptor.getValue()).containsOnly("ENU", "FRN", "Braille");
    }
}

