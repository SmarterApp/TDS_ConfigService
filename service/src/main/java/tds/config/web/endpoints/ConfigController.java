package tds.config.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tds.common.web.exceptions.NotFoundException;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.services.ConfigService;
import tds.config.web.resources.ClientSystemFlagResource;
import tds.config.web.resources.ClientTestPropertyResource;


@RestController
@RequestMapping("/config")
public class ConfigController {
    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/client-system-flags/{clientName}/{auditObject}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientSystemFlagResource> getClientSystemFlag(@PathVariable final String clientName, @PathVariable final String auditObject) {
        final ClientSystemFlag clientSystemFlag = configService.findClientSystemFlag(clientName, auditObject)
                .orElseThrow(() -> new NotFoundException("Could not find ClientSystemFlag for client name %s and audit object %s", clientName, auditObject));

        return ResponseEntity.ok(new ClientSystemFlagResource(clientSystemFlag));
    }

    @RequestMapping(value = "/client-test-properties/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientTestPropertyResource> getClientTestProperty(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final ClientTestProperty clientTestProperty = configService.findClientTestProperty(clientName, assessmentId)
                .orElseThrow(() -> new NotFoundException("Could not find ClientTestProperty for client name %s and assessment id %s", clientName, assessmentId));

        return ResponseEntity.ok(new ClientTestPropertyResource(clientTestProperty));
    }
}
