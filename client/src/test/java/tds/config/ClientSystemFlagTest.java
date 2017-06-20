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

import org.joda.time.Instant;
import org.joda.time.Minutes;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientSystemFlagTest {
    @Test
    public void shouldBuildAClientSystemFlag() {
        Instant utcNowMinusFiveMinutes = Instant.now().minus(Minutes.minutes(5).toStandardDuration());

        ClientSystemFlag clientSystemFlag = new ClientSystemFlag.Builder()
                .withClientName("SBAC_PT")
                .withIsPracticeTest(true)
                .withEnabled(true)
                .withDescription("Unit test description")
                .withAuditObject("Unit test audit object")
                .withDateChanged(utcNowMinusFiveMinutes)
                .withDatePublished(utcNowMinusFiveMinutes)
                .build();

        assertThat(clientSystemFlag.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientSystemFlag.getIsPracticeTest()).isTrue();
        assertThat(clientSystemFlag.isEnabled()).isTrue();
        assertThat(clientSystemFlag.getDescription()).isEqualTo("Unit test description");
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo("Unit test audit object");
        assertThat(clientSystemFlag.getDateChanged()).isEqualTo(utcNowMinusFiveMinutes);
        assertThat(clientSystemFlag.getDatePublished()).isEqualTo(utcNowMinusFiveMinutes);
    }
}
