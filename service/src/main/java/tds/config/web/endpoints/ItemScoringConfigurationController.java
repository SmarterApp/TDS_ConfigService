/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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
