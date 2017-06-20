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

package tds.config.repositories.impl;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import tds.config.TimeLimitConfiguration;
import tds.config.repositories.TimeLimitConfigurationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TimeLimitConfigurationRepositoryImplIntegrationTests {

    @Autowired
    private TimeLimitConfigurationRepository timeLimitConfigurationRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        String timeLimitsInsert = "INSERT INTO client_timelimits VALUES (UNHEX('0AEFBFBD6C362DEFBFBD49EFBFBDEFBF'),NULL,1,10,-1,10,'SBAC_PT',1,30,20,20,'2012-12-21 00:02:53.000','2012-12-21 00:02:53.000','Development',8,15,2),\n" +
            "(UNHEX('C3AD5010256F4F2AA68653A0CF56CF55'),'SBAC Math 3-MATH-3',1,10,-1,10,'SBAC_PT',1,30,20,50,'2012-12-21 00:02:53.000','2012-12-21 00:02:53.000','Development',8,15,2),\n" +
            "(UNHEX('0B6CEFBFBDEFBFBD6D064042EFBFBD7D'),NULL,1,10,-1,15,'SBAC',0,20,20,20,'2010-07-07 15:39:31.433',NULL,'Development',8,120,2);";

        jdbcTemplate.update(timeLimitsInsert, new MapSqlParameterSource());
    }

    @After
    public void tearDown(){
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForAClientName() {
        final String clientName = "SBAC_PT";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);

        Assertions.assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo(clientName);
        assertThat(result.get().getAssessmentId()).isNull();
        assertThat(result.get().getEnvironment()).isEqualTo("Development");
        assertThat(result.get().getExamRestartWindowMinutes()).isEqualTo(10);
        assertThat(result.get().getExamDelayDays()).isEqualTo(-1);
        assertThat(result.get().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(result.get().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
        assertThat(result.get().getTaCheckinTimeMinutes()).isEqualTo(20);
    }

    @Test
    public void shouldGetTimeLimitsConfigurationForAClientNameAndAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "SBAC Math 3-MATH-3";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId);

        Assertions.assertThat(result).isPresent();
        assertThat(result.get().getClientName()).isEqualTo(clientName);
        assertThat(result.get().getAssessmentId()).isNotNull();
        assertThat(result.get().getAssessmentId()).isEqualTo(assessmentId);
        assertThat(result.get().getEnvironment()).isEqualTo("Development");
        assertThat(result.get().getExamRestartWindowMinutes()).isEqualTo(10);
        assertThat(result.get().getExamDelayDays()).isEqualTo(-1);
        assertThat(result.get().getInterfaceTimeoutMinutes()).isEqualTo(10);
        assertThat(result.get().getRequestInterfaceTimeoutMinutes()).isEqualTo(15);
        assertThat(result.get().getTaCheckinTimeMinutes()).isEqualTo(50);
    }

    @Test
    public void shouldReturnOptionalEmptyTimeLimitsConfigurationForInvalidClientName() {
        final String clientName = "foo";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName);

        Assertions.assertThat(result).isNotPresent();
    }

    @Test
    public void shouldReturnOptionalEmptyTimeLimitsConfigurationForAValidClientNameAndInvalidAssessmentId() {
        final String clientName = "SBAC_PT";
        final String assessmentId = "foo";

        Optional<TimeLimitConfiguration> result = timeLimitConfigurationRepository.findTimeLimitConfiguration(clientName, assessmentId);

        Assertions.assertThat(result).isNotPresent();
    }
}
