package tds.config.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.exceptions.NotFoundException;
import tds.config.services.ConfigService;
import tds.config.web.resources.ClientSystemFlagResource;
import tds.config.web.resources.ClientTestPropertyResource;


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
    public ResponseEntity<ClientSystemFlagResource> getClientSystemFlag(@PathVariable final String clientName, @PathVariable final String auditObject) throws NotFoundException {
        final ClientSystemFlag clientSystemFlag = configService.getClientSystemFlag(clientName, auditObject)
                .orElseThrow(() -> new NotFoundException("Could not find ClientSystemFlag for client name " + clientName + " and audit object " + auditObject));

        return ResponseEntity.ok(new ClientSystemFlagResource(clientSystemFlag));
    }

    @RequestMapping(value = "/clientTestProperties/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientTestPropertyResource> getClientTestProperty(@PathVariable final String clientName, @PathVariable final String assessmentId) throws NotFoundException {
        final ClientTestProperty clientTestProperty = configService.getClientTestProperty(clientName, assessmentId)
                .orElseThrow(() -> new NotFoundException("Could not find ClientTestProperty for " + clientName + " and assessmentId " + assessmentId));

        return ResponseEntity.ok(new ClientTestPropertyResource(clientTestProperty));
    }
}
