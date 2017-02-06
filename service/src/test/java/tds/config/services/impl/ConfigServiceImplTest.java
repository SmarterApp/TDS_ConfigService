package tds.config.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemFlag;
import tds.config.ClientSystemMessage;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceImplTest {

    private ConfigService configService;

    @Mock
    private ConfigRepository mockConfigRepository;

    @Before
    public void setUp() {
        configService = new ConfigServiceImpl(mockConfigRepository);
    }

    @After
    public void tearDown() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlag() {
        when(mockConfigRepository.findClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag("SBAC_PT", "AUDIT_OBJECT 1");

        assertThat(result.isPresent()).isTrue();

        ClientSystemFlag clientSystemFlag = result.get();
        assertThat(clientSystemFlag.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo("AUDIT_OBJECT 1");
    }

    @Test
    public void shouldReturnOptionalEmptyClientSystemFlagForAnInvalidAuditObject() {
        when(mockConfigRepository.findClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag("SBAC_PT", "foo");

        assertThat(result).isNotPresent();
    }

    private List<ClientSystemFlag> getMockClientSystemFlag() {
        List<ClientSystemFlag> clientSystemFlags = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ClientSystemFlag clientSystemFlag = new ClientSystemFlag.Builder()
                .withClientName("SBAC_PT")
                .withAuditObject("AUDIT_OBJECT " + i)
                .withDescription("unit test description " + i)
                .withEnabled(true)
                .withIsPracticeTest(true)
                .build();

            clientSystemFlags.add(clientSystemFlag);
        }

        return clientSystemFlags;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemMessage Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemMessage() {
        String clientName = "SBAC_PT";
        String messageKey = "some message key";
        String language = "ENU";
        String context = "some message context";
        String subject = "ELA";
        String grade = "7";

        ClientLanguage clientLanguage = new ClientLanguage(clientName, language, true);
        when(mockConfigRepository.findClientLanguage(clientName)).thenReturn(Optional.of(clientLanguage));

        ClientSystemMessage clientSystemMessage = new ClientSystemMessage(123, "Message goes here", language);
        when(mockConfigRepository.findClientSystemMessage(clientName, messageKey, language, clientLanguage.getDefaultLanguage(), context, subject, grade))
            .thenReturn(Optional.of(clientSystemMessage));

        String result = configService.getSystemMessage(clientName, messageKey, language, context, subject, grade);

        assertThat(result).isEqualTo("Message goes here [123]");
    }

    @Test
    public void shouldGetAClientSystemMessageWithClientDefaultLanguage() {
        String clientName = "SBAC_PT";
        String messageKey = "some message key";
        String language = "ESN";
        String context = "some message context";
        String subject = "ELA";
        String grade = "7";

        ClientLanguage clientLanguage = new ClientLanguage(clientName, "ENU", false);
        when(mockConfigRepository.findClientLanguage(clientName)).thenReturn(Optional.of(clientLanguage));

        // When ClientLanguage internationalize is false, the langauge and defaultClientLanguage passed in are the same since it must always use the client default
        ClientSystemMessage clientSystemMessage = new ClientSystemMessage(123, "Message goes here", language);
        when(mockConfigRepository.findClientSystemMessage(clientName, messageKey, clientLanguage.getDefaultLanguage(), clientLanguage.getDefaultLanguage(), context, subject, grade))
            .thenReturn(Optional.of(clientSystemMessage));

        String result = configService.getSystemMessage(clientName, messageKey, language, context, subject, grade);

        assertThat(result).isEqualTo("Message goes here [123]");
    }
}
