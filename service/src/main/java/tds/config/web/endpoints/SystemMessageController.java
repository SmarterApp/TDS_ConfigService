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
