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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tds.config.services.SystemMessageService;

@RestController
@RequestMapping("/config/messages")
class SystemMessageController {
    private final SystemMessageService systemMessageService;

    @Autowired
    public SystemMessageController(final SystemMessageService systemMessageService) {
        this.systemMessageService = systemMessageService;
    }

    @GetMapping(value = "/{clientName}/{context}/{messageKey}/{languageCode}", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    ResponseEntity<String> getClientSystemMessage(@PathVariable final String clientName,
                                                  @PathVariable final String languageCode,
                                                  @PathVariable final String context,
                                                  @PathVariable final String messageKey,
                                                  @RequestParam(required = false) final String subject,
                                                  @RequestParam(required = false) final String grade) {
        return ResponseEntity.ok(
            systemMessageService.getSystemMessage(clientName, messageKey, languageCode, context, subject, grade)
        );
    }
}
