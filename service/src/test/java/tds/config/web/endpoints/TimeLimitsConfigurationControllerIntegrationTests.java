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
import tds.config.TimeLimitConfiguration;
import tds.config.services.TimeLimitConfigurationService;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TimeLimitConfigurationController.class)
@Import({ExceptionAdvice.class, SecurityConfiguration.class})
public class TimeLimitsConfigurationControllerIntegrationTests {
    private static final String TIME_LIMITS_RESOURCE = "/config/time-limits/";

    @Autowired
    private MockMvc http;

    @MockBean
    private TimeLimitConfigurationService mockTimeLimitConfigurationService;

    @Test
    public void shouldGetTimeLimitsConfigurationForClientNameAndAssessmentId() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        TimeLimitConfiguration timeLimitConfiguration = new TimeLimitConfiguration.Builder()
            .withAssessmentId(assessmentId)
            .withClientName(clientName)
            .withEnvironment("Development")
            .build();

        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId)).thenReturn(Optional.of(timeLimitConfiguration));

        http.perform(get(String.format("%s/%s/%s", TIME_LIMITS_RESOURCE, clientName, assessmentId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is(clientName)))
            .andExpect(jsonPath("assessmentId", is(assessmentId)))
            .andExpect(jsonPath("environment", is("Development")));
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForClientNameAndNonExistentAssessmentId() throws Exception {
        final String clientName = "SBAC_PT";
        final String assessmentId = "foo";

        TimeLimitConfiguration timeLimitConfiguration = new TimeLimitConfiguration.Builder()
            .withClientName(clientName)
            .withEnvironment("Development")
            .build();

        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId)).thenReturn(Optional.of(timeLimitConfiguration));

        http.perform(get(String.format("%s/%s/%s", TIME_LIMITS_RESOURCE, clientName, assessmentId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is(clientName)))
            .andExpect(jsonPath("assessmentId", is(nullValue())))
            .andExpect(jsonPath("environment", is("Development")));
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForClientName() throws Exception {
        final String clientName = "SBAC_PT";

        TimeLimitConfiguration timeLimitConfiguration = new TimeLimitConfiguration.Builder()
            .withClientName(clientName)
            .withEnvironment("Development")
            .build();

        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName)).thenReturn(Optional.of(timeLimitConfiguration));

        http.perform(get(String.format("%s/%s", TIME_LIMITS_RESOURCE, clientName))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("clientName", is(clientName)))
            .andExpect(jsonPath("assessmentId", is(nullValue())))
            .andExpect(jsonPath("environment", is("Development")));
    }

    @Test
    public void shouldGet404WhenGettingTimeLimitsConfigurationWithInvalidClientNameAndAssessmentId() throws Exception {
        final String clientName = "foo";
        final String assessmentId = "bar";

        when(mockTimeLimitConfigurationService.findTimeLimitConfiguration(clientName, assessmentId)).thenReturn(Optional.empty());

        http.perform(get(String.format("%s/%s/%s", TIME_LIMITS_RESOURCE, clientName, assessmentId))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
