package tds.config.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.repositories.ConfigRepository;
import tds.config.services.impl.ConfigServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceUnitTests {

    private ConfigService configService;

    @Mock
    private ConfigRepository mockConfigRepository;

    @Before
    public void Setup() {
        configService = new ConfigServiceImpl(mockConfigRepository);
    }

    @Test
    public void shouldGetAClientTestProperty() {
        when(mockConfigRepository.getClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());

        Optional<ClientTestProperty> result = configService.getClientTestProperty("SBAC_PT", "TEST_ID");

        assertThat(result.isPresent()).isTrue();

        ClientTestProperty clientTestProperty = result.get();
        assertThat(clientTestProperty.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientTestProperty.getAssessmentId()).isEqualTo("TEST_ID");
        assertThat(clientTestProperty.getAccommodationFamily()).isEqualTo("accommodation family");
        assertThat(clientTestProperty.getBatchModeReport()).isEqualTo(false);
        assertThat(clientTestProperty.getCategory()).isEqualTo("category");
    }

    @Test
    public void shouldNotBePresentForAnInvalidClientName() {
        when(mockConfigRepository.getClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());

        Optional<ClientTestProperty> result = configService.getClientTestProperty("foo", "TEST_ID");

        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldGetAClientSystemFlag() {
        when(mockConfigRepository.getClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.getClientSystemFlag("SBAC_PT", "AUDIT_OBJECT 1");

        assertThat(result.isPresent()).isTrue();

        ClientSystemFlag clientSystemFlag = result.get();
        assertThat(clientSystemFlag.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo("AUDIT_OBJECT 1");
    }

    @Test
    public void shouldNotBePresentForAnInvalidAuditObject() {
        when(mockConfigRepository.getClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.getClientSystemFlag("SBAC_PT", "foo");

        assertThat(result.isPresent()).isFalse();
    }

    @After
    public void Teardown() {
        mockConfigRepository = null;
        configService = null;
    }

    private ClientTestProperty getMockClientTestProperty() {
        ClientTestProperty clientTestProperty = new ClientTestProperty();
        clientTestProperty.setClientName("SBAC_PT");
        clientTestProperty.setAssessmentId("TEST_ID");
        clientTestProperty.setAccommodationFamily("accommodation family");
        clientTestProperty.setBatchModeReport(false);
        clientTestProperty.setCategory("category");

        return clientTestProperty;
    }

    private List<ClientSystemFlag> getMockClientSystemFlag() {
        List<ClientSystemFlag> clientSystemFlags = new ArrayList<>();

        for (Integer i = 0; i < 5; i++) {
            ClientSystemFlag clientSystemFlag = new ClientSystemFlag();
            clientSystemFlag.setClientName("SBAC_PT");
            clientSystemFlag.setAuditObject("AUDIT_OBJECT " + i.toString());
            clientSystemFlag.setDescription("unit test description " + i.toString());
            clientSystemFlag.setIsOn(true);
            clientSystemFlag.setIsPracticeTest(true);

            clientSystemFlags.add(clientSystemFlag);
        }

        return clientSystemFlags;
    }
}
