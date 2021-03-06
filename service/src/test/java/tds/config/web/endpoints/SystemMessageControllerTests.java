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

package tds.config.web.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import tds.config.services.SystemMessageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SystemMessageControllerTests {
    private SystemMessageController systemMessageController;
    private SystemMessageService mockSystemMessageService;

    @Before
    public void setUp() {
        HttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        mockSystemMessageService = mock(SystemMessageService.class);
        systemMessageController = new SystemMessageController(mockSystemMessageService);
    }

    @Test
    public void shouldGetClientSystemMessage() {
        String clientName = "UNIT_TEST";
        String messageKey = "unit test";
        String languageCode = "Unit Language";
        String context = "Unit context";
        String subject = "Unit subject";
        String grade = "Unit grade";
        when(mockSystemMessageService.getSystemMessage(clientName, messageKey, languageCode, context, subject, grade))
            .thenReturn("Mocked message");

        ResponseEntity<String> response = systemMessageController.getClientSystemMessage(clientName, languageCode, context, messageKey, subject, grade);

        verify(mockSystemMessageService).getSystemMessage(clientName, messageKey, languageCode, context, subject, grade);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Mocked message");
    }
}
