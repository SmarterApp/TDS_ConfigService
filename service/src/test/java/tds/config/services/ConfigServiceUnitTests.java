package tds.config.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.ConfigServiceApplication;
import tds.config.repositories.ConfigRepository;
import tds.config.services.impl.ConfigServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

        ClientTestProperty result = configService.getClientTestProperty("SBAC_PT", "TEST_ID");

        assertThat(result).isNotNull();
        assertThat(result.getClientName()).isEqualTo("SBAC_PT");
        assertThat(result.getAssessmentId()).isEqualTo("TEST_ID");
        assertThat(result.getAccommodationFamily()).isEqualTo("accommodation family");
        assertThat(result.getBatchModeReport()).isEqualTo(false);
        assertThat(result.getCategory()).isEqualTo("category");
    }

    @Test
    public void should_Get_a_ClientTestProperty() {
        shouldGetAClientTestProperty();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementExceptionForInvalidClientName() {
        when(mockConfigRepository.getClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());

        configService.getClientTestProperty("foo", "TEST_ID");
    }

    @Test(expected = NoSuchElementException.class)
    public void should_Throw_NoSuchElement_Exception_For_Invalid_ClientName() {
        shouldThrowNoSuchElementExceptionForInvalidClientName();
    }

    @Test
    public void shouldGetAClientSystemFlag() {
        when(mockConfigRepository.getClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        ClientSystemFlag result = configService.getClientSystemFlag("SBAC_PT", "AUDIT_OBJECT 1");

        assertThat(result).isNotNull();
        assertThat(result.getClientName()).isEqualTo("SBAC_PT");
        assertThat(result.getAuditObject()).isEqualTo("AUDIT_OBJECT 1");
    }

    @Test
    public void should_Get_a_ClientSystemFlag() {
        shouldGetAClientSystemFlag();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowNoSuchElementExceptionForAnInvalidAuditObject() {
        when(mockConfigRepository.getClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        configService.getClientSystemFlag("SBAC_PT", "foo");
    }

    @Test(expected = NoSuchElementException.class)
    public void should_Throw_NoSuchElementException_For_Invalid_AuditObject() {
        shouldThrowNoSuchElementExceptionForAnInvalidAuditObject();
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
