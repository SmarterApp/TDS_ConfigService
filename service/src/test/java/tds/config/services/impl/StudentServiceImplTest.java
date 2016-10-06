package tds.config.services.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import tds.config.configuration.ConfigServiceProperties;
import tds.config.services.StudentService;
import tds.student.RtsStudentPackageAttribute;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceImplTest {
    @Mock
    private RestTemplate restTemplate;

    private StudentService studentService;

    @Before
    public void setUp() {
        ConfigServiceProperties properties = new ConfigServiceProperties();
        properties.setStudentUrl("http://localhost:8080/students");
        studentService = new StudentServiceImpl(properties, restTemplate);
    }

    @Test
    public void shouldFindRtsAttributeByClientStudentAndName() {
        RtsStudentPackageAttribute attribute = new RtsStudentPackageAttribute("name", "value");

        when(restTemplate.getForObject("http://localhost:8080/students/1/rts/SBAC/test", RtsStudentPackageAttribute.class))
            .thenReturn(attribute);

        Optional<RtsStudentPackageAttribute> maybeAttribute = studentService.findRtsStudentPackageAttribute("SBAC", 1, "test");
        verify(restTemplate).getForObject("http://localhost:8080/students/1/rts/SBAC/test", RtsStudentPackageAttribute.class);

        assertThat(maybeAttribute.get()).isEqualTo(attribute);
    }

    @Test
    public void shouldReturnEmptyWhenRtsAttributeNotFound() {
        when(restTemplate.getForObject("http://localhost:8080/students/1/rts/SBAC/test", RtsStudentPackageAttribute.class))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        Optional<RtsStudentPackageAttribute> maybeAttribute = studentService.findRtsStudentPackageAttribute("SBAC", 1, "test");
        verify(restTemplate).getForObject("http://localhost:8080/students/1/rts/SBAC/test", RtsStudentPackageAttribute.class);

        assertThat(maybeAttribute).isNotPresent();
    }

    @Test (expected = RestClientException.class)
    public void shouldThrowIfUnexpectedErrorFindingRtsStudentPackageAttribute() {
        when(restTemplate.getForObject("http://localhost:8080/students/1/rts/SBAC/test", RtsStudentPackageAttribute.class)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        studentService.findRtsStudentPackageAttribute("SBAC", 1, "test");
    }
}
