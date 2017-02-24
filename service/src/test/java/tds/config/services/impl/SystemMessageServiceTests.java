package tds.config.services.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import tds.config.ClientLanguage;
import tds.config.ClientSystemMessage;
import tds.config.repositories.SystemMessageRepository;
import tds.config.services.SystemMessageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SystemMessageServiceTests {

    private SystemMessageService systemMessageService;

    @Mock
    private SystemMessageRepository mockSystemMessageRepository;

    @Before
    public void setUp() {
        systemMessageService = new SystemMessageServiceImpl(mockSystemMessageRepository);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldGetAClientSystemMessage() {
        String clientName = "SBAC_PT";
        String messageKey = "some message key";
        String languageCode = "ENU";
        String context = "some message context";
        String subject = "ELA";
        String grade = "7";

        ClientLanguage clientLanguage = new ClientLanguage(clientName, languageCode, true);
        when(mockSystemMessageRepository.findClientLanguage(clientName)).thenReturn(Optional.of(clientLanguage));

        ClientSystemMessage clientSystemMessage = new ClientSystemMessage(123, "Message goes here", languageCode);
        when(mockSystemMessageRepository.findClientSystemMessage(clientName, messageKey, languageCode, clientLanguage.getDefaultLanguageCode(), context, subject, grade))
            .thenReturn(Optional.of(clientSystemMessage));

        String result = systemMessageService.getSystemMessage(clientName, messageKey, languageCode, context, subject, grade);

        assertThat(result).isEqualTo("Message goes here [123]");
    }

    @Test
    public void shouldGetAClientSystemMessageWithClientDefaultLanguage() {
        String clientName = "SBAC_PT";
        String messageKey = "some message key";
        String languageCode = "ESN";
        String context = "some message context";
        String subject = "ELA";
        String grade = "7";

        ClientLanguage clientLanguage = new ClientLanguage(clientName, "ENU", false);
        when(mockSystemMessageRepository.findClientLanguage(clientName)).thenReturn(Optional.of(clientLanguage));

        // When ClientLanguage internationalize is false, the languageCode and defaultClientLanguageCode passed in are the same since it must always use the client default
        ClientSystemMessage clientSystemMessage = new ClientSystemMessage(123, "Message goes here", languageCode);
        when(mockSystemMessageRepository.findClientSystemMessage(clientName, messageKey, clientLanguage.getDefaultLanguageCode(), clientLanguage.getDefaultLanguageCode(), context, subject, grade))
            .thenReturn(Optional.of(clientSystemMessage));

        String result = systemMessageService.getSystemMessage(clientName, messageKey, languageCode, context, subject, grade);

        assertThat(result).isEqualTo("Message goes here [123]");
    }
}
