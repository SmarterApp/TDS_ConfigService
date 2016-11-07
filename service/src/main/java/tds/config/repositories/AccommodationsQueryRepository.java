package tds.config.repositories;

import java.util.List;
import java.util.Set;

import tds.config.Accommodation;

public interface AccommodationsQueryRepository {
    List<Accommodation> findAssessmentAccommodations(String assessmentKey, boolean segmented, Set<String> languageCodes);
}
