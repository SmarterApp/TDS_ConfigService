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

import java.util.Collection;
import java.util.List;

import tds.config.ClientSystemFlag;
import tds.config.repositories.ConfigRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ConfigRepositoryImplIntegrationTests {

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        String clientFlagInsertSQL = "INSERT INTO client_systemflags (auditobject,ison,description,clientname,ispracticetest,datechanged,datepublished) " +
            "VALUES ('accommodations',1,'keeps an audit trail of various changes to accommodations settings','SBAC_PT',1,'2011-06-01 11:27:47.980',NULL);";

        String clientTestPropertiesInsertSQL = "INSERT INTO `client_testproperties` VALUES ('SBAC','(SBAC) STSAssessment-ICA-5-2017-2018-81638630',3,NULL,2,NULL,'\\0','\u0001','Grade 5 ELA','','\u0001','\\0','ELA',NULL,NULL,'\u0001','\u0001',NULL,NULL,NULL,NULL,'ELA',NULL,'tds-testform','tds-testwindow','\\0','\\0',NULL,NULL,'\u0001','tds-testmode','\\0','\\0','\\0','\\0',1,0,'\\0','Grade 5',NULL,0,NULL,'\\0')";

        jdbcTemplate.update(clientFlagInsertSQL, new MapSqlParameterSource());
        jdbcTemplate.update(clientTestPropertiesInsertSQL, new MapSqlParameterSource());
    }

    @After
    public void tearDown() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlagsList() {
        final String clientName = "SBAC_PT";

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

        Assertions.assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        result.forEach(r -> {
            assertThat(r.getAuditObject()).isNotNull();
            assertThat(r.getClientName()).isEqualTo(clientName);
        });
    }

    @Test
    public void shouldGetEmptyListClientSystemFlagsForAnInvalidClientName() {
        final String clientName = "foo";

        List<ClientSystemFlag> result = configRepository.findClientSystemFlags(clientName);

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void shouldGetForceCompleteAssessmentIds() {
        Collection<String> assessmentIds = configRepository.findForceCompleteAssessmentIds("SBAC");
        assertThat(assessmentIds).containsOnly("(SBAC) STSAssessment-ICA-5-2017-2018-81638630");
    }
}