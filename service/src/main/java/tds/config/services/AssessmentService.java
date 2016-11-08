package tds.config.services;

import java.util.Optional;

import tds.assessment.Assessment;

/**
 * Service handling assessment lookup
 */
public interface AssessmentService {
    /**
     * Finds an assessment by key
     *
     * @param assessmentKey assessment key
     * @return an {@link tds.assessment.Assessment} if found otherwise empty
     */
    Optional<Assessment> findAssessment(String assessmentKey);
}
