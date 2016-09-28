package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import tds.config.ClientSystemFlag;
import tds.config.ClientTestProperty;
import tds.config.repositories.ConfigRepository;
import tds.config.repositories.impl.mappers.ClientSystemFlagRowMapper;
import tds.config.repositories.impl.mappers.ClientTestPropertyRowMapper;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class ConfigRepositoryImpl implements ConfigRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ConfigRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ClientSystemFlag> findClientSystemFlags(final String clientName) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName);

        final String SQL =
                "SELECT\n" +
                "   s.auditobject AS auditObject,\n" +
                "   s.clientname AS clientName,\n" +
                "   s.ispracticetest AS isPracticeTest,\n" +
                "   s.ison AS isOn,\n" +
                "   s.description AS description,\n" +
                "   s.datechanged AS dateChanged,\n" +
                "   s.datepublished AS datePublished\n" +
                "FROM\n" +
                "   configs.client_systemflags s\n" +
                "JOIN\n" +
                "   session.externs e\n" +
                "   ON (e.clientname = s.clientname\n" +
                "   AND e.ispracticetest = s.ispracticetest)\n" +
                "WHERE\n" +
                "   e.clientname = :clientName";

        List<ClientSystemFlag> clientSystemFlags;
        try {
            clientSystemFlags =
                    jdbcTemplate.query(
                            SQL,
                            parameters,
                            new ClientSystemFlagRowMapper());
        } catch(DataAccessException e) {
            LOG.debug("{} did not return results for clientName = {}", SQL, clientName);
            clientSystemFlags = Collections.emptyList();
        }

        return clientSystemFlags;
    }
}
