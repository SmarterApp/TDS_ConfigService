package tds.config.repositories;

import java.util.List;
import java.util.Set;

import tds.config.Accommodation;

/**
 * Data access repository for accommodations
 */
public interface AccommodationsQueryRepository {
    /**
     * Finds the accommodations for a segmented assessment
     *
     * @param assessmentKey the assessment key for lookup
     * @return List of {@link tds.config.Accommodation}
     */
    List<Accommodation> findAccommodationsForSegmentedAssessment(String assessmentKey);

    /**
     * Finds the accommodations for an assessment without segments
     *
     * @param assessmentKey the key for the assessment
     * @param languageCodes the included language codes for the accommodations
     * @return List of {@link tds.config.Accommodation}
     */
    List<Accommodation> findAccommodationsForNonSegmentedAssessment(String assessmentKey, Set<String> languageCodes);
}
