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
import tds.config.ClientSystemFlag;
import tds.config.services.ConfigService;
import tds.config.web.resources.ClientSystemFlagResource;


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
}
