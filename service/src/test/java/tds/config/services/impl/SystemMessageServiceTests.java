/***************************************************************************************************
 * Copyright 2017 Regents of the University of California. Licensed under the Educational
 * Community License, Version 2.0 (the “license”); you may not use this file except in
 * compliance with the License. You may obtain a copy of the license at
 *
 * https://opensource.org/licenses/ECL-2.0
 *
 * Unless required under applicable law or agreed to in writing, software distributed under the
 * License is distributed in an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for specific language governing permissions
 * and limitations under the license.
 **************************************************************************************************/

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
