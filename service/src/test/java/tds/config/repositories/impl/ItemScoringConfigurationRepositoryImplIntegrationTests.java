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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import tds.common.data.mysql.UuidAdapter;
import tds.config.repositories.ItemScoringConfigurationRepository;
import tds.student.sql.data.ItemScoringConfig;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemScoringConfigurationRepositoryImplIntegrationTests {
    @Autowired
    private ItemScoringConfigurationRepository itemScoringConfigurationRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        SqlParameterSource parameterSource = new MapSqlParameterSource("key1", UuidAdapter.getBytesFromUUID(UUID.randomUUID()))
            .addValue("key2", UuidAdapter.getBytesFromUUID(UUID.randomUUID()));
        String SQL = "INSERT INTO " +
            "client_itemscoringconfig " +
            " VALUES " +
            "   ('SBAC_PT','*','*','*','HTQ','\u0001',1, :key1,'http://localhost:8080/itemscoring/Scoring/ItemScoring','Development')," +
            "   ('SBAC_PT','server','site','context','HTC','\u0001',1, :key2,'http://www.google.com','Development')";

        jdbcTemplate.update(SQL, parameterSource);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldReturnItemScoringConfigs() {
        List<ItemScoringConfig> configs = itemScoringConfigurationRepository.findItemScoringConfigs("SBAC_PT", "site", "server");

        assertThat(configs).hasSize(2);

        ItemScoringConfig defaultConfig = null;
        ItemScoringConfig customConfig = null;

        for(ItemScoringConfig config: configs) {
            if(config.getItemType().equals("HTQ")) {
                defaultConfig = config;
            } else {
                customConfig = config;
            }
        }

        assertThat(defaultConfig).isNotNull();
        assertThat(customConfig).isNotNull();

        assertThat(defaultConfig.getContext()).isEqualTo("*");
        assertThat(defaultConfig.getPriority()).isEqualTo(1);
        assertThat(defaultConfig.getServerUrl()).isEqualTo("http://localhost:8080/itemscoring/Scoring/ItemScoring");

        assertThat(customConfig.getItemType()).isEqualTo("HTC");
        assertThat(customConfig.getPriority()).isEqualTo(1);
        assertThat(customConfig.getContext()).isEqualTo("context");
        assertThat(customConfig.getServerUrl()).isEqualTo("http://www.google.com");
    }

    @Test
    public void shouldReturnDefaultItemScoringConfigs() {
        List<ItemScoringConfig> configs = itemScoringConfigurationRepository.findItemScoringConfigs("SBAC_PT",  null, null);

        assertThat(configs).hasSize(1);

        ItemScoringConfig defaultConfig = configs.get(0);

        assertThat(defaultConfig.getContext()).isEqualTo("*");
        assertThat(defaultConfig.getPriority()).isEqualTo(1);
        assertThat(defaultConfig.getServerUrl()).isEqualTo("http://localhost:8080/itemscoring/Scoring/ItemScoring");
    }
}
