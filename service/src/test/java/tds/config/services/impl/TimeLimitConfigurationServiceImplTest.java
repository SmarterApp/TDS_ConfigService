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

import tds.config.TimeLimitConfiguration;
import tds.config.repositories.TimeLimitConfigurationRepository;
import tds.config.services.TimeLimitConfigurationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeLimitConfigurationServiceImplTest {

    private TimeLimitConfigurationServiceImpl timeLimitConfigurationService;

    @Mock
    private TimeLimitConfigurationRepository mockTimeLimitConfigurationRepository;

    @Before
    public void setUp() {
        this.timeLimitConfigurationService = new TimeLimitConfigurationServiceImpl(mockTimeLimitConfigurationRepository);
    }

    @After
    public void tearDown() {
        this.timeLimitConfigurationService = null;
        this.mockTimeLimitConfigurationRepository = null;
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForAClientNameAndAssessmentId() {
        when(mockTimeLimitConfigurationRepository.findTimeLimitConfiguration("SBAC_PT", "TEST_ID"))
                .thenReturn(getMockTimeLimits());

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationService.findTimeLimitConfiguration("SBAC_PT", "TEST_ID");

        assertThat(result).isPresent();
        assertThat(result.get().getAssessmentId()).isNotNull();
        assertThat(result.get().getAssessmentId()).isEqualTo("TEST_ID");
        assertThat(result.get().getEnvironment()).isEqualTo("UNIT_TEST");
        assertThat(result.get().getClientName()).isEqualTo("SBAC_PT");
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForAClientNameAndNullAssessmentId() {
        when(mockTimeLimitConfigurationRepository.findTimeLimitConfiguration("SBAC_PT", "foo"))
                .thenReturn(getMockTimeLimitsWithoutAssessmentId());

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationService.findTimeLimitConfiguration("SBAC_PT", "foo");

        assertThat(result).isPresent();
        assertThat(result.get().getAssessmentId()).isNull();
        assertThat(result.get().getEnvironment()).isEqualTo("UNIT_TEST");
        assertThat(result.get().getClientName()).isEqualTo("SBAC_PT");
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForAClientName() {
        when(mockTimeLimitConfigurationRepository.findTimeLimitConfiguration("SBAC_PT"))
                .thenReturn(getMockTimeLimitsWithoutAssessmentId());

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationService.findTimeLimitConfiguration("SBAC_PT");

        assertThat(result).isPresent();
        assertThat(result.get().getAssessmentId()).isNull();
        assertThat(result.get().getEnvironment()).isEqualTo("UNIT_TEST");
        assertThat(result.get().getClientName()).isEqualTo("SBAC_PT");
    }

    private Optional<TimeLimitConfiguration> getMockTimeLimits() {
        TimeLimitConfiguration timeLimits = new TimeLimitConfiguration.Builder()
                .withClientName("SBAC_PT")
                .withAssessmentId("TEST_ID")
                .withEnvironment("UNIT_TEST")
                .withExamRestartWindowMinutes(5)
                .withInterfaceTimeoutMinutes(10)
                .withExamDelayDays(1)
                .withRequestInterfaceTimeoutMinutes(15)
                .withTaCheckinTimeMinutes(20)
                .build();

        return Optional.of(timeLimits);
    }

    private Optional<TimeLimitConfiguration> getMockTimeLimitsWithoutAssessmentId() {
        TimeLimitConfiguration timeLimits = new TimeLimitConfiguration.Builder()
                .withClientName("SBAC_PT")
                .withEnvironment("UNIT_TEST")
                .withExamRestartWindowMinutes(5)
                .withInterfaceTimeoutMinutes(10)
                .withExamDelayDays(1)
                .withRequestInterfaceTimeoutMinutes(15)
                .withTaCheckinTimeMinutes(20)
                .build();

        return Optional.of(timeLimits);
    }
}
