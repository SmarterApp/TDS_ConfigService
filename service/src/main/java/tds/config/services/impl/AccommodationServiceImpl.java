package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import tds.assessment.Assessment;
import tds.assessment.Property;
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
        Optional<Assessment> maybeAssessment = assessmentService.findAssessment(assessmentKey);
        if (!maybeAssessment.isPresent()) {
            throw new NotFoundException("Could not find assessment for %s", assessmentKey);
        }

        Assessment assessment = maybeAssessment.get();

        if (assessment.isSegmented()) {
            return accommodationsQueryRepository.findAccommodationsForSegmentedAssessment(assessmentKey);
        } else {
            Set<String> languages = new HashSet<>();
            assessment.getSegments().forEach(segment -> segment.getLanguages()
                .forEach(new Consumer<Property>() {
                    @Override
                    public void accept(Property property) {
                        languages.add(property.getValue());
                    }
                }));

            return accommodationsQueryRepository.findAccommodationsForNonSegmentedAssessment(assessment.getKey(), languages);
        }
    }
}
