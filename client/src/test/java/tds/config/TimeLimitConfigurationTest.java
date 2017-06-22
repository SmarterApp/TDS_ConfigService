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

package tds.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeLimitConfigurationTest {
    @Test
    public void shouldBuildATimeLimitConfiguration() {
        TimeLimitConfiguration timeLimitConfiguration = new TimeLimitConfiguration.Builder()
                .withTaCheckinTimeMinutes(10)
                .withAssessmentId("Unit Test Assessment")
                .withClientName("SBAC_PT")
                .withEnvironment("unit test")
                .withExamDelayDays(1)
                .withExamRestartWindowMinutes(20)
                .withInterfaceTimeoutMinutes(10)
                .withRequestInterfaceTimeoutMinutes(15)
                .build();

        assertThat(timeLimitConfiguration.getTaCheckinTimeMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getAssessmentId()).isEqualTo("Unit Test Assessment");
        assertThat(timeLimitConfiguration.getClientName()).isEqualTo("SBAC_PT");
        assertThat(timeLimitConfiguration.getEnvironment()).isEqualTo("unit test");
        assertThat(timeLimitConfiguration.getExamDelayDays()).isEqualTo(1);
        assertThat(timeLimitConfiguration.getExamRestartWindowMinutes()).isEqualTo(20);
        assertThat(timeLimitConfiguration.getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(timeLimitConfiguration.getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
    }
}
