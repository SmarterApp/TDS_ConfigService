package tds.config.web.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tds.common.web.exceptions.NotFoundException;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.TimeLimits;
import tds.config.services.ConfigService;
import tds.config.web.resources.ClientSystemFlagResource;
import tds.config.web.resources.ClientTestPropertyResource;
import tds.config.web.resources.TaCheckinTimeResource;
import tds.config.web.resources.TimeLimitsResource;


@RestController
@RequestMapping("/config")
public class ConfigController {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigController.class);
    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/clientSystemFlags/{clientName}/{auditObject}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientSystemFlagResource> getClientSystemFlag(@PathVariable final String clientName, @PathVariable final String auditObject) {
        final ClientSystemFlag clientSystemFlag = configService.getClientSystemFlag(clientName, auditObject)
                .orElseThrow(() -> new NotFoundException("Could not find ClientSystemFlag for client name %s and audit object %s", clientName, auditObject));

        return ResponseEntity.ok(new ClientSystemFlagResource(clientSystemFlag));
    }

    @RequestMapping(value = "/clientTestProperties/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientTestPropertyResource> getClientTestProperty(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final ClientTestProperty clientTestProperty = configService.getClientTestProperty(clientName, assessmentId)
                .orElseThrow(() -> new NotFoundException("Could not find ClientTestProperty for client name %s and assessment id %s", clientName, assessmentId));

        return ResponseEntity.ok(new ClientTestPropertyResource(clientTestProperty));
    }

    @RequestMapping(value = "/timelimits/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TimeLimitsResource> getTimeLimits(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final TimeLimits timeLimits = configService.getTimeLimits(clientName, assessmentId)
                .orElseThrow(() -> new NotFoundException("Could not find TimeLimits for client name %s and assessment id %s", clientName, assessmentId));

        return ResponseEntity.ok(new TimeLimitsResource(timeLimits));
    }

    @RequestMapping(value = "/timelimits/{clientName}/checkin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TaCheckinTimeResource> getTaCheckInTimeMinutes(@PathVariable final String clientName) {
        final Integer checkInTime = configService.getTaCheckInTimeLimit(clientName)
                .orElseThrow(() -> new NotFoundException("Could not find TA Check-In Time value for client name %s", clientName));

        return ResponseEntity.ok(new TaCheckinTimeResource(checkInTime));
    }
}
