package tds.config.repositories.impl;

import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

import tds.config.model.ClientTestFormProperty;
import tds.config.repositories.ClientTestFormPropertiesQueryRepository;

@Repository
public class ClientTestFormPropertiesQueryRepositoryImpl implements ClientTestFormPropertiesQueryRepository {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ClientTestFormPropertiesQueryRepositoryImpl.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ClientTestFormPropertiesQueryRepositoryImpl(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<ClientTestFormProperty> findClientTestFormProperty(String clientName, String assessmentId) {
        final SqlParameterSource parameters = new MapSqlParameterSource("clientName", clientName)
            .addValue("assessmentId", assessmentId);

        String SQL = "SELECT testId, \n" +
            "clientname, \n" +
            "clientformid \n" +
            "FROM client_testformproperties \n" +
            "WHERE clientName = :clientName AND testId = :assessmentId";

        Optional<ClientTestFormProperty> maybeClientTestFormProperty = Optional.empty();
        try {
            final ClientTestFormProperty property = jdbcTemplate.queryForObject(SQL, parameters, (rs, rowNum) -> new ClientTestFormProperty(
                rs.getString("clientname"),
                rs.getString("testid"),
                rs.getString("clientformid")));

            maybeClientTestFormProperty = Optional.of(property);
        } catch (EmptyResultDataAccessException e) {
            LOG.debug(String.format("Failed to find a record for client %s and assessment %s", clientName, assessmentId));
        }

        return maybeClientTestFormProperty;
    }
}
