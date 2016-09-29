package tds.config.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.common.web.exceptions.NotFoundException;
import tds.config.ClientTestProperty;
import tds.config.services.ConfigService;
import tds.config.web.resources.ClientTestPropertyResource;

@RestController
@RequestMapping("/config/client-test-properties")
public class ClientTestPropertyController {
    private final ConfigService configService;

    @Autowired
    public ClientTestPropertyController(ConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(value = "/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ClientTestPropertyResource> getClientTestProperty(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final ClientTestProperty clientTestProperty = configService.findClientTestProperty(clientName, assessmentId)
            .orElseThrow(() -> new NotFoundException("Could not find ClientTestProperty for client name %s and assessment id %s", clientName, assessmentId));

        return ResponseEntity.ok(new ClientTestPropertyResource(clientTestProperty));
    }
}
