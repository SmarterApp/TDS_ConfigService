package tds.config.web.endpoints;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import tds.config.services.ItemScoringConfigurationService;
import tds.student.sql.data.ItemScoringConfig;

@RestController
@RequestMapping("/config")
public class ItemScoringConfigurationController {
    private final ItemScoringConfigurationService itemScoringConfigurationService;

    public ItemScoringConfigurationController(final ItemScoringConfigurationService itemScoringConfigurationService) {
        this.itemScoringConfigurationService = itemScoringConfigurationService;
    }

    @GetMapping(value = "{clientName}/scoring", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<ItemScoringConfig>> findScoringConfigurations(@PathVariable final String clientName,
                                                                      @RequestParam(required = false) final String site,
                                                                      @RequestParam(required = false) final String server) {
        final List<ItemScoringConfig> scoringConfigs = itemScoringConfigurationService.findItemScoringConfigs(clientName, site, server);

        if(scoringConfigs.isEmpty()) {
            return new ResponseEntity<>(scoringConfigs, HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(scoringConfigs);
    }
}
