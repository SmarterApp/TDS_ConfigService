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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import tds.config.ClientSystemFlag;
import tds.config.repositories.ConfigRepository;
import tds.config.repositories.impl.mappers.ClientSystemFlagRowMapper;

@Repository
public class ConfigRepositoryImpl implements ConfigRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final RowMapper<ClientSystemFlag> clientSystemFlagRowMapper = new ClientSystemFlagRowMapper();

    @Autowired
    public ConfigRepositoryImpl(final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ClientSystemFlag> findClientSystemFlags(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
            "SELECT\n" +
                "   auditobject AS auditObject,\n" +
                "   clientname AS clientName,\n" +
                "   ispracticetest AS isPracticeTest,\n" +
                "   ison AS isOn,\n" +
                "   description AS description,\n" +
                "   datechanged AS dateChanged,\n" +
                "   datepublished AS datePublished\n" +
                "FROM\n" +
                "   configs.client_systemflags\n" +
                "WHERE\n" +
                "   clientname = :clientName";

        List<ClientSystemFlag> clientSystemFlags;
        try {
            clientSystemFlags =
                jdbcTemplate.query(
                    SQL,
                    parameters,
                    clientSystemFlagRowMapper);
        } catch (DataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}", SQL, clientName);
            clientSystemFlags = Collections.emptyList();
        }

        return clientSystemFlags;
    }
}
