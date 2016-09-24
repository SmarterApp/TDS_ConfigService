package tds.config.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tds.common.web.exceptions.NotFoundException;
import tds.config.TimeLimitConfiguration;
import tds.config.services.TimeLimitConfigurationService;
import tds.config.web.resources.TimeLimitConfigurationResource;

@RestController
@RequestMapping("/config/time-limits")
public class TimeLimitConfigurationController {
    private final TimeLimitConfigurationService timeLimitConfigurationService;

    @Autowired
    public TimeLimitConfigurationController(TimeLimitConfigurationService timeLimitConfigurationService) {
        this.timeLimitConfigurationService = timeLimitConfigurationService;
    }

    @RequestMapping(value = "/{clientName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TimeLimitConfigurationResource> getTimeLimitConfiguration(@PathVariable final String clientName) {
        final TimeLimitConfiguration timeLimitConfiguration = timeLimitConfigurationService.findTimeLimitConfiguration(clientName)
                .orElseThrow(() -> new NotFoundException("Could not find time limit configuration for client name %s", clientName));

        return ResponseEntity.ok(new TimeLimitConfigurationResource(timeLimitConfiguration));
    }

    @RequestMapping(value = "/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TimeLimitConfigurationResource> getTimeLimitConfiguration(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final TimeLimitConfiguration timeLimitConfiguration = timeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId)
                .orElseThrow(() -> new NotFoundException("Could not find time limit configuration for client name %s and assessment id %s", clientName, assessmentId));

        return ResponseEntity.ok(new TimeLimitConfigurationResource(timeLimitConfiguration));
    }
}
