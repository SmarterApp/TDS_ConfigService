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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

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

    @GetMapping(value = "/client-test-properties/{clientName}/forceComplete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<Collection<String>> findForceCompleteAssessmentIds(@PathVariable final String clientName) {
        return ResponseEntity.ok(configService.findForceCompleteAssessmentIds(clientName));
    }
}
