package tds.config.web.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.common.web.exceptions.NotFoundException;
import tds.config.ClientSystemFlag;
import tds.config.services.ConfigService;

@RestController
@RequestMapping("/config")
class ConfigController {
    private final ConfigService configService;

    @Autowired
    public ConfigController(final ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping(value = "/client-system-flags/{clientName}/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ClientSystemFlag> getClientSystemFlag(@PathVariable final String clientName, @PathVariable final String type) {
        final ClientSystemFlag clientSystemFlag = configService.findClientSystemFlag(clientName, type)
            .orElseThrow(() -> new NotFoundException("Could not find ClientSystemFlag for client name %s and type %s", clientName, type));

        return ResponseEntity.ok(clientSystemFlag);
    }
}
