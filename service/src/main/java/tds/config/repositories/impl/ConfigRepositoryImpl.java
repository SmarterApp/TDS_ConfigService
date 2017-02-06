package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    private static final String MATCH_ANY = "--ANY--";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ConfigRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
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
                    new ClientSystemFlagRowMapper());
        } catch (DataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}", SQL, clientName);
            clientSystemFlags = Collections.emptyList();
        }

        return clientSystemFlags;
    }
}
