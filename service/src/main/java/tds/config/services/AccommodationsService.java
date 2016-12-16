package tds.config.services;

import java.util.List;

import tds.config.Accommodation;

/**
 * Configuration accommodations
 */
public interface AccommodationsService {
    /**
     * Finds the assessment accommodations for an assessment key
     *
     * @param assessmentKey the assessment's key
     * @return list of {@link tds.config.Accommodation}
     */
    List<Accommodation> findAccommodationsByAssessmentKey(String assessmentKey);

    /**
     * Finds the assessment accommodations for an assessment id
     *
     * @param clientName   the client name
     * @param assessmentId the id for the assessment
     * @return
     */
    List<Accommodation> findAccommodationsByAssessmentId(String clientName, String assessmentId);
}
