package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import tds.assessment.Assessment;
import tds.assessment.ItemProperty;
import tds.common.web.exceptions.NotFoundException;
import tds.config.Accommodation;
import tds.config.repositories.AccommodationsQueryRepository;
import tds.config.services.AccommodationsService;
import tds.config.services.AssessmentService;

@Service
public class AccommodationServiceImpl implements AccommodationsService {
    private final AssessmentService assessmentService;
    private final AccommodationsQueryRepository accommodationsQueryRepository;

    @Autowired
    public AccommodationServiceImpl(AssessmentService assessmentService, AccommodationsQueryRepository accommodationsQueryRepository) {
        this.assessmentService = assessmentService;
        this.accommodationsQueryRepository = accommodationsQueryRepository;
    }

    @Override
    public List<Accommodation> findAccommodations(String assessmentKey) {
        //Implements the replacement for CommonDLL.TestKeyAccommodations_FN
        Optional<Assessment> maybeAssessment = assessmentService.findAssessment(assessmentKey);
        if (!maybeAssessment.isPresent()) {
            throw new NotFoundException("Could not find assessment for %s", assessmentKey);
        }

        Assessment assessment = maybeAssessment.get();

        if (assessment.isSegmented()) {
            return accommodationsQueryRepository.findAccommodationsForSegmentedAssessment(assessmentKey);
        } else {
            Set<String> languages =
                assessment.getSegments().stream()
                    .flatMap(segment -> segment.getItems().stream()
                        .flatMap(item -> item.getItemProperties().stream()
                            .filter(itemProperty -> itemProperty.getName().equalsIgnoreCase(Accommodation.ACCOMMODATION_TYPE_LANGUAGE)))
                        .map(itemProperty -> itemProperty.getValue()))
                    .collect(Collectors.toSet());


            return accommodationsQueryRepository.findAccommodationsForNonSegmentedAssessment(assessment.getKey(), languages);
        }
    }
}
