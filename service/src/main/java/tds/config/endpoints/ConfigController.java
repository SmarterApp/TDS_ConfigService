package tds.config.endpoints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
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

    @RequestMapping(value = "/isAlive", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<String> isAlive() {
        return ResponseEntity.ok("Hello from Config service");
    }

    @RequestMapping(value = "/clientSystemFlags/{clientName}/{auditObject}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientSystemFlagResource> getClientSystemFlag(@PathVariable final String clientName, @PathVariable final String auditObject) {
        final ClientSystemFlag clientSystemFlag = configService.getClientSystemFlag(clientName, auditObject);

        return ResponseEntity.ok(new ClientSystemFlagResource(clientSystemFlag));
    }

    @RequestMapping(value = "/clientTestProperties/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientTestPropertyResource> getClientTestProperty(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final ClientTestProperty clientTestProperty = configService.getClientTestProperty(clientName, assessmentId);

        return ResponseEntity.ok(new ClientTestPropertyResource(clientTestProperty));
    }
}
