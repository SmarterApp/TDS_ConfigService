package tds.config.services;

import java.util.List;

import tds.config.Accommodation;

public interface AccommodationsService {
    List<Accommodation> findAccommodations(String assessmentKey);
}
