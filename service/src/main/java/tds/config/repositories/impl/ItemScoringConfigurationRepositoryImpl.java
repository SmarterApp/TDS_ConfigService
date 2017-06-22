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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import tds.config.repositories.ItemScoringConfigurationRepository;
import tds.student.sql.data.ItemScoringConfig;

@Repository
public class ItemScoringConfigurationRepositoryImpl implements ItemScoringConfigurationRepository {
    private static final ItemScoringConfigRowMapper itemScoringConfigRowMapper = new ItemScoringConfigRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ItemScoringConfigurationRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ItemScoringConfig> findItemScoringConfigs(final String clientName, final String siteName, final String serverName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("siteId", siteName)
            .addValue("serverName", serverName);

        String SQL = "SELECT\n" +
            "  C.context, \n" +
            "  C.itemtype, \n" +
            "  C.item_in, \n" +
            "  C.priority, \n" +
            "  C.serverurl\n" +
            "FROM \n" +
            "  client_itemscoringconfig C \n" +
            "WHERE\n" +
            "  C.clientname = :clientName \n" +
            "  AND (C.siteid = :siteId OR C.siteid = '*') \n" +
            "  AND (C.servername = :serverName OR C.servername = '*')";

        return jdbcTemplate.query(SQL, parameters, itemScoringConfigRowMapper);
    }

    private static class ItemScoringConfigRowMapper implements RowMapper<ItemScoringConfig> {
        @Override
        public ItemScoringConfig mapRow(final ResultSet rs, final int i) throws SQLException {
            ItemScoringConfig config = new ItemScoringConfig();

            config.setContext(rs.getString("context"));
            config.setItemType(rs.getString("itemType"));
            config.setEnabled(rs.getBoolean("item_in"));
            config.setPriority(rs.getInt("priority"));
            config.setServerUrl(rs.getString("serverurl"));

            return config;
        }
    }
}
