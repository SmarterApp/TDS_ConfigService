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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.common.web.exceptions.NotFoundException;
import tds.config.TimeLimitConfiguration;
import tds.config.services.TimeLimitConfigurationService;

@RestController
@RequestMapping("/config/time-limits")
class TimeLimitConfigurationController {
    private final TimeLimitConfigurationService timeLimitConfigurationService;

    @Autowired
    public TimeLimitConfigurationController(final TimeLimitConfigurationService timeLimitConfigurationService) {
        this.timeLimitConfigurationService = timeLimitConfigurationService;
    }

    @RequestMapping(value = "/{clientName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<TimeLimitConfiguration> getTimeLimitConfiguration(@PathVariable final String clientName) {
        final TimeLimitConfiguration timeLimitConfiguration = timeLimitConfigurationService.findTimeLimitConfiguration(clientName)
            .orElseThrow(() -> new NotFoundException("Could not find time limit configuration for client name %s", clientName));

        return ResponseEntity.ok(timeLimitConfiguration);
    }

    @RequestMapping(value = "/{clientName}/{assessmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<TimeLimitConfiguration> getTimeLimitConfiguration(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final TimeLimitConfiguration timeLimitConfiguration = timeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId)
            .orElseThrow(() -> new NotFoundException("Could not find time limit configuration for client name %s and assessment id %s", clientName, assessmentId));

        return ResponseEntity.ok(timeLimitConfiguration);
    }
}
