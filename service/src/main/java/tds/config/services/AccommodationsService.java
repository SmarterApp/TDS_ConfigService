package tds.config.services;

import java.util.List;

import tds.config.Accommodation;

/**
 * Configuration accommodations
 */
public interface AccommodationsService {
    /**
     * Finds the configuration accommodations for an assessment key
     *
     * @param assessmentKey the assessment's key
     * @return list of {@link tds.config.Accommodation}
     */
    List<Accommodation> findAccommodations(String assessmentKey);
}
