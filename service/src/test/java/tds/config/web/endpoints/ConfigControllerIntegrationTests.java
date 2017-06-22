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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import tds.common.configuration.SecurityConfiguration;
import tds.common.web.advice.ExceptionAdvice;
import tds.config.ClientSystemFlag;
import tds.config.services.ConfigService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class})
public class ConfigControllerIntegrationTests {
    @Autowired
    private MockMvc http;

    @MockBean
    private ConfigService mockConfigService;

    @Test
    public void shouldGetAClientSystemFlag() throws Exception {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        ClientSystemFlag flag = new ClientSystemFlag.Builder()
            .withAuditObject(auditObject)
            .withClientName(clientName)
            .withEnabled(true)
            .withIsPracticeTest(true)
            .build();

        when(mockConfigService.findClientSystemFlag(clientName, auditObject)).thenReturn(Optional.of(flag));

        http.perform(get("/config/client-system-flags/" + clientName + "/" + auditObject)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is(clientName)))
            .andExpect(jsonPath("auditObject", is(auditObject)))
            .andExpect(jsonPath("enabled", is(true)))
            .andExpect(jsonPath("isPracticeTest", is(true)));
    }

    @Test
    public void shouldReturn404WhenGettingClientSystemFlagWithAnInvalidAuditObject() throws Exception {
        final String clientName = "SBAC_PT";
        final String auditObject = "accommodations";

        when(mockConfigService.findClientSystemFlag(clientName, auditObject)).thenReturn(Optional.empty());

        http.perform(get("/config/client-system-flags/" + clientName + "/" + auditObject)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
