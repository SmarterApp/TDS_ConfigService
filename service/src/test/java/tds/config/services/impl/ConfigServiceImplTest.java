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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import tds.config.ClientSystemFlag;
import tds.config.repositories.ConfigRepository;
import tds.config.services.ConfigService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigServiceImplTest {

    private ConfigService configService;

    @Mock
    private ConfigRepository mockConfigRepository;

    @Before
    public void setUp() {
        configService = new ConfigServiceImpl(mockConfigRepository);
    }

    @After
    public void tearDown() {
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ClientSystemFlag Tests
    // -----------------------------------------------------------------------------------------------------------------
    @Test
    public void shouldGetAClientSystemFlag() {
        when(mockConfigRepository.findClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag("SBAC_PT", "AUDIT_OBJECT 1");

        assertThat(result.isPresent()).isTrue();

        ClientSystemFlag clientSystemFlag = result.get();
        assertThat(clientSystemFlag.getClientName()).isEqualTo("SBAC_PT");
        assertThat(clientSystemFlag.getAuditObject()).isEqualTo("AUDIT_OBJECT 1");
    }

    @Test
    public void shouldReturnOptionalEmptyClientSystemFlagForAnInvalidAuditObject() {
        when(mockConfigRepository.findClientSystemFlags("SBAC_PT")).thenReturn(getMockClientSystemFlag());

        Optional<ClientSystemFlag> result = configService.findClientSystemFlag("SBAC_PT", "foo");

        assertThat(result).isNotPresent();
    }

    @Test
    public void shouldReturnForceCompleteAssessmentIds() {
        when(mockConfigRepository.findForceCompleteAssessmentIds("SBAC")).thenReturn(Collections.singletonList("testId"));

        assertThat(configService.findForceCompleteAssessmentIds("SBAC")).containsExactly("testId");
    }

    private List<ClientSystemFlag> getMockClientSystemFlag() {
        List<ClientSystemFlag> clientSystemFlags = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ClientSystemFlag clientSystemFlag = new ClientSystemFlag.Builder()
                .withClientName("SBAC_PT")
                .withAuditObject("AUDIT_OBJECT " + i)
                .withDescription("unit test description " + i)
                .withEnabled(true)
                .withIsPracticeTest(true)
                .build();

            clientSystemFlags.add(clientSystemFlag);
        }

        return clientSystemFlags;
    }
}
