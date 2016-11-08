package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import tds.assessment.Assessment;
import tds.config.configuration.ConfigServiceProperties;
import tds.config.services.AssessmentService;

@Service
public class AssessmentServiceImpl implements AssessmentService {
    private final RestTemplate restTemplate;
    private final ConfigServiceProperties properties;

    @Autowired
    public AssessmentServiceImpl(RestTemplate restTemplate, ConfigServiceProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public Optional<Assessment> findAssessment(String assessmentKey) {
        UriComponentsBuilder builder =
            UriComponentsBuilder
                .fromHttpUrl(String.format("%s/%s", properties.getAssessmentUrl(), assessmentKey));

        Optional<Assessment> maybeAssessment = Optional.empty();
        try {
            final Assessment assessment = restTemplate.getForObject(builder.toUriString(), Assessment.class);
            maybeAssessment = Optional.of(assessment);
        } catch (HttpClientErrorException hce) {
            if (hce.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw hce;
            }
        }

        return maybeAssessment;
    }
}
