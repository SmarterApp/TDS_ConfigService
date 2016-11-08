package tds.config.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tds.config.Accommodation;
import tds.config.services.AccommodationsService;

@RestController
@RequestMapping("/config/accommodations")
public class AccommodationController {
    private final AccommodationsService accommodationsService;

    @Autowired
    public AccommodationController(AccommodationsService accommodationsService) {
        this.accommodationsService = accommodationsService;
    }

    @GetMapping(value = "/{assessmentKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<Accommodation>> findAccommodations(@PathVariable final String assessmentKey) {
        final List<Accommodation> accommodations = accommodationsService.findAccommodations(assessmentKey);
        return ResponseEntity.ok(accommodations);
    }
}
