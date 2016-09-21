package tds.config.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.TimeLimits;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

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

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemProperty Tests
    // -----------------------------------------------------------------------------------------------------------------
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
    public void shouldReturnOptionalEmptyClientTestPropertyForAnInvalidClientName() {
        when(mockConfigRepository.getClientTestProperty("SBAC_PT", "TEST_ID")).thenReturn(getMockClientTestProperty());
        when(mockConfigRepository.getClientTestProperty("foo", "TEST_ID")).thenReturn(Optional.empty());

        Optional<ClientTestProperty> result = configService.getClientTestProperty("foo", "TEST_ID");

        assertThat(result).isNotPresent();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
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
    public void shouldReturnOptionalEmptyClientSystemFlagForAnInvalidAuditObject() {
        when(mockConfigRepository.getClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.getClientSystemFlag("SBAC_PT", "foo");

        assertThat(result).isNotPresent();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // TACheckinTime Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetTaCheckInTimeForAClientName() {
        when(mockConfigRepository.getTimeLimits("SBAC_PT")).thenReturn(getMockTimeLimits());

        Optional<Integer> result = configService.getTaCheckInTimeLimit("SBAC_PT");

        assertThat(result).isPresent();
        assertThat(result).isEqualTo(20);
    }

    @Test
    public void shouldReturnOptionalEmptyForTaCheckInWhenClientNameTimeLimitsDoNotExist() {
        when(mockConfigRepository.getTimeLimits("foo")).thenReturn(Optional.empty());

        Optional<Integer> result = configService.getTaCheckInTimeLimit("foo");

        assertThat(result).isNotPresent();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // TimeLimits Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetTimeLimitsForAClientNameAndAssessmentId() {
        when(mockConfigRepository.getTimeLimits("SBAC_PT", "TEST_ID")).thenReturn(getMockTimeLimits());

        Optional<TimeLimits> result = configService.getTimeLimits("SBAC_PT", "TEST_ID");

        assertThat(result).isPresent();
        assertThat(result.get().getAssessmentId()).isNotNull();
        assertThat(result.get().getAssessmentId()).isEqualTo("TEST_ID");
        assertThat(result.get().getEnvironment()).isEqualTo("UNIT_TEST");
        assertThat(result.get().getClientName()).isEqualTo("SBAC_PT");
    }

    @Test
    public void shouldGetTimeLimitsForAClientNameAndNullAssessmentId() {
        when(mockConfigRepository.getTimeLimits("SBAC_PT", "foo")).thenReturn(getMockTimeLimitsWithoutAssessmentId());

        Optional<TimeLimits> result = configService.getTimeLimits("SBAC_PT", "foo");

        assertThat(result).isPresent();
        assertThat(result.get().getAssessmentId()).isNull();
        assertThat(result.get().getEnvironment()).isEqualTo("UNIT_TEST");
        assertThat(result.get().getClientName()).isEqualTo("SBAC_PT");

    }

    @After
    public void Teardown() {
        mockConfigRepository = null;
        configService = null;
    }

    private Optional<ClientTestProperty> getMockClientTestProperty() {
        ClientTestProperty clientTestProperty = new ClientTestProperty();
        clientTestProperty.setClientName("SBAC_PT");
        clientTestProperty.setAssessmentId("TEST_ID");
        clientTestProperty.setAccommodationFamily("accommodation family");
        clientTestProperty.setBatchModeReport(false);
        clientTestProperty.setCategory("category");

        return Optional.of(clientTestProperty);
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

    private Optional<TimeLimits> getMockTimeLimits() {
        TimeLimits timeLimits = new TimeLimits();
        timeLimits.setClientName("SBAC_PT");
        timeLimits.setAssessmentId("TEST_ID");
        timeLimits.setEnvironment("UNIT_TEST");
        timeLimits.setExamRestartWindowMinutes(5);
        timeLimits.setInterfaceTimeoutMinutes(10);
        timeLimits.setExamDelayDays(1);
        timeLimits.setRequestInterfaceTimeoutMinutes(15);
        timeLimits.setTaCheckinTimeMinutes(20);

        return Optional.of(timeLimits);
    }

    private Optional<TimeLimits> getMockTimeLimitsWithoutAssessmentId() {
        TimeLimits timeLimits = new TimeLimits();
        timeLimits.setClientName("SBAC_PT");
        timeLimits.setEnvironment("UNIT_TEST");
        timeLimits.setExamRestartWindowMinutes(5);
        timeLimits.setInterfaceTimeoutMinutes(10);
        timeLimits.setExamDelayDays(1);
        timeLimits.setRequestInterfaceTimeoutMinutes(15);
        timeLimits.setTaCheckinTimeMinutes(20);

        return Optional.of(timeLimits);
    }
}
