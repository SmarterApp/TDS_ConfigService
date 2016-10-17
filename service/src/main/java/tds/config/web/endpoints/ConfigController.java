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

import java.util.List;

import tds.common.web.exceptions.NotFoundException;
import tds.config.AssessmentWindow;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.model.AssessmentWindowParameters;
import tds.config.services.ConfigService;

@RestController
@RequestMapping("/config")
class ConfigController {
    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping(value = "/client-system-flags/{clientName}/{auditObject}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ClientSystemFlag> getClientSystemFlag(@PathVariable final String clientName, @PathVariable final String auditObject) {
        final ClientSystemFlag clientSystemFlag = configService.findClientSystemFlag(clientName, auditObject)
                .orElseThrow(() -> new NotFoundException("Could not find ClientSystemFlag for client name %s and audit object %s", clientName, auditObject));

        return ResponseEntity.ok(clientSystemFlag);
    }

    @GetMapping(value = "/client-test-properties/{clientName}/{assessmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<ClientTestProperty> getClientTestProperty(@PathVariable final String clientName, @PathVariable final String assessmentId) {
        final ClientTestProperty clientTestProperty = configService.findClientTestProperty(clientName, assessmentId)
            .orElseThrow(() -> new NotFoundException("Could not find ClientTestProperty for client name %s and assessment id %s", clientName, assessmentId));

        return ResponseEntity.ok(clientTestProperty);
    }

    @GetMapping(value = "/assessment-windows/{clientName}/{assessmentId}/{sessionType}/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<AssessmentWindow>> findAssessmentWindows(@PathVariable final String clientName,
                                                                 @PathVariable final String assessmentId,
                                                                 @PathVariable final int sessionType,
                                                                 @PathVariable final long studentId,
                                                                 @RequestParam(required = false) final int shiftWindowStart,
                                                                 @RequestParam(required = false) final int shiftWindowEnd,
                                                                 @RequestParam(required = false) final int shiftFormStart,
                                                                 @RequestParam(required = false) final int shiftFormEnd,
                                                                 @RequestParam(required = false) final String formList
                                                                ) {
        AssessmentWindowParameters assessmentWindowParameters = new AssessmentWindowParameters
            .Builder(studentId, clientName, assessmentId, sessionType)
            .withShiftWindowStart(shiftWindowStart)
            .withShiftWindowEnd(shiftWindowEnd)
            .withShiftFormStart(shiftFormStart)
            .withShiftFormEnd(shiftFormEnd)
            .withFormList(formList)
            .build();

        return ResponseEntity.ok(configService.findAssessmentWindows(assessmentWindowParameters));
    }
}
