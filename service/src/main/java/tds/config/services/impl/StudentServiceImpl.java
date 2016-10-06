package tds.config.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import tds.config.configuration.ConfigServiceProperties;
import tds.config.services.StudentService;
import tds.student.RtsStudentPackageAttribute;

@Service
public class StudentServiceImpl implements StudentService {
    private final RestTemplate restTemplate;
    private final ConfigServiceProperties properties;

    @Autowired
    public StudentServiceImpl(ConfigServiceProperties properties,
                              RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @Override
    public Optional<RtsStudentPackageAttribute> findRtsStudentPackageAttribute(String clientName, long studentId, String attributeName) {
        UriComponentsBuilder builder =
            UriComponentsBuilder
                .fromHttpUrl(String.format("%s%s/rts/%s/%s", properties.getStudentUrl(), studentId, clientName, attributeName));

        Optional<RtsStudentPackageAttribute> maybePackageAttribute = Optional.empty();
        try {
            final RtsStudentPackageAttribute packageAttribute = restTemplate.getForObject(builder.toUriString(), RtsStudentPackageAttribute.class);
            maybePackageAttribute = Optional.of(packageAttribute);
        } catch (HttpClientErrorException hce) {
            if (hce.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw hce;
            }
        }

        return maybePackageAttribute;
    }
}
